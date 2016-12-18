package com.alg;

import org.junit.Test;

public class MathTTest {

	@Test
	public void TestPow() throws Exception {
		int num = 456_930_394;
		long power = 493_039_654_000_000l;

		int rem = 43938243;

		int ans = new MathT().powMod(num, power, rem);
		System.out.println(ans);

		int clue = 78798284 - ans;

		System.out.println("clue: " + clue);
		System.out.println("get: " + (ans + 55075411));

		// remainder((456930394 ^ 493039654000000) / 43938243) + 55075411


		char[] name = "NORTH".toCharArray();

		for (int i = 0; i < 5; i++) {
			System.out.println((int)name[i]);
		}
	}
}