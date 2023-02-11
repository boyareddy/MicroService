package com.roche.nipt.dpcr.util;

import org.testng.annotations.Test;

public class TokenUtilsTest {
	
	@Test
	public void getToken() {
		
		try {
		String token=TokenUtils.getToken();
		System.out.println(token);
		}
		catch(Exception ex) {
			System.out.println("token null");
		}
		
	}
	@Test
	public void getTokenByReferesh() {
		
		try {
			String token=TokenUtils.getToken(true);
			System.out.println(token);
			}
			catch(Exception ex) {
				System.out.println("token null");
			}
		
	}
	@Test
	public void getTokenByNoReferesh() {
		
		try {
			String token=TokenUtils.getToken(false);
			System.out.println(token);
			}
			catch(Exception ex) {
				System.out.println("token null");
			}
		
	}

}
