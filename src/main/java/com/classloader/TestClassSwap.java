package com.classloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class TestClassSwap {

	public static void main(String[] args) {
		new TestClassSwap().testClassLoaderSwap();
	}

	public void testClassLoaderSwap() {
		while (true) {
			try {

				System.out.println("Waiting for class reload...press enter to continue. type x to stop");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String readLine = reader.readLine();
				if (readLine != null && readLine.equals("x")) {
					break;
				}

				loadClassAndPrint();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadClassAndPrint() throws MalformedURLException {
//		URL url = new File("D:\\tmp\\").toURI().toURL();
		URL url = this.getClass().getResource("/");
		System.out.println(url);

		URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url}, null);

//		URL classUrl = urlClassLoader.findResource("ConsolePrintString.class");
//		System.out.println(classUrl);

		try {
			Class<?> printStringClass = urlClassLoader.loadClass("com.classloader.changed.ConsolePrintString");
			printClassLoaderInfo(printStringClass.getClassLoader());
			Object object = printStringClass.newInstance();

			Method method = printStringClass.getDeclaredMethod("print", String.class);
			method.invoke(object, "test");

//			printStringClass.getMethod("print").invoke(object, "test");
//			PrintString printString = (PrintString) printStringClass.newInstance();
//			printString.print("test message");
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void printClassLoaderInfo(ClassLoader classLoader) {
		while (classLoader != null) {
			System.out.println(classLoader.getClass().getName());
			classLoader = classLoader.getParent();
		}
	}
}
