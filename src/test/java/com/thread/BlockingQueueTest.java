package com.thread;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.*;

public class BlockingQueueTest {

	private BlockingQueue<Integer> blockingQueue;

	private Queue<Integer> queue;

	private final int SIZE = 100000;

	@Before
	public void setUp() {
		queue = new LinkedList<>();

		for (int i = 0; i < SIZE; i++) {
			queue.add(i);
		}

		blockingQueue = new BlockingQueue<>(queue);
	}

	@Test
	public void testPullBlocksAfterEmptying() throws Exception {

		Runnable test = () -> {
			for (int i = 0; i < SIZE; i++) {
				try {
					blockingQueue.pull();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		Thread thread = new Thread(test);
		thread.setDaemon(true);
		thread.start();

		Thread.currentThread().sleep(5*1000);

		assertEquals(thread.getState(), Thread.State.WAITING);

		blockingQueue.push(5);

		Thread.currentThread().sleep(5*1000);

		assertEquals(thread.getState(), Thread.State.TERMINATED);

		assertEquals(queue.size(), 0);
	}


	// this might fail (Depends on SIZE and your luck) if BlockingQueue is not thread safe.
	@Test
	public void testRetrieveIn2Threads() throws Exception {
		List<Integer> collector1 = new ArrayList<>();
		List<Integer> collector2 = new ArrayList<>();
		Runnable runnable1 = getRunnableCollector(collector1);
		Runnable runnable2 = getRunnableCollector(collector2);
		Thread thread1 = new Thread(runnable1);
		thread1.setDaemon(true);
		Thread thread2 = new Thread(runnable2);
		thread2.setDaemon(true);

		thread1.start();
		thread2.start();

		Thread.currentThread().sleep(5 * 1000);

		// check each collector got a unique set
		Set<Integer> set1 = new HashSet<>(collector1);
		Set<Integer> set2 = new HashSet<>(collector2);

		assertEquals(set1.size(), SIZE/2);
		assertEquals(set2.size(), SIZE/2);

		for (Integer num : set1) {
			assertFalse(set2.contains(num));
		}

		for (Integer num : set2) {
			assertFalse(set1.contains(num));
		}


		assertEquals(queue.size(), 0);


	}

	private Runnable getRunnableCollector(List<Integer> collectedInts) {
		Runnable retrieveThreads = () -> {
			for (int i = 0; i < SIZE/2; i++) {
				try {
					collectedInts.add(blockingQueue.pull());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		return retrieveThreads;
	}
}