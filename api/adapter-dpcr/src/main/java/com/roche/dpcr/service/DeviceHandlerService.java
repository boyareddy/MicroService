package com.roche.dpcr.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;
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
import com.roche.dpcr.dto.MessageExchange;
import com.roche.dpcr.util.TokenUtils;

import ca.uhn.hl7v2.HL7Exception;

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

	private static final String PROTOCOL_VERSION = "protocolVersion";
	private static final String LOCATION_FIELD = "location";
	private static final String OFFLINE = "offline";
	private static final String ONLINE = "online";
	private static final int TOKEN_VALIDITY=1*60*1000;

	/** The logger. */
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	private static Map<String, MessageExchange> deviceMap = new ConcurrentHashMap<>();
	private static Map<String, Timestamp> devicePingMap = new ConcurrentHashMap<>();

	private String deviceIP;

	private String protocolVersion;

	private String deviceSubStatus;

	private String deviceModel;

	private String hostName;

	private String location;

	public boolean updateDeviceStatus(String serialNo, String status) throws UnsupportedEncodingException {
		logger.info("enters into update device status | serialNo:" + serialNo + " | status:" + status);
		boolean registeredStatus = true;
		if (deviceFlag) {
			String parameters = "((serialNo='" + serialNo + "') & (state='ACTIVE') | (state='NEW'))";
			String url = deviceEndPoint + "/json/device/fetch/expr?filterExpression=" + URLEncoder.encode(parameters,"UTF-8");
						
			String token = TokenUtils.getToken();
			
			Builder builder2 = RestClientUtil.getBuilder(url, null);
			builder2.header("Cookie", "brownstoneauthcookie=" + token);
			String resp = builder2.get(String.class);
			logger.info("Fetch response::" + resp);
			JSONArray respobj = null;
			JSONObject updateJson = new JSONObject();
			JSONObject attributes = new JSONObject();
			registeredStatus = false;
			try {
				respobj = new JSONArray(resp);

				if (respobj.length() == 0) {

					List<String> content = new ArrayList<>();
					content.add(serialNo);
					AdmNotificationService.sendNotification(NotificationGroupType.UNREGISTERED_DEVICE_DPCR, content,
							token, admNotificationURL);

					throw new HL7Exception("serial number mismatched");
				}

				attributes = respobj.getJSONObject(0).getJSONObject("attributes");
				String deviceType = respobj.getJSONObject(0).getJSONObject("deviceType").getString("name");
				if (!(deviceType.equalsIgnoreCase(getDeviceModel()))) {

					List<String> content = new ArrayList<>();
					content.add(serialNo);
					AdmNotificationService.sendNotification(NotificationGroupType.INVALID_DEVICE_MODEL_DPCR, content,
							token, admNotificationURL);

					throw new HL7Exception("Device type mismatch");
				}

				updateJson.put("state", "ACTIVE");
				updateJson.put("deviceStatus", status);
				updateJson.put("ipAddress", deviceIP);
				updateJson.put("hostName", hostName);
				updateJson.put("make", "-");
				if (deviceModel != null)
					updateJson.put("model", deviceModel);
				else
					updateJson.put("model", "-");
				updateJson.put("hwVersion", "-");
				updateJson.put("swVersion", "-");
				updateJson.put(PROTOCOL_VERSION, protocolVersion);

				if (status.equals(OFFLINE))
					deviceSubStatus = "";

				updateJson.put("deviceSubStatus", deviceSubStatus);
				logger.info("Device Sub status:" + deviceSubStatus);
				if (attributes.getString(LOCATION_FIELD) == null || attributes.getString(LOCATION_FIELD).equals("")
						|| attributes.getString(LOCATION_FIELD).equals("-"))
					updateJson.put(LOCATION_FIELD, location);
				else
					updateJson.put(LOCATION_FIELD, "-");
				url = deviceEndPoint + "/json/device/update/" + respobj.getJSONObject(0).get("deviceId").toString();
				Builder builder3 = RestClientUtil.getBuilder(url, null);
				builder3.header("Cookie", "brownstoneauthcookie=" + token);
				builder3.post(Entity.entity(updateJson.toString(), MediaType.APPLICATION_JSON));
				logger.info("Updated device status");
				registeredStatus = true;

				if (status.equalsIgnoreCase(OFFLINE)) {
					List<String> content = new ArrayList<>();
					content.add(serialNo);
					AdmNotificationService.sendNotification(NotificationGroupType.DEVICE_OFFLINE_DPCR, content, token,
							admNotificationURL);
				}

			} catch (Exception e) {
				logger.error("DeviceManagement | Closing connection: " + e.getMessage());
				logger.error("DeviceManagement | Something went wrong while updating device status: " + e);
			}
		}
		return registeredStatus;
	}

	public void sendNotification(String serialNo, String message) {
		/*** sendNotification **/
	}

	@Scheduled(fixedDelay = 5000)
	public void checkConnectionStatus() throws UnsupportedEncodingException {
				
		try {
			if (deviceMap.size() != 0) {
				for (Map.Entry<String, MessageExchange> entry : deviceMap.entrySet()) {
					
					String msgControlId =entry.getKey();
					
					Timestamp val = devicePingMap.get(msgControlId+"^"+ entry.getValue().getSerialNo());
					if(val!=null) {
					Timestamp currenttimestamp = new Timestamp(System.currentTimeMillis()-TOKEN_VALIDITY);
					
										
					if (entry.getValue().getSocket().isClosed() && currenttimestamp.after(val)) {
						logger.info(
								"Device connection status " + entry.getValue().getSerialNo() + ":" + entry.getValue().getSocket().isClosed());
						updateDeviceStatus(entry.getValue().getSerialNo(), OFFLINE);
						deviceMap.remove(entry.getKey());
					}
					}
				}

			}
			if (devicePingMap.size() != 0) {
				for (Map.Entry<String, Timestamp> entry : devicePingMap.entrySet()) {
					Timestamp timestamp = new Timestamp(System.currentTimeMillis()-TOKEN_VALIDITY);
					if (entry.getValue()!=null && timestamp.before(entry.getValue())) {
						logger.info("Device connection status " + entry.getValue() + ":online");
						updateDeviceStatus(entry.getKey().substring(entry.getKey().indexOf('^')+1,entry.getKey().length()), ONLINE);						
					}					
				}

			}

		} catch (JsonIOException | JsonSyntaxException e) {
			logger.error("DeviceManagement | Error while checking connection status: " + e);
		}
	}

	public boolean deviceValidation(Socket socket, MessageExchange messageExchange) throws UnsupportedEncodingException {

		String remoteHost = (socket.getRemoteSocketAddress().toString()).split(":")[0];
		String serialNo = messageExchange.getSerialNo();
		String msgControlId=messageExchange.getMsgControlId();
		setDeviceIP(remoteHost.substring(1));
		setLocation(messageExchange.getSendingApp());
		setDeviceModel(messageExchange.getDeviceType());
		setDeviceSubStatus(messageExchange.getDeviceSubStatus());
		try {
			InetAddress addr = InetAddress.getByName(getDeviceIP());
			setHostName(addr.getHostName());
		} catch (UnknownHostException e) {
			logger.info("Device management | error while getting hostname::" + e.getMessage());
		}
		setProtocolVersion(messageExchange.getMsgVersion());
		boolean isRegisteredDevice;
		isRegisteredDevice = updateDeviceStatus(serialNo, ONLINE);
		logger.info("after calling device status update method::" + isRegisteredDevice);
		if (!isRegisteredDevice) {
			try {
				socket.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		} else if(messageExchange.getDeviceSubStatus().equals("ID")|| messageExchange.getDeviceSubStatus().equals("OP") ||messageExchange.getDeviceSubStatus().equals("ES")) {
				 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				 getDevicePingMap().put(msgControlId+"^"+serialNo, timestamp);
			getDeviceMap().put(msgControlId, messageExchange);
		}
		return isRegisteredDevice;
	}

	public static Map<String, MessageExchange> getDeviceMap() {
		return deviceMap;
	}

	public static void setDeviceMap(Map<String, MessageExchange> deviceMap) {
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

	public static Map<String, Timestamp> getDevicePingMap() {
		return devicePingMap;
	}

	public static void setDevicePingMap(Map<String, Timestamp> devicePingMap) {
		DeviceHandlerService.devicePingMap = devicePingMap;
	}

}
