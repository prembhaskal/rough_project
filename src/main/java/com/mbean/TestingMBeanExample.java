package com.mbean;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Add below line if JConsole does not show it.
 -Dcom.sun.management.jmxremote.port=3333 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false


 */
public class TestingMBeanExample {

	public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
		Counter counter = new Counter();

		MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
		ObjectName objectName = new ObjectName("com.example:type=TestCounter");
		platformMBeanServer.registerMBean(counter, objectName);

		System.out.println("counting the values");

		for (long i = 0; i < Long.MAX_VALUE; i++) {
			try {
				if (i % 500 == 0) {
					counter.setCurrentCount(i);
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
