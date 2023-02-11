package com.roche.connect.appinstaller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.KeyStore;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.UrlValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AppInstallerController {
private final static String  KEYSTORE_TYPE="PKCS12";
private final static String  CACERTSPASSWORD="changeit";
private static Properties configPropMap = ConfigurationProperties.propertiesMap;
	@FXML
	private Button browse_btn;
	
	@FXML
	private Button browse_btn1;

	@FXML
	private TextField config_txt;
	
	@FXML
	private TextField config_txt1;
	
	@FXML
	private Button config_btn_next;
	
	@FXML
	private Button cert_next_btn;
	
	@FXML
	private Button pi_browse_btn;
	
	@FXML
	private TextField sslKey;
	
	@FXML
	private Text prblmerrortxt;
	
	@FXML
	private Text bkperrortxt;
	
	@FXML
	private Text certError;
	@FXML
	private PasswordField sslPasword;
	

	public void initialize() {
	}

	@FXML
	private void handleBrowseButtonAction(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select Path");
		File selectedFile = directoryChooser.showDialog(null);
		if (selectedFile != null && selectedFile.exists()) {
			if (!Files.isWritable(new File(selectedFile.getAbsolutePath()).toPath())) {
				config_txt.setText("");
				prblmerrortxt.setVisible(true);

			} else {
				prblmerrortxt.setVisible(false);
				config_txt.setText(selectedFile.getAbsolutePath());
				if(config_txt1.getText()!=null && (!config_txt1.getText().isEmpty())) {
					config_btn_next.setDisable(false);
				}
			}
		}
	}
	
	@FXML
	private void handleBackupBrowseButtonAction(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select Path");
		File selectedFile = directoryChooser.showDialog(null);
		if (selectedFile != null && selectedFile.exists()) {
			if (!Files.isWritable(new File(selectedFile.getAbsolutePath()).toPath())) {
				config_txt1.setText("");
				bkperrortxt.setVisible(true);

			} else {
				bkperrortxt.setVisible(false);
				config_txt1.setText(selectedFile.getAbsolutePath());
				if(config_txt.getText()!=null && (!config_txt.getText().isEmpty())) {
					config_btn_next.setDisable(false);
				}
			}
		}
	}
	
	@FXML
	private void handleSSLBrowseButtonAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Certificate screen");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PFX", Arrays.asList("*.p12","*.pfx")));
		final File file = fileChooser.showOpenDialog(null);
		

		try {
			if (file == null||file.length()==0) {
				throw new ConnectInstallerException("Certificate is corrupted or empty, please upload a valid certificate.");
			}sslKey.setText(file.getAbsolutePath());
			cert_next_btn.setDisable(false);
		} catch (Exception e) {
		
			certError.setVisible(true);
		
			cert_next_btn.setDisable(true);
			certError.setText(e.getMessage());
			
		}
	}
	
	
	private static int hear(BufferedReader in) throws IOException {
		String line = null;
		int res = 0;

		while ((line = in.readLine()) != null) {
			String pfx = line.substring(0, 3);
			try {
				res = Integer.parseInt(pfx);
			} catch (Exception ex) {
				res = -1;
			}
			if (line.charAt(3) != '-')
				break;
		}

		return res;
	}

	private static void say(BufferedWriter wr, String text) throws IOException {
		wr.write(text + "\r\n");
		wr.flush();

		return;
	}

	public static boolean isAddressValid(String address, String serverAddress, int port) throws Exception {
		int pos = address.indexOf('@');

		if (pos == -1)
			return false;

			boolean valid = false;
			Socket skt = null;
			BufferedReader rdr = null;
			BufferedWriter wtr = null;
			try {
				int res;
				//
				try {
				skt = new Socket((String) serverAddress, port);
				}catch(Exception e) {
					throw new Exception("Server not reachable.");
				}
				rdr = new BufferedReader(new InputStreamReader(skt.getInputStream()));
				wtr = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));

				res = hear(rdr);
				if (res != 220)
					throw new Exception("Invalid SMTP server.");
				say(wtr, "EHLO "+serverAddress);

				res = hear(rdr);
				if (res != 250)
					throw new Exception("Not ESMTP");

				say(wtr, "MAIL FROM: <"+address+">");
				res = hear(rdr);
				if (res != 250)
					throw new Exception("Please enter a valid User name.");

				say(wtr, "RCPT TO: <" + address + ">");
				res = hear(rdr);

				say(wtr, "RSET");
				hear(rdr);
				say(wtr, "QUIT");
				hear(rdr);
				if (res != 250)
					throw new Exception("Please enter a valid User name.");

				valid = true;
				
			} catch (Exception ex) {
				throw ex;
			} finally {
				if(rdr != null)
					rdr.close();
				if(wtr != null)
					wtr.close();
				if(skt != null)
					skt.close();
				if (valid)
					return true;
			}
		return false;
	}
	
	static boolean validateFQDN(String ConnectFQDN) throws Exception {
		
		String[] schemes = {"https"};
		String temp = null;
		UrlValidator  urlValidator  = new UrlValidator(schemes);
		if(!ConnectFQDN.startsWith("https://")){
			temp = "https://"+ConnectFQDN;
		}else {
			throw new Exception("Enter a valid Fully Qualified Domain Name (FQDN).");
		}
		
		if(urlValidator.isValid(temp)) {
			try {
			InetAddress.getByName(ConnectFQDN);
			}catch(Exception e) {
				throw new Exception("FQDN could not be resolved to a server IP address. Please enter a valid FQDN.");
			}
			return true;
			
		}
		else {
			throw new Exception("Enter a valid Fully Qualified Domain Name (FQDN).");
		}
		
	}
	
	static boolean validateLabDomain(String labDomain) throws Exception {
		
	DomainValidator  domainValidator  = DomainValidator.getInstance(false);
	
		
		if(domainValidator.isValid(labDomain)) {
			return true;
			
		}
		else {
			throw new Exception("Enter a valid lab domain name.");
		}
		
	}
	
	public static boolean impCert(String pfxkeypath, String certkeypath, String password) throws Exception {
		InputStream certIn = null;
		InputStream localCertIn = null;
		FileOutputStream fos = null;
		OutputStream out = null;
		String alias = null;
		char[] pwdArray = null;
		String certfilepath = null;
		String destpath = null;
		 String cnName=null;
		
		try {
			configPropMap.setProperty("keyAlias","");
			configPropMap.setProperty("keyStoreType", "");
			configPropMap.setProperty("key-store-password", "");
			if (nullcheckAndFileExit(pfxkeypath)) {
				if (password == null) {
					password = "";
				}
				certIn = new FileInputStream(new File(pfxkeypath));
				final char sep = File.separatorChar;
				destpath = System.getProperty("java.home") + sep + "lib" + sep + "security";
				File dir = new File(destpath);
				File file = new File(dir, "cacerts");
				pwdArray = CACERTSPASSWORD.toCharArray();			
				localCertIn = new FileInputStream(file);
				KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
				keystore.load(localCertIn, pwdArray);
				KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
				ks.load(certIn, password.toCharArray());
				Enumeration<String> aliases = ks.aliases();
				while (aliases.hasMoreElements()) {
					alias = aliases.nextElement();	
					 X509Certificate c = (X509Certificate) ks.getCertificate(alias);    
			            Principal subject = c.getSubjectDN();  
			            if(subject.toString()!=null) {
			            	 Map< String, String> details=new HashMap<>();
			 	            Arrays.asList(subject.toString().split(",")).stream().forEach(
			 	            		item -> {
			 	            			if(item!=null && item.contains("=")) {
			 	            				if(item.split("=").length>=2) {
			 	            					details.put(item.trim().split("=")[0], item.trim().split("=")[1]);
			 	            				}
			 	            			}			 	            			
			 	            		});	 	            
			 	        if(details.containsKey("CN")) {
			 	        	cnName=details.get("CN");
			 	        }
			            }
			
					
				}
				if(cnName.equals(configPropMap.getProperty("fqdnserver"))) {
					configPropMap.setProperty("keyAlias",alias);
					configPropMap.setProperty("keyStoreType", "PKCS12");
					configPropMap.setProperty("key-store-password",password);
					if (keystore.containsAlias(cnName)) {
						certIn.close();
						localCertIn.close();
			
					}
					certIn = new FileInputStream(new File(pfxkeypath));
					KeyStore ks1 = KeyStore.getInstance(KEYSTORE_TYPE);
				
					while (certIn.available() > 0) {
						ks1.load(certIn, password.toCharArray());
						Certificate cert = ks.getCertificate(alias);
						certfilepath = destpath + sep + alias + ".crt";
						try {
							keystore.setCertificateEntry(cnName, cert);
							
						fos = new FileOutputStream(certfilepath);
						ks1.store(fos,password.toCharArray());
						

						fileCopy(pfxkeypath, destpath + sep + new File(pfxkeypath).getName());
						}catch(Exception e) {
							throw new ConnectInstallerException("Access denied while importing  the certificate.");
						}
					}
					out = new FileOutputStream(file);
					keystore.store(out, pwdArray);
					
					}else {
						throw new  ConnectInstallerException("FQDN is not matching with Common Name of the key.");
					}
			}
		} catch (Exception exception) {
			throw exception;
		} finally {
			if (certIn != null)
				certIn.close();
			if (localCertIn != null)
				localCertIn.close();
			if (fos != null)
				fos.close();
		}
		return true;
	}

	private static  boolean nullcheckAndFileExit(String filepath) {

		if (filepath != null && new File(filepath).isFile() && new File(filepath).exists()) {
			return true;
		}
		return false;
	}

	private static  void fileCopy(String sourceFilepath, String destinationFilepath) throws IOException {
		try {
			if (sourceFilepath != null && destinationFilepath != null) {
				Path source = Paths.get(sourceFilepath);
				Path destination = Paths.get(destinationFilepath);
				configPropMap.setProperty("sslKeyStore",destinationFilepath );
				Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			throw e;
		}
	}


}
