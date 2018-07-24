package com.dch.schedule;

import javax.management.Notification;
import javax.management.NotificationListener;

public class ServerConfigNotificationListener implements NotificationListener {

	public void handleNotification(Notification notification, Object handback) {
		// TODO Auto-generated method stub

		log("SequencNumber:"+notification.getSequenceNumber());
		log("type:"+notification.getType());
		log("message:"+notification.getMessage());
		log("source:"+notification.getSource());
		log("timestamp:"+notification.getTimeStamp());
	}

	private void log(String string) {
		System.out.println(string);
	}
}
