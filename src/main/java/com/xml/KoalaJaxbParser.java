package com.xml;

import com.xml.feta.model.*;
//import com.xml.koala.model.Adaptation;
import com.xml.pmmodel.model.Adaptation;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


// FIXME model file and koala file -- unit, nename are placed differently :(
public class KoalaJaxbParser {

    enum TimeInterval {
        HOUR("hour", 0),
        DAY("day", 1),
        WEEK("week", 2),
        MONTH("month", 3)
        ;

        private final String interval;
        private final int index;

        TimeInterval(String  interval, int index) {
            this.interval = interval;
            this.index = index;
        }

        public static Map<Integer, TimeInterval> getIndexVsTimeInterval() {
            return Arrays.stream(values()).
                    collect(
                            Collectors.toMap(
                                    value -> value.index,
                                    value -> value,
                                    (a, b) -> b,
                                    HashMap::new));
        }
    }

    // TODO -- use this instead https://stackoverflow.com/questions/8626153/make-jaxb-go-faster/8626388#8626388

    public static void main(String[] args) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        new KoalaJaxbParser().run("d:/tmp/glsp", "ADGLSP", "d://tmp/glsp/adglsp.model");
//        System.out.println(Arrays.toString(new KoalaJaxbParser().getLevelRefs("a.b.c.d.e", "a.b.c.d").toArray(new String[]{})));
    }

    private void run(String fileDirectory, String schemaPrefix, String koalaFilePath) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        Adaptation adaptation = parseKoala(koalaFilePath);
        List<com.xml.feta.model.Adaptation> adaptations = convertToFeta(adaptation, schemaPrefix);
        createFeta(adaptations, fileDirectory, schemaPrefix);
        System.out.println("done");
    }

    private void createFeta(List<com.xml.feta.model.Adaptation> adaptations, String fileDirectory, String schemaPrefix) throws JAXBException, IOException {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(com.xml.feta.model.Adaptation.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());

            File directory = new File(fileDirectory);
            for (com.xml.feta.model.Adaptation adaptation : adaptations) {
                String measId = adaptation.getMeasurementNumber();
                String fileName = String.format("%s_P_MEAS_%s_O2.%s.xml", schemaPrefix, measId, measId);
                File file = new File(directory, fileName);
                writeFile(marshaller, adaptation, file);
            }

        }
        catch (JAXBException | IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void writeFile(Marshaller marshaller, com.xml.feta.model.Adaptation adaptation, File file) throws IOException, JAXBException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            marshaller.marshal(adaptation, bos);
        }
    }

    private List<com.xml.feta.model.Adaptation> convertToFeta(Adaptation adaptation, String schemaPrefix) {
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

            List<String> timeLevels = getTimeSummaryLevel(koalaMeas.getTime().getFirstLevel(), koalaMeas.getTime().getLastLevel());
            fetaAdap.setTimeSummaryLevels(convertToCommaSeparated(timeLevels));

            List<String> topoLevels = getLevelRefs(koalaMeas.getTopologyRef().getFirstLevelRef(), koalaMeas.getTopologyRef().getLastLevelRef());
            fetaAdap.setNWSummaryLevels(convertToCommaSeparated(topoLevels));

            AIBDetails aibDetails = new AIBDetails();
            aibDetails.setMeasurementName(koalaMeas.getID());
            aibDetails.setDescription(koalaMeas.getDescription().trim());
            fetaAdap.setAIBDetails(aibDetails);

            fetaAdap.setRawDetails(getRawDetails(schemaPrefix, koalaMeas.getID(), topoLevels.get(0)));

            SummaryDetails summaryDetails = getSummaryDetails(schemaPrefix, koalaMeas.getID(), timeLevels, topoLevels);
            // for each time and level combination add a table

            fetaAdap.setSummaryDetails(summaryDetails);

            Counters counterList = new Counters();
            Adaptation.Measurement.PhysicalCounters physicalCounters = new Adaptation.Measurement.PhysicalCounters();
//            List<Adaptation.Measurement.PhysicalCounters.Counter> koalaCounters = ;
            for (Adaptation.Measurement.PhysicalCounters.Counter koalaCounter : koalaMeas.getPhysicalCounters().getCounter()) {
                Counter counter = new Counter();
                counter.setID(koalaCounter.getID());
                counter.setColName(koalaCounter.getOMeSName());
                counter.setTimeFormula(getTimeRawFormula(koalaCounter));//.substring(0, 3));
                counter.setNwFormula(getObjRawFormula(koalaCounter));//.substring(0, 3));
                counter.setUnit(koalaCounter.getUnit());
                counter.setNeName(koalaCounter.getNEName());
                System.out.println("unit " + koalaCounter.getUnit());
                counter.setDescription(koalaCounter.getDescription().trim());
//                System.out.println("here");

                counterList.getCounter().add(counter);
            }

            fetaAdap.setCounters(counterList);

            fetaAdaptationList.add(fetaAdap);
        }

        return fetaAdaptationList;
    }

    private String getObjRawFormula(Adaptation.Measurement.PhysicalCounters.Counter koalaCounter) {
        return getFormula(koalaCounter.getObjRawFormula());
    }

    private String getFormula(String expression) {
        if (expression!= null && expression.contains("(")) {
            return expression.substring(0, expression.indexOf('(')).toLowerCase();
        }
        return "";
    }

    private String getTimeRawFormula(Adaptation.Measurement.PhysicalCounters.Counter koalaCounter) {
        return getFormula(koalaCounter.getTimeRawFormula());
    }

    private RawDetails getRawDetails(String schemaPrefix, String measId, String lastLevel) {
        RawDetails rawDetails = new RawDetails();
        String tableName = String.format("%s_P_MEAS_%s_O2", schemaPrefix, measId);
        String view = String.format("%s_PS_%s_%s_RAW", schemaPrefix, measId, lastLevel);
        String viewRaw = String.format("%s_P_%s_%s_PMC", schemaPrefix, measId, lastLevel);
        String viewAgg = String.format("%s_PV_%s_%s_RAW", schemaPrefix, measId, lastLevel);

        rawDetails.setTable(tableName);
        rawDetails.setView(view);
        rawDetails.setViewRaw(viewRaw);
        rawDetails.setViewAgg(viewAgg);
        return rawDetails;
    }

    private SummaryDetails getSummaryDetails(String schemaPrefix, String measId, List<String> timeLevels, List<String> topoLevels) {
        SummaryDetails summaryDetails = new SummaryDetails();
        for (String topoLevel : topoLevels) {
            for (String timeLevel : timeLevels) {
                timeLevel = getColNamePart(timeLevel);
                String tableName = String.format("%s_P_%s_%s_%s", schemaPrefix, measId, topoLevel, timeLevel);
                String view = String.format("%s_PS_%s_%s_%s", schemaPrefix, measId, topoLevel, timeLevel);
                String viewAgg = String.format("%s_PV_%s_%s_%s", schemaPrefix, measId, topoLevel, timeLevel);
                SumTable sumTable  = new SumTable();
                sumTable.setTable(tableName);
                sumTable.setView(view);
                sumTable.setViewAgg(viewAgg);
                summaryDetails.getSumTable().add(sumTable);
            }
        }

        return summaryDetails;
    }

    private String getColNamePart(String timeLevel) {
        timeLevel = timeLevel.toUpperCase();
        if (timeLevel.equals("MONTH"))
            return "MON";
        return timeLevel;
    }

    private String convertToCommaSeparated(List<String> list) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = list.iterator();
        if (iterator.hasNext()) {
            sb.append(iterator.next());
        }
        for (;iterator.hasNext();) {
            sb.
                    append(",")
                    .append(iterator.next());
        }


        return sb.toString();

    }

    private List<String> getTimeSummaryLevel(String firstLevelStr, String lastLevelStr) {
        TimeInterval firstLvl = TimeInterval.valueOf(firstLevelStr.toUpperCase());
        TimeInterval lastLvl = TimeInterval.valueOf(lastLevelStr.toUpperCase());

        Map<Integer, TimeInterval> map = TimeInterval.getIndexVsTimeInterval();
        return IntStream.rangeClosed(firstLvl.index, lastLvl.index)
                .mapToObj(i -> map.get(i).interval)
                .collect(Collectors.toList());
    }

    public List<String> getLevelRefs(String firstLevel, String lastLevel) {
        String[] firstLvls = firstLevel.split("\\.");
        String[] lastLvls = lastLevel.split("\\.");
        String parentLevel = lastLvls[lastLvls.length - 1];

        List<String> list = new ArrayList<>();
        for (int i = firstLvls.length - 1; i >= 0; i--) {
            String currentLevel = firstLvls[i];
            list.add(currentLevel);
            if (currentLevel.equals(parentLevel)) {
                break;
            }
        }
        return list;
    }

    private Adaptation parseKoala(String koalaFile) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        JAXBContext jaxbContext = null;
        BufferedInputStream inputStream = null;

        try {
            jaxbContext = JAXBContext.newInstance(Adaptation.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();

//            inputStream = new BufferedInputStream(new FileInputStream("d://tmp/glsp/pmcbisKOALA.xml"));
            inputStream = new BufferedInputStream(new FileInputStream(koalaFile));
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
