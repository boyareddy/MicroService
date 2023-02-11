package com.roche.connect.amm.util;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;

public class FlagDescriptionCache {
	private static FlagDescriptionCache instance;
	private Map<String, JSONObject> dataMap;
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	private FlagDescriptionCache() {
		dataMap = new ConcurrentHashMap<>();
	}

	public static synchronized FlagDescriptionCache getInstance() {
		if (instance == null) {
		instance = new FlagDescriptionCache();
		}
		return instance;
	}

	public void put(String key, JSONObject value) {
		dataMap.put(key, value);
	}

	public JSONObject get(String key, String url) {
		JSONObject flagDes = null;
		if (key != null && url != null) {
			if (containskey(key)) {
				flagDes = dataMap.get(key);
			} else {
				flagDes = getMap(key, url);
			}
		}
		return flagDes;

	}

	public JSONObject getMap(String key, String url) {
		JSONObject flagDes = null;
		if (key.contains(",")) {
			String[] var = key.split(",");
			String locale = var[0];
			String deviceType = var[1];
			try {
				flagDes = new JSONObject(IOUtils.toString(new URL(url + "." + locale.replace("_", "-") + ".json"),
						Charset.forName("UTF-8")));
			} catch (JSONException | IOException r) {
				key = Locale.US.toString() + "," + deviceType;
				if (!dataMap.containsKey(key)) {
					try {
						flagDes = new JSONObject(
								IOUtils.toString(new URL(url + "." + Locale.US.toString().replace("_", "-") + ".json"),
										Charset.forName("UTF-8")));
					} catch (JSONException | IOException e) {
						logger.error("error while loading the file " + e.getMessage());
					}
				}
			}
			if (flagDes != null && flagDes.has(deviceType.toLowerCase())) {
				dataMap.put(key, flagDes.getJSONObject(deviceType.toLowerCase()));
			}

		}
		return dataMap.get(key);

	}

	public boolean containskey(String key) {
		return dataMap.containsKey(key);
	}

}