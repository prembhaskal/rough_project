package com.polymorph;

import com.other.TapPole;

public class Frog {
	protected void ribbit(){
		System.out.println("Frog ribbit");
	}

	public static void main(String[] args) {
		TapPole t = new TapPole();
//		t.ribbit();// compile error
		Frog f = new TapPole();
		f.ribbit();
	}
}
