package com.dch.schedule;

import java.util.concurrent.atomic.AtomicLong;

import javax.management.AttributeChangeNotification;
import javax.management.NotificationBroadcasterSupport;

public class ServerConfigure extends NotificationBroadcasterSupport implements
		ServerConfigureMBean {

	private AtomicLong sequenceNumber = new AtomicLong(1);
	
	private int port;
	
	private String host;
	
	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public void setHost(String host) {
		String oldHost = this.host;
		this.host = host;
		AttributeChangeNotification ano = new AttributeChangeNotification(this,
                sequenceNumber.getAndIncrement(), 
				System.currentTimeMillis(), 
				AttributeChangeNotification.ATTRIBUTE_CHANGE, 
				"Server host change", 
				"java.lang.String", 
				oldHost, 
				this.host);
      super.sendNotification(ano);
	}

	public void setPort(int port) {

		int oldPort = this.port;
		this.port = port;
		AttributeChangeNotification ano = new AttributeChangeNotification(this,
				                                                            sequenceNumber.getAndIncrement(), 
																			System.currentTimeMillis(), 
																			AttributeChangeNotification.ATTRIBUTE_CHANGE, 
																			"Server port change", 
																			"java.lang.Integer", 
																			oldPort+"", 
																			this.port+"");
		super.sendNotification(ano);
	}

}
