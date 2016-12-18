package com.alg;

public class MathT {

	// exponentiation by squaring
	public int powMod(long n, long p, int MOD) {
		long res = 1;
		while(p > 0) {
			if ( (p&1)==1) {
				res = (res * n) % MOD;
			}
			n  = (n * n) % MOD;
			p /= 2;
		}

		return (int)res;
	}
}
