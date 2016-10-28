package com.other;

import com.polymorph.Frog;

public class TapPole extends Frog {

	protected void ribbit(){
		System.out.println("Tadpole ribbit");
	}
	public static void main(String[] args) {
		TapPole t = new TapPole();
		t.ribbit();
		Frog f = new TapPole();
//		f.ribbit();  // line with Compile error ?

		Frog rf = new Frog();
	}

}
