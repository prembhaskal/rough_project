package com.other;

public class CleanString {

	public static void main(String[] args) {
		String text = "aaaaaaac";
		String clean = new CleanString().removeAdjDuplicateChars(text.toCharArray(), 0);
		System.out.println(clean);
	}

	public String removeAdjDuplicateChars(char[] chars, int idx) {
		if (idx >= chars.length) return "";

		if (idx == chars.length - 1) return chars[idx] + "";

		String diffChar = chars[idx] != chars[idx + 1] ? chars[idx] + "" : "";
		return diffChar + removeAdjDuplicateChars(chars, idx + 1);
	}
}
