package com.mbean;

/**
 * Name matters..its interface should of name "SelfName" + MBean.
 */
public class Counter implements CounterMBean {

	private long counter = 0;

	@Override
	public String sayHello() {
		return "Hello";
	}

	@Override
	public long getCurrentCount() {
		return counter;
	}

	@Override
	public void setCurrentCount(long count) {
		this.counter = count;
	}
}
