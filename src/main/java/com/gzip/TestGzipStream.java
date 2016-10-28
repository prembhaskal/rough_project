package com.gzip;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class TestGzipStream {

	public void printGZipFileContent(String gzipFileName) {
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = getGZipInputStream(gzipFileName);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

			System.out.println("########## File Content ###########");
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}

			System.out.println("########## End of File ##########");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			closeQuitely(inputStream);
			closeQuitely(bufferedReader);
		}
	}

	private void closeQuitely(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private InputStream getGZipInputStream(String gzipFileName) throws IOException {
		if (gzipFileName == null) {
			throw new IllegalArgumentException("GZip file name is null");
		}

		return new GZIPInputStream(new BufferedInputStream(new FileInputStream(gzipFileName)));
	}
}
