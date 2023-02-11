package com.roche.connect.omm.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.util.ConfigurationParser;
import com.roche.connect.omm.error.CustomErrorCodes;

@Component
public class AES {
	/**@Value("${connect.secret_key}")
	private static Boolean secretKey;*/
	private static String key;
	private static String[] columns = { "patientFirstName", "patientLastName", "patientMedicalRecNo", "patientDOB",
			"refClinicianName", "otherClinicianName", "accountNumber", "labId",
			"reasonForReferral", "clinicName"};
	private static HMTPLogger logger = HMTPLoggerFactory.getLogger(AES.class.getClass().getName());

	public static String encrypt(String strToEncrypt) throws Exception {
		try {
			readKeyFile();
			byte[] keyBytes = Arrays.copyOf(key.getBytes(), 32);
			SecretKeySpec secret = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			if(strToEncrypt!=null)
				return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
			else
				return null;
		} catch (Exception e) {
			logger.error("Error while encrypting: " + e.toString());
			throw new HMTPException(e);
		}
		/**return null;*/
	}

	public static String decrypt(String strToDecrypt) {
		try {

			readKeyFile();
			byte[] keyBytes = Arrays.copyOf(key.getBytes(), 32);
			SecretKeySpec secret = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secret);
			if(strToDecrypt!=null){
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
			}
		} catch (Exception e) {
			logger.error("Error while decrypting: " + e.toString());
		}
		return null;
	}

	/**
	 * Gets the single instance of AES.
	 *
	 * @return single instance of AES
	 * @throws IOException 
	 * @throws HMTPException 
	 */
	public static synchronized String readKeyFile() throws IOException, HMTPException {
		if (AES.key == null) {
			try {
				
				key = new String(
				    Files.readAllBytes(Paths.get(ConfigurationParser.getString("connect.secret_key") + "/pass.key")));
						//Files.readAllBytes(Paths.get(AES.secretKey + "/pass.key")));
				if(key.isEmpty() || key==null){
					logger.error("key is empty: ");
					throw new HMTPException(CustomErrorCodes.INVALID_LAST_NAME_FORMAT,
							" Encription key is empty or null");
				}
				
			} catch (IOException e) {
				logger.error("Error while readKeyFile: " + e.toString());
				throw new HMTPException(e);
			}
		}
		return key;
	}

	@SuppressWarnings("unchecked")
	public static <V> Object getEncryptedObject(String name, Object dtoObject) {
		Class<?> clazz = null;
		Class<?> dtoclazz = null;
		Object object = null;
		try {
			clazz = Class.forName(name);
			dtoclazz = dtoObject.getClass();
			object = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			StringWriter sw = new StringWriter();
			e1.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
		} catch (ClassNotFoundException e) {
			logger.error("Error while encryptedObject: " + e.toString());
		}
		while (clazz != null) {
			for (String fieldName : columns) {
				try {
					Field dtoField = dtoclazz.getDeclaredField(fieldName);
					Field field = clazz.getDeclaredField(fieldName);
					field.setAccessible(true);
					dtoField.setAccessible(true);
					field.set(object, encrypt((String) (V) dtoField.get(dtoObject)));
				} catch (NoSuchFieldException e) {
					clazz = clazz.getSuperclass();
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
			}
			return object;
		}
		return null;
	}

	public static <T> T initializeAndUnproxy(T entity) {
		if (entity == null) {
			throw new NullPointerException("Entity instance is null. Unable to initialize.");
		}

		Hibernate.initialize(entity);
		entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		return entity;
	}

	@SuppressWarnings("unchecked")
	public static <V> Object getDecryptedObject(String name, Object entityObj) {

		Class<?> clazz = null;
		Class<?> entityClazz = null;
		Object object = null;

		try {
			clazz = Class.forName(name);
			entityClazz = entityObj.getClass();
			if (entityObj instanceof HibernateProxy) {
				entityObj = initializeAndUnproxy(entityObj);
				entityClazz = entityObj.getClass();
			}
			object = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			StringWriter sw = new StringWriter();
			e1.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
		} catch (ClassNotFoundException e) {
			logger.error("Error while decryptedObject: " + e.toString());
		}
		while (clazz != null) {
			for (String fieldName : columns) {
				try {
					Field entityField = entityClazz.getDeclaredField(fieldName);
					Field field = clazz.getDeclaredField(fieldName);
					field.setAccessible(true);
					entityField.setAccessible(true);
					field.set(object, decrypt((String) (V) entityField.get(entityObj)));
				} catch (NoSuchFieldException e) {
					clazz = clazz.getSuperclass();
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
			}
			return object;
		}
		return null;
	}
}