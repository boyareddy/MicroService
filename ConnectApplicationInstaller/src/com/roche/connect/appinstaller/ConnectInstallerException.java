package com.roche.connect.appinstaller;

public class ConnectInstallerException  extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ConnectInstallerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConnectInstallerException(String message) {
		super(message);
	}

}
