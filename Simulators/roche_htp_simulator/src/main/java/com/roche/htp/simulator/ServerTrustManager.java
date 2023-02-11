package com.roche.htp.simulator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class ServerTrustManager implements X509TrustManager {
	public static final Logger logger = Logger.getLogger(WebServicesClientResponse.class);
	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");   
		FileInputStream finStream = null;;
		try {
			finStream = new FileInputStream(this.readProperties("sslCertificatePath"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		X509Certificate caCertificate = (X509Certificate)cf.generateCertificate(finStream);  
		 
		    if (certs == null || certs.length == 0) {  
		        throw new IllegalArgumentException("null or zero-length certificate chain");  
		   }  

		   if (authType == null || authType.length() == 0) {  
		              throw new IllegalArgumentException("null or zero-length authentication type");  
		    }  

		     //Check if certificate send is your CA's
		      if(!certs[0].equals(caCertificate)){
		           try
		           {   //Not your CA's. Check if it has been signed by your CA
//		               certs[0].verify(caCertificate.getPublicKey());
		        	   certs[0].checkValidity();
		           }
		           catch(Exception e){   
		                throw new CertificateException("Certificate not trusted"+e);
		           }
		      }
		      //If we end here certificate is trusted. Check if it has expired.  
		       try{
		            certs[0].getIssuerUniqueID();
		        }
		        catch(Exception e){
		              throw new CertificateException("Certificate not trusted. It has expired"+e);
		        }  
		/*arg0[0].checkValidity();	
		arg0[0].getIssuerUniqueID();
		arg0[0].getSubjectDN();*/
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}
	public String readProperties(String propKey) {
        Properties prop = new Properties();
        try {
            FileReader fr = new FileReader("resources//simulator.properties");
            prop.load(fr);
        } catch (IOException e) {
            logger.error(String.format("Exception while getting file path... %s", e));
        }
        return prop.getProperty(propKey);
    }

}
