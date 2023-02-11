package com.roche.nipt.dpcr.util;

import java.util.Date;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.dto.TokenDetails;

@Component
public class TokenUtils {

	private static HMTPLogger logger = HMTPLoggerFactory.getLogger(TokenUtils.class.getName());

	private static String token;

	/** The login entity. */
	private static String loginUrl;

	/** The login entity. */
	private static String loginEntity;

	/** The login token. */
	@Value("${pas.token_detail_url}")
	private static String tokenDetail;

	private static Date tokenExpiryDate;

	private TokenUtils() {

	}

	public static String getToken() {

		logger.info("Token Expiry Date : " + tokenExpiryDate);
		if (token == null || tokenExpiryDate == null || tokenExpiryDate.before(DateUtils.addMinutes(new Date(), 5))) {
			logger.info("Token is null or expired");
			token = getValidToken();
		}
		return token;
	}

	public static String getToken(boolean refresh) {

		if (refresh)
			tokenExpiryDate = getValidTokenDetails(token);

		return getToken();
	}

	private static Date getValidTokenDetails(String token) {

		String urlTokenDetail = "";
		try {
			urlTokenDetail = RestClientUtil.getUrlString("pas.security_url", "/json/security/", "tokenDetails", "",
					null);

			Builder builder = RestClientUtil.getBuilder(urlTokenDetail, null);
			builder.header("Cookie", "brownstoneauthcookie=" + token);

			TokenDetails tokenDetails = builder.get(TokenDetails.class);

			if (tokenDetails.getIssuedDate().before(tokenDetails.getExpiryDate())) {
				return tokenDetails.getExpiryDate();
			}
		} catch (NotAuthorizedException e) {
			logger.info("NotAuthorizedException:Token expired or invalid");
			token = null;
			tokenExpiryDate = null;
		} catch (HMTPException e) {
			logger.error("error at getValidTokenDetails() ", e);
			token = null;
			tokenExpiryDate = null;
		}
		return null;

	}

	private static String getValidToken() {

		Builder builder = RestClientUtil.getBuilder(loginUrl, null);
		Entity<String> entity = Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED);
		String token = builder.post(entity, String.class);
		tokenExpiryDate = getValidTokenDetails(token);
		logger.debug("exit ::getValidToken()- Token Expiry date:" + tokenExpiryDate);
		return token;
	}

	public static Date getTokenExpiryDate() {
		return tokenExpiryDate;
	}

	public static void setTokenExpiryDate(Date tokenExpiryDate) {
		TokenUtils.tokenExpiryDate = tokenExpiryDate;
	}

	@Value("${pas.authenticate_url}")
	public void setLoginUrl(String loginUri) {
		loginUrl = loginUri;
	}

	@Value("${pas.login_entity}")
	public void setLoginEntity(String tmploginEntity) {
		loginEntity = tmploginEntity;
	}
}
