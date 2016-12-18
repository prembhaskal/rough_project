package com.naren.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TaskExe1 {


	public static void main(String[] args) throws ExecutionException, InterruptedException {

		ExecutorService executor = Executors.newFixedThreadPool(3);

		List<Future<?>> tasks = new ArrayList<>();

		for (int i = 1; i < 2000; i+= 100) {
			Count count = new Count();
			count.start = i;
			count.end = i + 100;
			Future<?> task = executor.submit(count);
			tasks.add(task);
		}

		executor.shutdown();

		for (Future<?> task : tasks) {
			task.get();
		}

		System.out.println("all done.");
	}


	private static class Count implements Runnable {
		public int start;
		public int end;

		@Override
		public void run() {
			for (int i = start; i < end; i++) {
//				System.out.println("counting :" + i);
				try {
					TimeUnit.SECONDS.sleep(60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
