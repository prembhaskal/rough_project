package com.naren;

import com.naren.TestAbstract;

public class IndianStylePizza extends TestAbstract {

	public IndianStylePizza() {
		super(false);
	}

	@Override
	protected void finishing() {

	}

	@Override
	protected void bake() {

	}

	@Override
	protected void addItems() {

	}

	@Override
	protected void addBread() {
		// tk
	}

	public static void main(String[] args) {
		TestAbstract testAbstract = new IndianStylePizza();

		testAbstract.makePizza();
	}
}
