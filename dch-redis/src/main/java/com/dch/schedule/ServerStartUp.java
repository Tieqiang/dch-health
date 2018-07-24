package com.dch.schedule;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;


public class ServerStartUp {

	/**
	 * @param args
	 * @throws NullPointerException 
	 * @throws MalformedObjectNameException 
	 * @throws Exception 
	 * @throws MBeanRegistrationException 
	 * @throws InstanceAlreadyExistsException 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//创建MBeanServer
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		//新建bean ObjectName, 在MBeanServer里标识注册的MBean
		ObjectName name = new ObjectName("com.chillax.schedual:type=ServerConfigure");
		//创建Bean
		final ServerConfigure sc = new ServerConfigure();
		mbs.registerMBean(sc, name);
		//自定义观察者
		ServerConfigNotificationListener snl = new ServerConfigNotificationListener();
		mbs.addNotificationListener(name, snl, null, null);
		new Thread(new Runnable(){
			public void run() {
				try {
					Thread.sleep(6000);
					sc.setHost("skq");
					System.out.println("改变了");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();
		System.out.println("来了==");
		Thread.sleep(Long.MAX_VALUE);
	}

}
