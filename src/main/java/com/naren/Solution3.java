package com.naren;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class Solution3 {

	public static void main(String[] args) {
		Solution3 solution3 = new Solution3();
		int[] soln = solution3.solution(new int[]{9, 1, 4, 9, 0, 4, 8, 9, 0, 1});
		System.out.println(Arrays.toString(soln));

		soln = solution3.solution(new int[]{0});
		System.out.println(Arrays.toString(soln));
		soln = solution3.solution(new int[]{0, 0, 1, 2, 3, 4});
		System.out.println(Arrays.toString(soln));
	}

	public int[] solution(int[] T) {

		int [] distArray = new int[T.length];

		List<Integer>[] adjList = new ArrayList[T.length];
		Graph graph = new Graph(adjList);

		int capitalCity = -1;
		for (int i = 0; i < T.length; i++) {
			if (T[i] == i) {
				capitalCity = i;
				break;
			}
		}

		// form graph
		for (int i = 0; i < T.length; i++) {
			int cityIdx = i;
			int connected = T[i];

			graph.addNeighbour(cityIdx, connected);
			graph.addNeighbour(connected, cityIdx);
		}

		Queue<Integer> queue = new ArrayDeque<Integer>();

		int dist = 0;
		queue.add(capitalCity);
		distArray[capitalCity] = 0;

		boolean visited[] = new boolean[T.length];
		visited[capitalCity] = true;

		while (!queue.isEmpty()) {
			int city = queue.poll();
			int currentDist = distArray[city];

			// get all neighbours;
			List<Integer> neighbours = adjList[city];
			int newDistance = currentDist + 1;

			// update all neighbours.
			for (Integer neighbourCity : neighbours) {
				if (!visited[neighbourCity]) {
					visited[neighbourCity] = true;
					queue.add(neighbourCity);
					distArray[neighbourCity] = newDistance;
				}
			}
		}

		// for final array
		int[] finalArray = new int[T.length - 1];
		for (int i = 0; i < distArray.length; i++) {
			int cityDist = distArray[i];
			if (cityDist > 0) {
				finalArray[cityDist - 1]++;
			}
		}

		return finalArray;
	}



	class Graph {
		List<Integer>[] adjacencyList;

		public Graph(List<Integer>[] adjacencyList) {
			this.adjacencyList = adjacencyList;
		}


		public void addNeighbour(Integer fromNode, Integer toNode) {
			List<Integer> neighbours = adjacencyList[fromNode];
			if (neighbours == null) {
				neighbours = new ArrayList<Integer>();
				adjacencyList[fromNode] = neighbours;
			}
			neighbours.add(toNode);
		}

	}

}
