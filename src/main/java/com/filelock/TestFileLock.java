package com.filelock;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;

public class TestFileLock {
	private static CountDownLatch countDownLatch;
	private static CountDownLatch latch1;
	private static CountDownLatch latch2;

	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
//		String testLockFileName = TestFileLock.class.getResource("/test_lock.txt").toURI().getPath();
		if (args!= null && args.length < 2) {
			throw new RuntimeException("give 2 file names");
		}

		String fileName1 = args[0];
		String fileName2 = args[1];

		new Thread(() -> {
			new TestFileLock().acquireLockAndWait(fileName1);
		}).start();


		Thread.sleep(10000);

		new Thread(() -> {
			new TestFileLock().acquireLockAndWait(fileName2);
		}).start();

		Thread.currentThread().join();
	}

	public void acquireLockAndWait(String fileName) {
		try {
			//		countDownLatch.countDown();
			RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
			FileChannel channel = raf.getChannel();
//		latch1.countDown();

			channel.lock();

			System.out.println("acquired lock on file : " + fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

			String len;
			while ((len = reader.readLine()) != null) {
				System.out.println(len);
			}

			Thread.currentThread().join();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
