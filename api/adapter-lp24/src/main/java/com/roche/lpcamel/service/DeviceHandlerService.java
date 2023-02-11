package com.roche.lpcamel.service;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;

import org.apache.camel.Exchange;
import org.apache.camel.component.mina2.Mina2Constants;
import org.apache.mina.core.session.IoSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.constant.NotificationGroupType;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v251.segment.MSH;

@Component
public class DeviceHandlerService {

    /** The login url. */
    @Value("${pas.authenticate_url}")
    private String loginUrl;

    /** The login entity. */
    @Value("${pas.login_entity}")
    private String loginEntity;

    @Value("${pas.device_url}")
    String deviceEndPoint;

    @Value("${device.device_validate}")
    private boolean deviceFlag;
    
    @Value("${connect.adm_notification_url}")
    private String admNotificationURL;
    
    @Value("${pas.username}")
    private String userId;
    
    @Value("${connect.imm_url}")
    private String immUrl;
    
    @Autowired
    ResponseHandlerService responseHandlerService;
    
    private static final String PROTOCOL_VERSION = "protocolVersion";
    private static final String LOCATION_FIELD = "location";
    private static final String SOURCE_AND_DEVICETYPE = "?source=device&devicetype=";
    private static final String MESSAGE_TYPE = "&messagetype=";
    private static final String DEVICEID = "&deviceid=";
    
    /** The logger. */
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    private static Map<String, IoSession> deviceMap = new HashMap<>();

    private String deviceIP;

    private String protocolVersion;
    
    private String deviceSubStatus;
    
    private String deviceModel;
    
    private String hostName;
    
    private String location;

