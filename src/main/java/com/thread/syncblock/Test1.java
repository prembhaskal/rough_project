package com.thread.syncblock;

public class Test1 {

	public void runmethodt1(String processName) {
		synchronized (processName.intern()) {
			System.out.println("inside run method t1 .... locked on " + processName);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("t1 - work done");
		}
	}
}
