package com.naren;

import java.util.Arrays;

public final class StringClass {

	private final char[] chars;

	public StringClass(char[] chars) {
		this.chars = Arrays.copyOf(chars, chars.length);
	}

//	String prem = "prem"; // new String(new char[] {'p', 'r', 'e', 'm'};
}
