package com.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class CleanUpUsingFileVisitor {


	public static void main(String[] args) {

		File testDirectory = new File("D:/lteamed");

		try {
			Thread.currentThread().sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Started at " + new Date());

		new CleanUpUsingFileVisitor().walkTreeUsingVisitor(testDirectory);

		System.out.println("Completed at " + new Date());

		try {
			Thread.currentThread().sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void walkTreeUsingVisitor(File directory) {

		CounterVisitor<Path> counterVisitor = new CounterVisitor<>();

		try {
			Files.walkFileTree(directory.toPath(), counterVisitor);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("total files in " + directory.getAbsolutePath() + " are " + counterVisitor.totalFiles);
	}


	private class CounterVisitor<Path> extends SimpleFileVisitor<Path> {

		int totalFiles = 0;

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			totalFiles++;
			return FileVisitResult.CONTINUE;
		}

	}
}
