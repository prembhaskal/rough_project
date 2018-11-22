package com.thread.syncblock;

public class Test2 {

	public void runmethodt2(String processName) {
		synchronized (processName.intern()) {
			System.out.println("inside run method t2 .... locked on " + processName);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("t2 - work done");
		}
	}
}
