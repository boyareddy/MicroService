package com.roche.nipt.dpcr.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.CharEncoding;

import com.hcl.hmtp.common.server.rest.RestClientUtil;

public class RestClient {

	public static final String COOKIE_STR = "Cookie";

	public static final String COOKIE_KEY = "brownstoneauthcookie=";

	private RestClient() {

	}

	public static Response post(String url, Object object, String token, List<Object> providers)
			throws UnsupportedEncodingException {

		return post(url, Entity.entity(object, MediaType.APPLICATION_JSON), token, providers);
	}

	public static Response post(String url, Entity entity, String token, List<Object> providers)
			throws UnsupportedEncodingException {

		Builder builder = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), providers);
		if (token != null)
			builder.header(COOKIE_STR, COOKIE_KEY + token);
		return builder.post(entity);
	}
	
	public static <T> T post(String url, Entity entity, List<Object> providers, Class<T> responseType)
			throws UnsupportedEncodingException {

		Builder builder = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), providers);
		return builder.post(entity, responseType);
	}

	public static Response put(String url, Object object, String token, List<Object> providers)
			throws UnsupportedEncodingException {

		Builder builder = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), providers);
		if (token != null)
			builder.header(COOKIE_STR, COOKIE_KEY + token);

		return builder.put(Entity.entity(object, MediaType.APPLICATION_JSON));

	}

}
