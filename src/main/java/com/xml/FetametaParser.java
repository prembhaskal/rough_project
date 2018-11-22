package com.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

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

        private String cntDescription;
        private StringBuilder descBuilder = new StringBuilder();

        private boolean isMeasurement = false;
        private boolean isCounter = false;
        private boolean isDescription = false;


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
                        isMeasurement = true;
                        captureMeasurementAttributes(attr);
                        break;

                    case "Description":
                        isDescription = true;
                        break;

                    case "Counter":
                        isCounter = true;
                        break;

                    default:
                        break;
                }
            }
        }

        private void collectDescription(String description) {
            descBuilder.append(description);
        }

        private void captureDescription() {
            String currentDescription = descBuilder.toString().trim();

            if (isMeasurement) {
                if (isCounter) {
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
            isMeasurement = true;
            measID = attr.getValue("ID");
            measOmesName = attr.getValue("OMeSName");
            measNeName = attr.getValue("NEName");
            measRBFolderName = attr.getValue("RBFolderName");
            printWriter.println(String.format("%s %s %s %s", measID, measOmesName, measRBFolderName, measNeName));
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {

//            if (!isInsideCommand)
//                return;

            if (isDescription) {
                String description = new String(ch, start, length);
//                System.out.println(String.format("%d -- %s", tagCounter, description));
                collectDescription(description);
            }
//            printWriter.print(commandValue);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName != null) {
                switch (qName) {
                    case "Measurement":
                        isMeasurement = false;
                        break;

                    case "Counter":
                        isCounter = false;
                        break;

                    case "Description":
                        captureDescription();
                        isDescription = false;
                        break;

                    default:
                        break;

                }
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
