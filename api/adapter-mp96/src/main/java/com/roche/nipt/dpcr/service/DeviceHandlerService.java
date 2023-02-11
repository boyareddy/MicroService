package com.roche.nipt.dpcr.service;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.nipt.dpcr.util.TokenUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.segment.MSH;

@Component
public class DeviceHandlerService {

	private HMTPLogger log = HMTPLoggerFactory.getLogger(this.getClass().getName());
	@Value("${pas.authenticate_url}")
	private String loginUrl;

	@Value("${pas.login_entity}")
	private String loginEntity;

	//added newly
	@Value("${pas.device_url}")
	String deviceEndPoint;

	@Value("${device.device_validate}")
	private boolean deviceFlag;
	
	@Value("${connect.adm_notification_url}")
	private String admNotificationURL;
	
	@Autowired
	MessageHandlerService messageHandlerService;
	
	
	private static Map<String, IoSession> deviceMap = new ConcurrentHashMap<>();
	
	private String deviceIP;
	
	private String protocolVersion;
	 
	private String deviceModel;
		
	private String hostName;
		
	private String location;
	
	private String deviceAssaytype;
	
	private boolean messageTypeFlag;
	
	private static final String OFFLINE = "offline";
	private static final String PROTOCOL_VERSION = "protocolVersion";
	private static final String LOCATION_FIELD = "location";

	/**
     * @return the messageTypeFlag
     */
    public boolean isMessageTypeFlag() {
        return messageTypeFlag;
    }

    /**
     * @param messageTypeFlag the messageTypeFlag to set
     */
    public void setMessageTypeFlag(boolean messageTypeFlag) {
        this.messageTypeFlag = messageTypeFlag;
    }

    public boolean updateDeviceStatus(String serialNo,String status) {
		log.info("enters into update device status | serialNo:" + serialNo + " | status:" + status);
		boolean registeredStatus = true;
		if (deviceFlag) {
			try {
			String parameters = "((serialNo='" + serialNo + "') & (state='ACTIVE') | (state='NEW'))";
			String url = deviceEndPoint + "/json/device/fetch/expr?filterExpression=" + URLEncoder.encode(parameters,"UTF-8");
			String token = TokenUtils.getToken();
			Builder builder2 = RestClientUtil.getBuilder(url, null);
			builder2.header("Cookie", "brownstoneauthcookie=" + token);
			String resp = builder2.get(String.class);
			log.info("Fetch response::" + resp);
			JSONArray respobj = null;
			JSONObject updateJson = new JSONObject();
			registeredStatus = false;
				respobj = new JSONArray(resp);
				if (respobj.length() == 0) {
					List<String> content=new ArrayList<>();
					content.add(serialNo);
					AdmNotificationService.sendNotification(NotificationGroupType.UNREGISTERED_DEVICE_MP96,content,token,admNotificationURL);
					throw new HL7Exception("serial number mismatched");
				}
				String deviceAssayType = respobj.getJSONObject(0).getJSONObject("deviceType").getJSONObject("attributes").getJSONArray("supportedAssayTypes").getString(0);
				setDeviceAssaytype(deviceAssayType);
				JSONObject attributes = respobj.getJSONObject(0).getJSONObject("attributes");
				String deviceType=respobj.getJSONObject(0).getJSONObject("deviceType").getString("name");
				if (!(attributes.getString(PROTOCOL_VERSION).equalsIgnoreCase(protocolVersion) || attributes.getString(PROTOCOL_VERSION).equals(""))) {
					List<String> content=new ArrayList<>();
					content.add(serialNo);
					AdmNotificationService.sendNotification(NotificationGroupType.INVALID_HL7_VER_MP96,content,token,admNotificationURL);
					throw new HL7Exception("HL7 protocol version mismatch");
				}
				if(messageTypeFlag && (!(deviceType.equalsIgnoreCase(getDeviceModel())))) {
					List<String> content=new ArrayList<>();
					content.add(serialNo);
					AdmNotificationService.sendNotification(NotificationGroupType.INVALID_DEVICE_MODEL_MP96,content,token,admNotificationURL);
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
				if(attributes.getString(LOCATION_FIELD) == null || attributes.getString(LOCATION_FIELD).equals("") || attributes.getString(LOCATION_FIELD).equals("-"))
					updateJson.put(LOCATION_FIELD, location);
				else
					updateJson.put(LOCATION_FIELD, "-");
				url = deviceEndPoint + "/json/device/update/" + respobj.getJSONObject(0).get("deviceId").toString();
				Builder builder3 = RestClientUtil.getBuilder(url, null);
				builder3.header("Cookie", "brownstoneauthcookie=" + token);
				builder3.post(Entity.entity(updateJson.toString(), MediaType.APPLICATION_JSON));
				log.info("Updated device status");
				registeredStatus = true;
				if(status.equalsIgnoreCase(OFFLINE)) {
					List<String> content=new ArrayList<>();
					content.add(serialNo);
					AdmNotificationService.sendNotification(NotificationGroupType.DEVICE_OFFLINE_MP96,content,token,admNotificationURL);
				}
			} catch (Exception e) {
				log.error("DeviceManagement | Closing connection: " + e.getMessage());
				log.error("DeviceManagement | Something went wrong while updating device status: " + e);
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
						updateDeviceStatus(entry.getKey(), OFFLINE);
						deviceMap.remove(entry.getKey());
					} 
				}

			}

		} catch (JsonIOException | JsonSyntaxException e) {
			log.error("DeviceManagement | error while checking connection status: " + e.getMessage());
		}
	}
	
	public boolean deviceValidation(Exchange exchange, MSH msh) {
		IoSession session = exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
		String remoteHost = (exchange.getIn().getHeader(Mina2Constants.MINA_REMOTE_ADDRESS).toString()).split(":")[0];
		String serialNo = msh.getMsh10_MessageControlID().getValue();
		setDeviceIP(remoteHost.substring(1));
		setLocation(msh.getMsh4_SendingFacility().getHd1_NamespaceID().getValue());
		setDeviceModel(msh.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
		try {
			InetAddress addr = InetAddress.getByName(getDeviceIP());
			setHostName(addr.getHostName());
		} catch (UnknownHostException e) {
			log.info("Device management | error while getting hostname::" +e.getMessage());
		}
		setProtocolVersion(msh.getMsh12_VersionID().getVersionID().getValue());
		boolean isRegisteredDevice;
		isRegisteredDevice = updateDeviceStatus(serialNo, "online");
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
	public String getDeviceAssaytype() {
		return deviceAssaytype;
	}

	public void setDeviceAssaytype(String deviceAssaytype) {
		this.deviceAssaytype = deviceAssaytype;
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

			
 
	
}
