package com.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class FetametaParser {

    public void parseModelFile(PrintWriter printWriter, InputStream inputStream) {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();

            saxParser.parse(inputStream, new KoalaFileHandler(printWriter));
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private class KoalaFileHandler extends DefaultHandler {

        private PrintWriter printWriter;

        private String measID;
        private String measOmesName;
        private String measNeName;
        private String measRBFolderName;
        private String measDescription;

        private String mTimeFirstLvl;
        private String mTimeLastLvl;

        private String mTopFirstRef;
        private String mTopLastRef;
        private String mTopRawRef;


        private String cntDescription;
        private StringBuilder descBuilder = new StringBuilder();

        private boolean isMeasurement = false;
        private boolean isCounter = false;
        private boolean isDescription = false;
        private boolean isTime = false;

        private Deque<String> stack = new ArrayDeque<>();


        private int tagCounter = 0;

        public KoalaFileHandler(PrintWriter printWriter) {
            this.printWriter = printWriter;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
            if (qName != null) {
                tagCounter++;
                switch (qName) {
                    case "Measurement":
                        captureMeasurementAttributes(attr);
                        break;
                    default:
                        break;
                }

                stack.push(qName);
            }
        }

        private boolean isInsideTag(String tagName) {
            return stack.contains(tagName);
        }

        private boolean isMeasurement() {
            return isInsideTag("Measurement");
        }

        private boolean isCounter() {
            return isInsideTag("Counter");
        }

        private boolean isDescription() {
            return isInsideTag("Description");
        }

        private boolean isTime() {
            return isInsideTag("Time");
        }
        private boolean isRawLevel() {
            return isInsideTag("RawLevel");
        }
        private boolean isFirstLevel() {
            return isInsideTag("FirstLevel");
        }
        private boolean isLastLevel() {
            return isInsideTag("LastLevel");
        }

        private boolean isRawLevelRef() {
            return isInsideTag("RawLevelRef");
        }
        private boolean isFirstLevelRef() {
            return isInsideTag("FirstLevelRef");
        }
        private boolean isLastLevelRef() {
            return isInsideTag("LastLevelRef");
        }


        private void collectDescription(String description) {
            descBuilder.append(description);
        }

        private void captureDescription() {
            String currentDescription = descBuilder.toString().trim();

            if (isMeasurement()) {
                if (isCounter()) {
                    cntDescription = currentDescription;
                    System.out.println("counter description  -- " + cntDescription);
                }
                else {
                    measDescription = currentDescription;
                    System.out.println("measurement description  -- " + measDescription);
                }
            }
            else {
                System.err.println("Unknown description for current tag.");
            }

            descBuilder.setLength(0);
        }

        private void captureMeasurementAttributes(Attributes attr) {
            measID = attr.getValue("ID");
            measOmesName = attr.getValue("OMeSName");
            measNeName = attr.getValue("NEName");
            measRBFolderName = attr.getValue("RBFolderName");
            printWriter.println(String.format("%s %s %s %s", measID, measOmesName, measRBFolderName, measNeName));
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (isDescription()) {
                String description = new String(ch, start, length);
//                System.out.println(String.format("%d -- %s", tagCounter, description));
                collectDescription(description);
            }
            else if (isTime) {

            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName != null) {
                switch (qName) {
                    case "Description":
                        captureDescription();
                        break;
                    default:
                        break;
                }

                String tag = stack.pop();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        File statsFile = new File("d://tmp/glsp/adglsp.model");
        File outputFile = new File("d://tmp/testcommand.txt");
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(statsFile));
             PrintWriter printWriter = new PrintWriter(outputFile)) {
//			new SaxParserUtility().parseModelFile(new PrintWriter(System.out), inputStream);
            new FetametaParser().parseModelFile(printWriter, inputStream);
        }
    }
}