    public boolean updateDeviceStatus(String serialNo, String status) {
        logger.info("enters into update device status | serialNo:" + serialNo + " | status:" + status);
        boolean registeredStatus = true;
        if (deviceFlag) {
        	try {
            String parameters = "((serialNo='" + serialNo + "') & (state='ACTIVE') | (state='NEW'))";
            String url = deviceEndPoint + "/json/device/fetch/expr?filterExpression=" + URLEncoder.encode(parameters,"UTF-8");
            Builder builder = RestClientUtil.getBuilder(loginUrl, null);
            Entity<String> entity = Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED);
            String token = builder.post(entity, String.class);
            Builder builder2 = RestClientUtil.getBuilder(url, null);
            builder2.header("Cookie", "brownstoneauthcookie=" + token);
            String resp = builder2.get(String.class);
                logger.info("Fetch response::" + resp);
                JSONArray respobj = null;
                JSONObject updateJson = new JSONObject();
                registeredStatus = false;
                    respobj = new JSONArray(resp);
                    if (respobj.length() == 0) {
                        List<String> content=new ArrayList<>();
                        content.add(serialNo);
                        AdmNotificationService.sendNotification(NotificationGroupType.UNREGISTERED_DEVICE_LP24,content,token,admNotificationURL);
                        throw new HL7Exception("serial number mismatched");
                    }
                    JSONObject attributes = respobj.getJSONObject(0).getJSONObject("attributes");
                    String deviceType = respobj.getJSONObject(0).getJSONObject("deviceType").getString("name");
                    if (!(attributes.getString(PROTOCOL_VERSION).equalsIgnoreCase(protocolVersion)
                            || attributes.getString(PROTOCOL_VERSION).equals(""))) {
                        List<String> content=new ArrayList<>();
                        content.add(serialNo);
                        AdmNotificationService.sendNotification(NotificationGroupType.INVALID_HL7_VER_LP24,content,token,admNotificationURL);
                        throw new HL7Exception("HL7 protocol version mismatch");
                    }
                    if (!(deviceType.equalsIgnoreCase(getDeviceModel()))) {
                        List<String> content=new ArrayList<>();
                        content.add(serialNo);
                        AdmNotificationService.sendNotification(NotificationGroupType.INVALID_DEVICE_MODEL_LP24,content,token,admNotificationURL);
                        throw new HL7Exception("Device type mismatch");
                    }
                    updateJson.put("state", "ACTIVE");
                    updateJson.put("deviceStatus", status);
                    updateJson.put("ipAddress", deviceIP);
                    updateJson.put("hostName", hostName);
                    updateJson.put("make", "-");
                    if(deviceModel!=null)
                        updateJson.put("model", deviceModel);
                    else
                        updateJson.put("model", "-");
                    updateJson.put("hwVersion", "-");
                    updateJson.put("swVersion", "-");
                    updateJson.put(PROTOCOL_VERSION, protocolVersion);
                    updateJson.put("deviceSubStatus", deviceSubStatus);
                    if(attributes.getString(LOCATION_FIELD)==null || attributes.getString(LOCATION_FIELD).equals("") || attributes.getString(LOCATION_FIELD).equals("-"))
                        updateJson.put(LOCATION_FIELD, location);
                    else
                        updateJson.put(LOCATION_FIELD, "-");
                    
                try {
                    String oldDeviceSubStatus =
                        Optional.ofNullable(String.valueOf(attributes.get("deviceSubStatus"))).orElse(null);
                    logger.info("Old device Status :" + oldDeviceSubStatus);
                    logger.info("New device Status :" + deviceSubStatus);
                    if ("OP".equalsIgnoreCase(oldDeviceSubStatus) && "ID".equalsIgnoreCase(deviceSubStatus)) {
                        String updateIMMRunResultURL = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.LP24 + MESSAGE_TYPE
                            + MessageType.UPDATE_RUN + DEVICEID + serialNo;
                        
                        Map<String, String> requestBody = new HashMap();
                        requestBody.put("messageType", MessageType.UPDATE_RUN);
                        Entity entityObj = Entity.entity(requestBody, MediaType.APPLICATION_JSON);
                        
                        logger.info("Update run Result request URL to IMM :" + updateIMMRunResultURL);
                        responseHandlerService.postRequest(updateIMMRunResultURL, entityObj);
                        
                    }
                } catch (Exception e) {
                    logger.info("Exception: " + e.getMessage());
                }
                
                    url = deviceEndPoint + "/json/device/update/" + respobj.getJSONObject(0).get("deviceId").toString();
                    Builder builder3 = RestClientUtil.getBuilder(url, null);
                    builder3.header("Cookie", "brownstoneauthcookie=" + token);
                    builder3.post(Entity.entity(updateJson.toString(), MediaType.APPLICATION_JSON));
                    logger.info("Updated device status");
                    registeredStatus = true;
                    if(status.equalsIgnoreCase("offline")) {
                        List<String> content=new ArrayList<>();
                        content.add(serialNo);
                        AdmNotificationService.sendNotification(NotificationGroupType.DEVICE_OFFLINE_LP24,content,token,admNotificationURL);
                    }
                } catch (Exception e) {
                    logger.error("DeviceManagement | Closing connection: " + e.getMessage());
                    logger.error("DeviceManagement | Something went wrong while updating device status: " + e);
                }
        }
        return registeredStatus;
    }
    
    @Scheduled(fixedDelay = 5000)
    public void checkConnectionStatus() {
        try {
            if (deviceMap.size() != 0) {
                for (Map.Entry<String, IoSession> entry : deviceMap.entrySet()) {
                    if (!entry.getValue().isActive()) {
                        updateDeviceStatus(entry.getKey(), "offline");
                        deviceMap.remove(entry.getKey());
                    }
                }

            }

        } catch (JsonIOException | JsonSyntaxException e) {
            logger.error("DeviceManagement | Error while checking connection status: " + e);
        }
    }

    public boolean deviceValidation(Exchange exchange, MSH msh) {
        IoSession session = exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
        String remoteHost = (exchange.getIn().getHeader(Mina2Constants.MINA_REMOTE_ADDRESS).toString()).split(":")[0];
        String serialNo = msh.getMsh3_SendingApplication().getHd2_UniversalID().getValue();
        setDeviceIP(remoteHost.substring(1));
        setLocation(msh.getMsh4_SendingFacility().getHd1_NamespaceID().getValue());
        setDeviceModel(msh.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
        try {
            InetAddress addr = InetAddress.getByName(getDeviceIP());
            setHostName(addr.getHostName());
        } catch (UnknownHostException e) {
            logger.info("Device management | error while getting hostname::" +e.getMessage());
        }
        setProtocolVersion(msh.getMsh12_VersionID().getVersionID().getValue());
        boolean isRegisteredDevice;
        isRegisteredDevice = updateDeviceStatus(serialNo, "online");
        logger.info("after calling device status update method::" + isRegisteredDevice);
        if (!isRegisteredDevice) {
            session.closeNow();
        }else {
            getDeviceMap().put(serialNo, session);
        }
        return isRegisteredDevice;
    }

    public static Map<String, IoSession> getDeviceMap() {
        return deviceMap;
    }

    public static void setDeviceMap(Map<String, IoSession> deviceMap) {
        DeviceHandlerService.deviceMap = deviceMap;
    }

    public String getDeviceIP() {
        return deviceIP;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
    
    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeviceSubStatus() {
        return deviceSubStatus;
    }
    
    public void setDeviceSubStatus(String deviceSubStatus) {
        this.deviceSubStatus = deviceSubStatus;
    }

}
