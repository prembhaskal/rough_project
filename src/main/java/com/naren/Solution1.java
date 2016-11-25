package com.naren;

public class Solution1 {
	public static void main(String[] s) {
		// eg1
		Solution1 solution = new Solution1();
		System.out.println(solution.solution("10:00", "13:21"));
		System.out.println(solution.solution("09:42", "11:42"));
		System.out.println(solution.solution("11:42", "11:42"));
		System.out.println(solution.solution("10:43", "11:42"));

		System.out.println(solution.solutionnaren("10:00", "13:21"));
		System.out.println(solution.solutionnaren("09:42", "11:42"));
		System.out.println(solution.solutionnaren("11:42", "11:42"));
		System.out.println(solution.solutionnaren("10:43", "11:42"));
	}


	public int solutionnaren(String E, String L) {
		String[] e1 = E.split(":");
		String[] l1 = L.split(":");

		int hours = Integer.valueOf(l1[0]) - Integer.valueOf(e1[0]);
		int minutes = Integer.valueOf(l1[1]) - Integer.valueOf(e1[1]);

		int entryfee = 2;
		int firsthourorminute = 3;
		int successivehours = 4;

		int totalfare = 0;

		if (hours < 0) {
			hours = hours + 24;
		}

		if (minutes < 0) {
			hours = hours - 1;
			minutes = minutes + 60;
		}

		if (hours < 1 && minutes > 0) {
			totalfare = entryfee + firsthourorminute;
		}

		if (hours >= 1 && minutes > 0) {
			totalfare = entryfee + firsthourorminute + 4 * (hours);
		}

		if (hours >= 1 && minutes == 0) {
			totalfare = entryfee + firsthourorminute + 4 * (hours - 1);
		}

		return totalfare;
	}

	public int solution(String E, String L) {
		String[] eArray = E.split(":");

		int eToMinutes = Integer.parseInt(eArray[0]) * 60 + Integer.parseInt(eArray[1]);

		String[] lArray = L.split(":");
		int lToMinutes =  Integer.parseInt(lArray[0]) * 60 + Integer.parseInt(lArray[1]);

		int diff = lToMinutes - eToMinutes;

		int cost = 2; // initial
		if (diff == 0) {
			return cost;
		}

		diff = diff - 60; // 1st hour
		cost = cost + 3;

		if (diff <= 0) return cost;

		int remainHours = diff / 60;
		cost = cost + remainHours * 4;

		int remaining = diff % 60;
		if (remaining > 0) {
			cost = cost + 4;
		}

		return cost;
	}

}
