package com.alg;

public class HearthStringOpers {

    public static void main(String[] args) {
        // A(i) = A(i-1) + 2*A(i-2) + 4*A(i-3)  for i > 3
        // use matrix exponentiation (log(n) one) for linear recurrence relation

        long a_i3 = 3;
        long a_i2 = 4;
        long a_i1 = 10;

        int MOD = 1000_000_000 + 7;

        for (int i = 4; i < 30; i++) {
            long a_i = (a_i1 + 2 * a_i2 + 4 * a_i3) % MOD;
            System.out.println("N: " + i + " --> " + a_i);

            a_i3 = a_i2;
            a_i2 = a_i1;
            a_i1 = a_i;
        }

        old1();
    }

    private static void old1() {
        int one_i2 = 3;
        int z_i2 = 2;
        int one_i1 = 4;
        int z_i1 = 3;

        for (int i = 3; i < 30; i++) {
            int one_i = one_i1 + 3 * z_i2;
            int z_i = 2 * one_i1 + 2 * z_i2;

            System.out.println("N : " + i + " one: " + one_i + " zero: " + z_i);
            one_i2 = one_i1;
            one_i1 = one_i;

            z_i2 = z_i1;
            z_i1 = z_i;
        }
    }
}
