package com.file;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestDeleteFilesMemoryConsumption {

	public static void main(String[] args) throws ParseException {
		File testDirectory = new File("D:/lteamed");
		System.out.println("testing " + testDirectory.getAbsolutePath());

		new TestDeleteFilesMemoryConsumption().testGetOlderDates();
	}

	private static void doSimpleTraverse(File testDirectory) {
		try {
			Thread.currentThread().sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Started at " + new Date());

		new TestDeleteFilesMemoryConsumption().simpleRecurseTraversing(testDirectory);

		System.out.println("Completed at " + new Date());

		try {
			Thread.currentThread().sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void traverseFiles(File testDirectory) {
		try {
			Thread.currentThread().sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("acquiring some memory");

		String[] memory = new String[10 * 1000_000];
		for (int i = 0; i < memory.length; i++) {
			memory[i] = "memory " + i;
		}

		for (int i = 0; i < 10; i++) {
			System.out.println("iteration number: " + i);

			System.out.println("started at " + new Date());

			int total = new TestDeleteFilesMemoryConsumption().traverseDirectory(testDirectory);
			System.out.println("total files are " + total);

			System.out.println("Completed at " + new Date());

			try {
				Thread.currentThread().sleep(1000);
			} catch (Exception e) {
			}

		}
	}

	public int traverseDirectory(File directory) {
		int i = 0;
		if (!directory.isDirectory()) {
			return 0;
		}

		File[] files = directory.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				i = i + traverseDirectory(file);
			}
			else {
				long num = file.lastModified();
				// do nothing, we want to see only how memory behaves
				num += 2;
				i++;

				String logDebug = "File deleted: " + file + " from Compressed folder older than: " +  file.lastModified();
				if (logDebug != null) {
					num += 1;
				}
			}
		}
		return i;
	}

	public int simpleRecurseTraversing(File directory) {
		int i = 0;
		if (!directory.isDirectory()) {
			return 0;
		}

		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				i += simpleRecurseTraversing(file);
			}
			else {
				i++;
			}
		}

		return i;

	}


	public void createTestFiles(File directory) {

		int hours = 72;
		int enodebs = 500;
		int filenums = 40;

		for (int hr = 1; hr <= hours; hr++) {
			System.out.println("started with hour " + hr);
			File hrDir = new File(directory, hr + "");
			hrDir.mkdir();

			for (int node = 1; node <= enodebs; node++) {
				File nodeDir = new File(hrDir, node + "");
				nodeDir.mkdir();

				for (int fl = 1; fl <= filenums; fl++) {
					File newFile = new File(nodeDir, fl + "");
					try {
						newFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException("Error creating file " + newFile.getAbsolutePath());
					}
				}

			}
		}
	}

	public void testGetOlderDates() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

		Calendar testDate = Calendar.getInstance();

		System.out.println("before change: " + simpleDateFormat.format(testDate.getTime()));

		int _3daysBack = (int) TimeUnit.DAYS.toMillis(3);

		testDate.add(Calendar.MILLISECOND, -1 * _3daysBack);
		System.out.println("after change: " + testDate.getTime());
		// day boundary correction
		testDate.add(Calendar.DAY_OF_MONTH, 1);
		testDate.set(Calendar.HOUR_OF_DAY, 0);
		testDate.set(Calendar.MINUTE, 0);
		testDate.set(Calendar.SECOND, 0);
		System.out.println("after day correction : " + testDate.getTime());

		testDate.add(Calendar.DAY_OF_MONTH, -1);
		System.out.println("after change: " + simpleDateFormat.format(testDate.getTime()));

		System.out.println(simpleDateFormat.format(new Date()));

		// check the directories.
		System.out.println("SCANNED directory 20161118 " + scanThisDirectory(_3daysBack, "20161118"));
		System.out.println("SCANNED directory 20161117 " + scanThisDirectory(_3daysBack, "20161117"));
		System.out.println("SCANNED directory 20161116 " + scanThisDirectory(_3daysBack, "20161116"));
		System.out.println("SCANNED directory 20161115 " + scanThisDirectory(_3daysBack, "20161115"));
		System.out.println("SCANNED directory 20161114 " + scanThisDirectory(_3daysBack, "20161114"));
		System.out.println("SCANNED directory 20161113 " + scanThisDirectory(_3daysBack, "20161113"));
	}

	private boolean scanThisDirectory(int milliSecs, String directoryName) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

		Calendar thresholdDay = Calendar.getInstance();
		// to start of 3 days back + next day start.
		thresholdDay.add(Calendar.MILLISECOND, -1 * milliSecs);
		thresholdDay.add(Calendar.DAY_OF_MONTH, 1);
		thresholdDay.set(Calendar.HOUR_OF_DAY, 0);
		thresholdDay.set(Calendar.MINUTE, 0);
		thresholdDay.set(Calendar.SECOND, 0);
		thresholdDay.set(Calendar.MILLISECOND, 0);

		System.out.println("Threshold day is " + thresholdDay.getTime());

		Date directoryDate = simpleDateFormat.parse(directoryName);

		// don't scan anything equal or after the threshold day.
		return directoryDate.before(thresholdDay.getTime());
	}
}
