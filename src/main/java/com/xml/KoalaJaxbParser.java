package com.xml;

import com.xml.feta.model.*;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KoalaJaxbParser {

    // TODO -- use this instead https://stackoverflow.com/questions/8626153/make-jaxb-go-faster/8626388#8626388

    public static void main(String[] args) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        Adaptation adaptation = parseKoala();
        System.out.println("done");
        List<com.xml.feta.model.Adaptation> adaptations = convertToFeta(adaptation);


        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(com.xml.feta.model.Adaptation.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());

            File file = new File("d:/tmp/glsp/test.xml");
            marshaller.marshal(adaptations.get(0), file);
        }
        catch (JAXBException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static List<com.xml.feta.model.Adaptation> convertToFeta(Adaptation adaptation) {

        List<com.xml.feta.model.Adaptation> fetaAdaptationList = new ArrayList<>();

        List<Adaptation.Measurement> koalaMeasList = adaptation.getMeasurement();
        for (Adaptation.Measurement koalaMeas : koalaMeasList) {
            com.xml.feta.model.Adaptation fetaAdap = new com.xml.feta.model.Adaptation();
            fetaAdap.setMeasurementNumber(koalaMeas.getID());
            fetaAdap.setObjectHierarchy(koalaMeas.getTopologyRef().getLastLevelRef());
            fetaAdap.setRBFolderName(koalaMeas.getRBFolderName());
            fetaAdap.setPMCoreMeasType(koalaMeas.getOMeSName());
            fetaAdap.setOMeSName(koalaMeas.getOMeSName());
            fetaAdap.setBusyHourDefinitionFormula("");
            fetaAdap.setTimeSummaryLevels(koalaMeas.getTime().getFirstLevel() + "," + koalaMeas.getTime().getLastLevel());
            fetaAdap.setNWSummaryLevels(koalaMeas.getTopologyRef().getLastLevelRef());

            AIBDetails aibDetails = new AIBDetails();
            aibDetails.setMeasurementName(koalaMeas.getID());
            aibDetails.setDescription(koalaMeas.getDescription());
            fetaAdap.setAIBDetails(aibDetails);

            RawDetails rawDetails = new RawDetails();
            rawDetails.setTable(koalaMeas.getID() + "_O2");
            rawDetails.setView(koalaMeas.getID() + "_RAWPS");
            rawDetails.setViewRaw(koalaMeas.getID() + "_PMC");
            rawDetails.setViewAgg(koalaMeas.getID() + "_RAWPV");

            SummaryDetails summaryDetails = new SummaryDetails();
            // for each time and level combination add a table

            fetaAdap.setSummaryDetails(summaryDetails);

            Counters counterList = new Counters();
            Adaptation.Measurement.PhysicalCounters physicalCounters = new Adaptation.Measurement.PhysicalCounters();
//            List<Adaptation.Measurement.PhysicalCounters.Counter> koalaCounters = ;
            for (Adaptation.Measurement.PhysicalCounters.Counter koalaCounter : koalaMeas.getPhysicalCounters().getCounter()) {
                Counter counter = new Counter();
                counter.setID(koalaCounter.getID());
                counter.setColName(koalaCounter.getOMeSName());
                counter.setTimeFormula(koalaCounter.getTimeRawFormula());//.substring(0, 3));
                counter.setNwFormula(koalaCounter.getObjRawFormula());//.substring(0, 3));
                counter.setUnit(koalaCounter.getUnit());
                counter.setNeName(koalaCounter.getNEName());
                counter.setDescription(koalaCounter.getDescription());

                System.out.println("here");

                counterList.getCounter().add(counter);
            }

            fetaAdap.setCounters(counterList);

            fetaAdaptationList.add(fetaAdap);
        }

        return fetaAdaptationList;

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

//            inputStream = new BufferedInputStream(new FileInputStream("d://tmp/glsp/pmcbisKOALA.xml"));
            inputStream = new BufferedInputStream(new FileInputStream("d://tmp/glsp/adglsp.model"));
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
