package com.naren;

public class Solution2 {

	public static void main(String[] args) {
		Solution2 solution2 = new Solution2();
		long start = System.currentTimeMillis();
		System.out.println(solution2.solution(new int[]{1, 2, 4, 5, 7, 29, 30}));
		System.out.println(solution2.solution(new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30}));
		System.out.println(solution2.solution(new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,22,23,24,25,26,27,28}));
		System.out.println(solution2.solution(new int[]{1,2}));
		long end = System.currentTimeMillis();

		System.out.println("time elapsed " + (end - start)/ 1000);
	}

	public int solution(int[] A) {
		int value = findSol(A, 0);

		return Math.min(25, value);
	}


	private int findSol(int[] A, int indexOfDay) {
		if (indexOfDay > A.length - 1 || indexOfDay < 0)
			return 0;

		// get this day pass
		int day_rest = 2 + findSol(A, indexOfDay + 1);

		// if week starting from now.
		int currentDay = A[indexOfDay];
		int afterThisWeek = currentDay + 7;

		int nextIndex = getNextIndex(indexOfDay, A, afterThisWeek);

		int week_rest = 7 + findSol(A, nextIndex);

		return Math.min(day_rest, week_rest);
	}

	private int getNextIndex(int startFrom, int[] A, int matchWith) {
		for (int i = startFrom; i < A.length; i++) {
			if (A[i] >= matchWith) return i;
		}
		return -1;
	}
}
