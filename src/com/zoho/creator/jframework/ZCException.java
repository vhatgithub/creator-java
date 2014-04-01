package com.zoho.creator.jframework;

public class ZCException extends Exception {

	public static final int NETWORK_ERROR = 1;
	public static final int GENERAL_ERROR = 2;
	public static final int LICENCE_ERROR = 3;
	public static final int ACCESS_ERROR = 4;
	public static final int ERROR_OCCURED = 5;
	public static final int ACCESSED_COMPONENTS_ERROR = 6;
	private int type = ZCException.GENERAL_ERROR;
	private String logMessage = null;

	public ZCException(String message,int type) {
		this(message, type, null);
	}

	public ZCException(String message,int type, String logMessage) {
		super(message);
		if(ZOHOCreator.getAccessedComponents() && (type == GENERAL_ERROR || type == ERROR_OCCURED)){
			this.type = ACCESSED_COMPONENTS_ERROR;
		}
		else{
			this.type = type;
		}

		this.logMessage = logMessage;
	}

	public int getType(){
		return type;
	}

	public String getLogMessage() {
		return logMessage;
	}

}
