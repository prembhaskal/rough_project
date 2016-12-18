package com.naren.executor;

import java.util.concurrent.TimeUnit;

public class OddEvenPrinter {


	public static void main(String[] args) throws InterruptedException {

		Object mutex = new Object();
		CurrentNumber currentNumber = new CurrentNumber();
		Thread th1 = new Thread(new EvenPrinter(mutex, currentNumber));
		Thread th2 = new Thread(new OddPrinter(mutex, currentNumber));

		th1.start();
		th2.start();

		TimeUnit.SECONDS.sleep(1);
	}

	private static class EvenPrinter implements Runnable{
		private Object mutex;
		private CurrentNumber currentNumber;

		public EvenPrinter(Object mutex, CurrentNumber currentNumber) {
			this.mutex = mutex;
			this.currentNumber = currentNumber;
		}

		@Override
		public void run() {

			try {
				while (true) {
					if (currentNumber.current > 100)
						break;

					synchronized (mutex) {
						while (currentNumber.current % 2 == 0) {
							mutex.wait();
						}

						System.out.println(currentNumber.current);
						currentNumber.current++;

						mutex.notifyAll();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	private static class OddPrinter implements Runnable{
		private Object mutex;
		private CurrentNumber currentNumber;

		public OddPrinter(Object mutex, CurrentNumber currentNumber) {
			this.mutex = mutex;
			this.currentNumber = currentNumber;
		}

		@Override
		public void run() {

			try {
				while (true) {
					if (currentNumber.current > 100)
						break;

					synchronized (mutex) {
						while (currentNumber.current % 2 == 1) {
							mutex.wait();
						}

						System.out.println(currentNumber.current);
						currentNumber.current++;

						mutex.notifyAll();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	private static class CurrentNumber {
		int current;

	}
}
