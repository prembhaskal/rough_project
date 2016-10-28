package com.gzip;

import org.junit.Test;

public class TestGzipStreamTest {

	@Test
	public void testGZipFileUnCompression() throws Exception {
		String gzipFileName = getClass().getResource("/test_file.txt.gz").toURI().getPath();
		System.out.println("processing the file: " + gzipFileName);

		new TestGzipStream().printGZipFileContent(gzipFileName);

	}
}