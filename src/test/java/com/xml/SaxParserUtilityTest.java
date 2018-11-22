package com.xml;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class SaxParserUtilityTest {


	@Test
	public void parseCommandTag() throws Exception {
//		File statsFile = new File("d://tmp/test.xml");
		File statsFile = new File("d://tmp/prem_stats.xml");
		File outputFile = new File("d://tmp/testcommand.txt");
		try (InputStream inputStream = new BufferedInputStream(new FileInputStream(statsFile));
			 PrintWriter printWriter = new PrintWriter(outputFile)) {
//			new SaxParserUtility().parseModelFile(new PrintWriter(System.out), inputStream);
			new SaxParserUtility().parseCommandTag(printWriter, inputStream);
		}


	}
}