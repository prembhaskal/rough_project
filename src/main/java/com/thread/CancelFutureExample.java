package com.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CancelFutureExample {

	public static void main(String[] args) {
		new CancelFutureExample().test();
	}

	public void test() {
		System.out.println("testing the future tasks cancellation.");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<?> taskFuture = executorService.submit(new UnEndingTask());

		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.schedule(() -> {
			System.out.println("cancelling the future task now.");
			boolean isCancelled = taskFuture.cancel(true);
			System.out.println("Was task cancelled: " + isCancelled);
		}, 10000, TimeUnit.MILLISECONDS);

		executorService.shutdown();
		scheduledExecutorService.shutdown();

		System.out.println("done testing.");
	}

	private class UnEndingTask implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					int i = 0;
					i = i + 1;

					if (Thread.currentThread().isInterrupted()) { // seems we need to explicitly check this, the while does not stop on Future.cancel on task cancel otherwise.
						System.out.println("This thread was interrupted.");
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
