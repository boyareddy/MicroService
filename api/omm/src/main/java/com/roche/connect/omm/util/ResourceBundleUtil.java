package com.roche.connect.omm.util;

import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;

@Component
public class ResourceBundleUtil {
	private static final Locale defaultLocale = Locale.US;
	private final Set<Locale> validLocales = new HashSet<>();

	public ResourceBundleUtil() {
		validLocales.add(Locale.US);
		validLocales.add(Locale.FRENCH);
	}

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	public ResourceBundle getResourceBundle() throws HMTPException {
		ResourceBundle resourceBundle = null;
		Locale currentLocale = null;
		try {
			currentLocale = LocaleContextHolder.getLocale();
			if (currentLocale != null && isLocaleValid(currentLocale))
				resourceBundle = ResourceBundle.getBundle("messages", currentLocale);
			else
				resourceBundle = ResourceBundle.getBundle("messages", defaultLocale);
		} catch (Exception exp) {
			throw new HMTPException(exp.getMessage());
		}
		return resourceBundle;
	}

	public String getMessages(ResourceBundle resourceBundle, String key, Object... obj) {
		String errMsg = "";
		try {
			if (obj == null)
				errMsg = resourceBundle.getString(key);
			else
				errMsg = String.format(resourceBundle.getString(key), obj);
		} catch (Exception exp) {
			logger.info("Error occured while getting the messages from message.properties file");
		}
		return errMsg;
	}

	private boolean isLocaleValid(Locale currentLocale) {
		return validLocales.contains(currentLocale);
	}

}
