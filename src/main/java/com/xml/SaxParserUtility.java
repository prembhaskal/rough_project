package com.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class SaxParserUtility {

	public void parseCommandTag(PrintWriter printWriter, InputStream inputStream) {
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();

			saxParser.parse(inputStream, new CommandTagHandler(printWriter));
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class CommandTagHandler extends DefaultHandler {

		private boolean isInsideCommand = false;
		private PrintWriter printWriter;

		public CommandTagHandler(PrintWriter printWriter) {
			this.printWriter = printWriter;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

			if (qName.equals("command")) {
				isInsideCommand = true;
				printWriter.println("");
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {

			if (!isInsideCommand)
				return;

			String commandValue = new String(ch, start, length);
			printWriter.print(commandValue);
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equals("command")) {
				isInsideCommand = false;
				printWriter.println("");
			}
		}
	}
}
