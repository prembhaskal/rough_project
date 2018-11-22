package com.xml.feta.model;

import java.util.List;

public class Adaptation {
    String MeasurementNumber;
    String ObjectHierarchy;
    String PMCoreMeasType;
    String RBFolderName;
    String OMeSName;
    String BusyHourDefinitionFormula;
    String TimeSummaryLevels;
    String NWSummaryLevels;

    AIBDetails aibDetails;
    RawDetails rawDetails;
    SummaryDetails summaryDetails;
    Counters counters;
}

class AIBDetails {
    String MeasurementName;
    String Description;
}

class RawDetails {
    String table;
    String view;
    String viewRaw;
    String viewAgg;

}

class SummaryDetails {
    List<SumTable> sumTableList;
}

class SumTable {
    String table;
    String view;
    String viewAgg;
}

class Counters {
    List<Counter> counters;
}

class Counter {
    String colName;
    String timeFormula;
    String nwFormula;
    String unit;
    String neName;
    String description;
}
