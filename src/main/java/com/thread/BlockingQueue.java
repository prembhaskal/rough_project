package com.thread;

import java.util.Queue;

public class BlockingQueue<E> {

	private final Queue<E> queue;

	private Object queueSizeMutex = new Object();
	private Object addMutex = new Object();

	public BlockingQueue(Queue<E> queue) {
		this.queue = queue;
	}

	public void push(E e) {
		synchronized (queueSizeMutex) {
			queue.add(e);
			queueSizeMutex.notifyAll();
		}
	}

	public E pull() throws InterruptedException {
		synchronized (queueSizeMutex) {
			while (queue.size() == 0) {
					queueSizeMutex.wait();
			}
			return queue.poll();
		}
	}
}
