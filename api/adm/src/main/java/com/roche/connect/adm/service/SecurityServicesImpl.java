package com.roche.connect.adm.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.util.ADMConstants;

@Service
public class SecurityServicesImpl implements SecurityServices {

	@Value("${pas.security_remote_url}")
	private String securityHostURL;

	private HMTPLogger log = HMTPLoggerFactory.getLogger(this.getClass().getName());

	public Set<String> getRoles(Set<Integer> roleIds) throws HMTPException {

		log.info("Entering in to getRoles()" + roleIds);

		String url = securityHostURL + ADMConstants.GET_ROLES_URL.toString();

		Invocation.Builder resp = null;

		try {
			resp = RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null);
		} catch (UnsupportedEncodingException e1) {
			throw new HMTPException("Exception occured on getting roles");
		}

		String respBody = null;

		Set<String> roleName = new HashSet<String>();

		if (resp.get().getStatus() == HttpStatus.SC_OK) {

			respBody = resp.get(new GenericType<String>() {
			});

			ObjectMapper mapper = new ObjectMapper();

			JsonNode roles;
			try {
				log.info(mapper.writeValueAsString(respBody));
				roles = mapper.readTree(respBody).get("roles");
				for (JsonNode r : roles) {
					//roleName.add(r.get("name").textValue());
                  if (roleIds.contains(r.get("id").asInt())) {
						roleName.add(r.get("name").textValue());
					}
				}
			} catch (IOException e) {
				log.info("Exception on SecurityServicesImpl.getRoles(): " + e.getMessage());
			}

		}

		return roleName;
	}
}
