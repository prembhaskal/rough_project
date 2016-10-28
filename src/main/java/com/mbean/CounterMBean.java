package com.mbean;

public interface CounterMBean {

	public String sayHello();

	public long getCurrentCount();

	public void setCurrentCount(long count);
}
