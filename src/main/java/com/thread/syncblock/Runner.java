package com.thread.syncblock;

public class Runner {

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(() -> new Test1().runmethodt1("test"));
		Thread t2 = new Thread(() -> new Test2().runmethodt2("test1"));

		t1.start();

		t2.start();

		Thread.sleep(20000);
	}
}
