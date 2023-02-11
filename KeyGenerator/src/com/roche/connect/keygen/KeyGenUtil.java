package com.roche.connect.keygen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.yaml.snakeyaml.Yaml;

 
public class KeyGenUtil {
 
    private static SecretKey secretKey;
    private static SecureRandom seureRandom ;
 
   
    public static void setKey1(String yamlLocation) throws IOException
    {
        try {
        	
        	Yaml yaml = new Yaml();
        	InputStream inputStream = Files.newInputStream( Paths.get(yamlLocation));
        	Map<String, Object> obj = yaml.load(inputStream);
        	seureRandom = SecureRandom.getInstanceStrong() ;
        	byte[] b = new byte[32] ;
        	seureRandom.nextBytes(b);
        	KeyGenerator keyGen = KeyGenerator.getInstance("AES") ;
            keyGen.init(256,seureRandom) ;
            secretKey = keyGen.generateKey();
//            System.out.println(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
            Path path = Paths.get(obj.get("secret_key") + "/pass.key");
            File file = path.toFile();
            
            if(file.exists()) {
            	throw new IOException("Key file already exists which is in use::::");
//            	 PrintWriter writer = new PrintWriter(file);
//                 writer.print(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
//                 writer.close();
                 
            } else {
            	if (file.getParentFile().exists()) {
                    file.createNewFile();
                    PrintWriter writer = new PrintWriter(file);
                    writer.print(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
                    writer.close();
                } else if(file.getParentFile().mkdir()) {
                	file.createNewFile();
                    PrintWriter writer = new PrintWriter(file);
                    writer.print(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
                    writer.close();
                } else {
                    throw new IOException("Failed to create directory " + file.getParent());
                }
            	System.out.println("Key file has been generated successfully::::");
            }
            
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) throws IOException
    {
    	if(args!= null && args.length > 0) {
    		setKey1(args[0]);
    	} else {
    		
    	}
    	
    }

}