package com.naren;

public abstract class TestAbstract {

	private final boolean needFinishing;

	public TestAbstract(boolean needFinishing) {
		this.needFinishing = needFinishing;
	}
	
	public final void makePizza() {
		// define way... CANNOT be changed.
		addBread();
		addItems();
		bake();
		if (needFinishing) {
			finishing();
		}
	}
	
	protected abstract void finishing();
	
	protected abstract void bake();
	
	protected abstract void addItems();
	
	protected abstract void addBread();
}
