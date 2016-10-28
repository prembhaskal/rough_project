package com.filelock;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Properties;
import java.util.RandomAccess;

public class TestRandomAccess {

	public static void main(String[] args) throws IOException {

		RandomAccessFile randomAccessFile = null;
		try {
			File file = new File(TestRandomAccess.class.getResource("/random_access_test.properties").getPath());

			Properties properties = new Properties();
			properties.load(new FileInputStream(file));

			System.out.println(" printing properties: " + properties);

			randomAccessFile = new RandomAccessFile(file, "rw");
			FileChannel channel = randomAccessFile.getChannel();

			channel.lock();

//			// read file
			InputStream randomAccessInputStream = Channels.newInputStream(channel);
			properties.load(randomAccessInputStream);
//			randomAccessFile.seek(0);

			System.out.println("after getting lock  reading property file: " + properties);

			System.out.println("adding one more property");
			properties.clear();
			properties.put("FOUR", "4");


			channel.truncate(0);
			OutputStream randomAccessOutputStream = Channels.newOutputStream(channel);
			properties.store(randomAccessOutputStream, "");
			randomAccessOutputStream.flush();

			System.out.println("after getting lock . writing property file: " + properties);

			FileLock fileLock = channel.tryLock();
			fileLock.toString();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (randomAccessFile != null) {
				randomAccessFile.close();
			}
		}
	}
}
