package com.roche.connect.dpcr.lis.bl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.roche.connect.dpcr.MainApp;

public class FileGenerator {

	public static void main(String[] a) {
		FileGenerator fc = new FileGenerator();
		String userName = MainApp.getValueFromPropFile("FileShareUserName"), Encpassword = "3E3YydU6tQiLmfbaZ7HIZw==",
				ip = "10.146.128.149", password = MainApp.getValueFromPropFile("FileSharePassword");
		String fileName=MainApp.getValueFromPropFile("FilePath")+MainApp.getValueFromPropFile("MountPath")+"RND19213181832.raw";
		int port = 22;
		System.out.println(fc.createCycleFileAndGetChecksum(ip, userName, Encpassword, port, MainApp.getValueFromPropFile("FilePath"),
				MainApp.getValueFromPropFile("MountPath"), "test.raw", "sarath"));
		//System.out.println(fc.getFileChecksumWindows(fileName, "RND19213181832.raw"));
	}

	/*public void readFile(String userName, String password, String ip, int port) {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		InputStream isStream = null;
		JSch jsch = new JSch();

		try {
			session = jsch.getSession(userName, ip, port);

			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();

			System.out.println("Config done");
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			System.out.println("Config set");

			session.connect();

			System.out.println("Session connected");
			channel = session.openChannel("sftp");
			channel.connect();
			System.out.println("Connection Opened\\n");
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd("/home/rconnect/roche/simulators/mplp/LP-SEQ-PP/testF/testC/testA/");
			isStream = channelSftp.get("test.fastq");

			System.out.println("checksum :" + this.getChecksum(isStream));

		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/

	/*public void writeFile(String userName, String password, String ip, int port) {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		InputStream isStream = null;

		JSch jsch = new JSch();
		try {
			session = jsch.getSession(userName, ip, port);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();

			System.out.println("Config done");
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			System.out.println("Config set");

			session.connect();

			System.out.println("Session connected");
			channel = session.openChannel("sftp");
			channel.connect();

			String folderPath[] = "simulators/mplp/LP-SEQ-PP/testF/testC/testA".split("/");

			System.out.println("Connection Opened\n");
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd("/home/rconnect/roche/");
			for (String path : folderPath) {
				try {
					channelSftp.cd(path);
				} catch (SftpException e) {
					channelSftp.mkdir(path);
					channelSftp.cd(path);
				}
			}
			isStream = new ByteArrayInputStream("SAnthosh, itsworking".getBytes());
			channelSftp.put(isStream, "/home/rconnect/roche/simulators/mplp/LP-SEQ-PP/testF/testC/testA/test.fastq");
			System.out.println("file is created");
			InputStream is = channelSftp
					.get("/home/rconnect/roche/simulators/mplp/LP-SEQ-PP/testF/testC/testA/test.fastq");
			System.out.println("                   " + getChecksum(is));

		} catch (JSchException e) {
			System.out.println("ERROR :" + e.getMessage());
			e.printStackTrace();
		} catch (SftpException e) {
			System.out.println("ERROR :" + e.getMessage());
			e.printStackTrace();
		} finally {
			channelSftp.disconnect();
			channel.disconnect();
			session.disconnect();

		}
	}*/

	public String createCycleFileAndGetChecksum(String ip, String username, String password, int port, String mainPath,
			String tempServerPath, String fileName, String message) {
		String checksum = null;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		InputStream isStream = null;
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(username, ip, port);
			Key aesKey = new SecretKeySpec("P@ssw0rd@f0r2019".getBytes("UTF8"), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(password));
			session.setPassword(new String(decrypted));
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			String folderPath[] = tempServerPath.split("/");

			channelSftp.cd(mainPath);
			for (String path : folderPath) {
				try {
					channelSftp.cd(path);
				} catch (SftpException e) {
					channelSftp.mkdir(path);
					channelSftp.cd(path);
				}
			}
			isStream = new ByteArrayInputStream(message.getBytes());

			channelSftp.put(isStream, mainPath + tempServerPath + fileName);
		} catch (Exception e) {
			System.out.println("Error while creating file in mount :" + e.getMessage());
		}  finally {
			channel.disconnect();
			session.disconnect();
		}
		return mainPath + tempServerPath + fileName;
	}

	public String getFileChecksumWindows(String path, String message) {
		File file = new File(path);
		file.getParentFile().mkdirs();
		try (FileWriter outputFile = new FileWriter(file);) {
			outputFile.write(message);
		} catch (Exception e) {
			System.out.println("Error while creating file in mount :" + e.getMessage());
		}
		return path;
	} 
	/*public String getChecksum(InputStream input) {
		InputStream in = null;
		MessageDigest digest = null;
		byte[] block = new byte[1024];
		int length;
		byte[] output = new byte[1024];
		try {
			in = input;
			digest = MessageDigest.getInstance("SHA1");
			while ((length = in.read(block)) > 0) {
				if (digest != null) {
					digest.update(block, 0, length);
					output = digest.digest();
				} else {
					throw new NullPointerException("Message digest cannnot be null");
				}
			}
		} catch (NoSuchAlgorithmException | IOException e) {
			System.out.println("Error in checkSum method :" + e.getMessage());
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				System.out.println(String.format("IO exception while closing resource...%s", e));
			}
		}
		return javax.xml.bind.DatatypeConverter.printHexBinary(output);

	}
*/
}
