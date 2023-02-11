package com.roche.connect.appinstaller;

import java.util.HashMap;
import java.util.Map;

public class ValidationUtil {
	public final String REGX_PASSWORD_PATTERN = "^((?=.*\\d)(?!.*\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]).{8,25})$";
	public final String REGX_EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,5})$";
	public final String REGX_NAME_PATTERN = "^[a-zA-Z\\s-' ]+$";
	public final String REGX_USER_NAME_PATTERN = "^[0-9a-z_.]+$";
	// public final String oneChar = "^(?=.*[a-z])([0-9a-z\\s_.]+)$";
	 final String oneChar = "^(?=.*[a-z])([0-9a-z\\s_.]+)$";
	 public final String  specialCharRes = "^[a-zA-Z\\s-']+$";
	 public final String  specCharStart = "^[a-zA-Z]";
	 public final String  specCharEnd = "[a-zA-Z]+$";
	
	public boolean isEmpty(String s) {
		if (null == s || s.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public boolean checkMinLenth(String string, int minlenth) {
		if (null != string && string.trim().length() >= minlenth) {
			return true;
		}
		return false;
	}

	public boolean checkMaxLength(String string, int maxLength) {
		if (null != string && string.trim().length() <= maxLength) {
			return true;
		}
		return false;
	}

	public boolean isValidPassword(final String password) {
		return password.matches(REGX_PASSWORD_PATTERN);
	}

	public boolean isValidEmail(final String email) {
		return email.matches(REGX_EMAIL_PATTERN);
	}


	public   boolean isValidName(final String name) {
		return name.matches(REGX_NAME_PATTERN);
	}

	public boolean isValidUserName(final String name) {
		return name.matches(REGX_USER_NAME_PATTERN);
	}
	public boolean iscontainoneCha(final String name) {
		return name.matches(oneChar);
	}
	
	
	
	
public static void main(String[] args) {
	ValidationUtil util=new ValidationUtil();
	System.out.println(util.isValidPassword("Bh@$kar1"));
	

}

/*public int longestCommonSubstring(String username,String password) {
	
}*/
	public  boolean longestSubstring(String userName, String password) {

		StringBuilder sb = new StringBuilder();
		if (userName == null || userName.isEmpty() || password == null || password.isEmpty())
			return false;

// ignore case
		//userName = userName.toLowerCase();
		//password = password.toLowerCase();

// java initializes them already with 0
		int[][] num = new int[userName.length()][password.length()];
		int maxlen = 0;
		int lastSubsBegin = 0;

		for (int i = 0; i < userName.length(); i++) {
			for (int j = 0; j < password.length(); j++) {
				if (userName.charAt(i) == password.charAt(j)) {
					if ((i == 0) || (j == 0))
						num[i][j] = 1;
					else
						num[i][j] = 1 + num[i - 1][j - 1];

					if (num[i][j] > maxlen) {
						maxlen = num[i][j];
						// generate substring from str1 => i
						int thisSubsBegin = i - num[i][j] + 1;
						if (lastSubsBegin == thisSubsBegin) {
							// if the current LCS is the same as the last time this block ran
							sb.append(userName.charAt(i));
						} else {
							// this block resets the string builder if a different LCS is found
							lastSubsBegin = thisSubsBegin;
							sb = new StringBuilder();
							sb.append(userName.substring(lastSubsBegin, i + 1));
						}
					}
				}
			}
		}

		return (sb.toString().length())>=4;
	}
	public boolean isrepatecharmorethanFourtime(String password) {
		 Map<Character, Integer> map = new HashMap<>();
	boolean	isrepeatedchar  =false;
		if(!isEmpty(password)) {
			char[] chars = password.toCharArray();
	         
	      
	        for(char c : chars)
	        {
	            if(map.containsKey(c)) {
	                int counter = map.get(c);
	                map.put(c, ++counter);
	                if(counter>=4) {
	                	isrepeatedchar=true;
	                	break;
	                }
	            } else {
	                map.put(c, 1);
	            }
	        }
	       
			
		}
		 
		return isrepeatedchar;
		
	}
	
	public boolean ismacthpassword(String password,String confirmPassword) {
		
		if(!isEmpty(password)&&!isEmpty(confirmPassword)&&password.equals(confirmPassword)) {
			return true;
		}
		return false;
	}
	
}
