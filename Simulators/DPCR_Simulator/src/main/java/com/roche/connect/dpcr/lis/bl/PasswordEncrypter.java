package com.roche.connect.dpcr.lis.bl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class PasswordEncrypter {
            public static void main(String[] s) {
                        try {
                                    String password = "Connect@123";
                                    String key = "P@ssw0rd@f0r2019";
                                    Key shaKey = new SecretKeySpec(key.getBytes("UTF8"), "AES");
                               Cipher cipher = Cipher.getInstance("AES");
                                   cipher.init(Cipher.ENCRYPT_MODE, shaKey);
                                    String encrypted = Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes("UTF-8")));
                                    String input = new String(encrypted);
                                    System.err.println("Encripted String :"+ input);

                                    /*//decrypting 
                                    Cipher cipher = Cipher.getInstance("AES");
                                    cipher.init(Cipher.DECRYPT_MODE, shaKey);
                                    byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode("3E3YydU6tQiLmfbaZ7HIZw=="));
                                    System.out.println("Decrypted String :"+new String(decrypted));*/
                                    
                        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {

                                    e.printStackTrace();
                        } catch (InvalidKeyException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                        } catch (BadPaddingException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                        }
            }

}
