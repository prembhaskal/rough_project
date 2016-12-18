package com.naren;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bank {

	private Map<String, BankAccount> bankAccountMap = new HashMap<>();

	public Bank() {
		// init
		BankAccount narenAccount = new BankAccount();
		narenAccount.money = 1000;
		bankAccountMap.put("naren", narenAccount);
	}

	public void addMoneyToAccount(String accountNumber, int money) {
		BankAccount bankAccount = bankAccountMap.get(accountNumber);
		synchronized (bankAccount) {
			// add
		}
	}

	public void withDrawMoney(String accountNumber, int money) {
		BankAccount bankAccount = bankAccountMap.get(accountNumber);
		synchronized (bankAccount) {
			// withdraw

		}

	}


	private class BankAccount {

		int money;
		private void setmoney(int money) {

		}

		private int getMoney() {
			return money;
		}
	}

	public static void main(String[] args) {

		ExecutorService executorService = Executors.newFixedThreadPool(5);

		Bank bank = new Bank();

		for (int i = 0; i < 10; i++) {
			executorService.submit(new Add(bank, "naren", 100));
			executorService.submit(new Withdraw(bank, "naren", 50));
		}


	}

	private static class Add implements Runnable {
		private final Bank bank;
		private final String accountNumber;
		private final int money;

		Add(Bank bank, String accountNumber, int money) {
			this.bank = bank;
			this.accountNumber = accountNumber;
			this.money = money;
		}


		@Override
		public void run() {
			bank.addMoneyToAccount(accountNumber, money);
		}
	}

	private static class Withdraw implements Runnable {
		private final Bank bank;
		private final String accountNumber;
		private final int money;

		Withdraw(Bank bank, String accountNumber, int money) {
			this.bank = bank;
			this.accountNumber = accountNumber;
			this.money = money;
		}


		@Override
		public void run() {
			bank.withDrawMoney(accountNumber, money);
		}
	}
}
