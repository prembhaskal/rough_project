package com.classloader.changed;

import com.classloader.PrintString;

public class ConsolePrintString implements PrintString {

	@Override
	public void print(String text) {
		printClassInformation();
		System.out.println(": " + text + " :");
	}

	private void printClassInformation() {
		System.out.println("ConsolePrintString version 1");
	}
}
