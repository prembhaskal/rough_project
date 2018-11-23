package com.xml;

import com.xml.koala.model.Adaptation;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class KoalaJaxbParser {

    // TODO -- use this instead https://stackoverflow.com/questions/8626153/make-jaxb-go-faster/8626388#8626388

    public static void main(String[] args) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        Adaptation adaptation = parseKoala();
        System.out.println("done");
    }

    private static Adaptation parseKoala() throws IOException, JAXBException, ParserConfigurationException, SAXException {
        JAXBContext jaxbContext = null;
        BufferedInputStream inputStream = null;

        try {
            jaxbContext = JAXBContext.newInstance(Adaptation.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();

            inputStream = new BufferedInputStream(new FileInputStream("d://tmp/glsp/pmcbisKOALA.xml"));
            InputSource inputSource = new InputSource(inputStream);
            SAXSource saxSource = new SAXSource(xmlReader, inputSource);

            JAXBElement<Adaptation> unmarshal = unmarshaller.unmarshal(saxSource, Adaptation.class);
            return unmarshal.getValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
