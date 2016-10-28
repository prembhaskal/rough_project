package com.mib;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibLoaderLog;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.snmp.SnmpIndex;
import net.percederberg.mibble.snmp.SnmpObjectType;
import net.percederberg.mibble.value.ObjectIdentifierValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MIBParser {

	public static void main(String[] args) {
//		File file = new File("D:\\temp\\scm_7510SE.mib");
		// MGW hardware NE
		File fileHE1 = new File("D:\\project\\cm\\mywork\\cm_mediation\\mgw_discussion\\files\\mib_files_updates_2ndAugust\\scm_7510.mib");
		File fileHE2 = new File("D:\\project\\cm\\mywork\\cm_mediation\\mgw_discussion\\files\\mib_files_updates_2ndAugust\\nam_7510.mib");


//		File fileSECE1 = new File("D:\\project\\cm\\mywork\\cm_mediation\\mgw_discussion\\files\\mib_files_updates_2ndAugust\\scm_7510SE.mib");
//		File fileSECE2 = new File("D:\\project\\cm\\mywork\\cm_mediation\\mgw_discussion\\files\\mib_files_updates_2ndAugust\\nam_7510SE.mib");

		new MIBParser().extractMIBDetails(fileHE1, fileHE2);
//		new MIBParser().extractMIBDetails(fileSECE1, fileSECE2);
	}

	public void extractMIBDetails(File ... mibFiles) {
		try( PrintWriter writer = new PrintWriter(new FileOutputStream("D:\\temp\\test.txt"));
		     PrintWriter nonAccessibleIndexWrt = new PrintWriter(new FileOutputStream("D:\\temp\\nonAccessibleIndex.txt"))) {

			Mib[] mibs = loadMibs(mibFiles);
			for (Mib mib : mibs) {
				parseMib(mib, writer, nonAccessibleIndexWrt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Mib[] loadMibs(File ... mibFiles) throws IOException {
		MibLoader loader = new MibLoader();

		try {
			for (File mibFile : mibFiles) {
				System.out.println("loading file " + mibFile.getName());
				loader.load(mibFile);
			}
		} catch (MibLoaderException e) {
			MibLoaderLog mibLoader = e.getLog();
			mibLoader.printTo(System.err);
			throw new RuntimeException(e);
		}

		return loader.getAllMibs();
	}

	public static Mib[] loadMib(File file) throws IOException {
		MibLoader loader = new MibLoader();
//		loader.

		try {
			File parentFile = file.getParentFile();
			loader.addDir(parentFile);
			for (File mibfile : parentFile.listFiles()) {
				if (mibfile.getName().contains(".mib")) {
					System.out.println("processing file " + mibfile.getName());
					loader.load(mibfile);
				}
			}
		} catch (MibLoaderException e) {
			MibLoaderLog mibLoader = e.getLog();
			mibLoader.printTo(System.err);
			throw new RuntimeException(e);
		}

		return loader.getAllMibs();
	}

	private void parseMib(Mib mib, PrintWriter writer, PrintWriter nonAccessIndexWriter) {

		for (Object mibSymbol : mib.getAllSymbols()) {
			parseSymbol((MibSymbol) mibSymbol, writer, nonAccessIndexWriter);
		}
	}

	private void parseSymbol(MibSymbol mibSymbol, PrintWriter writer, PrintWriter nonAccessIndexWriter) {
		if (mibSymbol instanceof MibValueSymbol) {
			MibType mibType = ((MibValueSymbol) mibSymbol).getType();
			MibValue mibValue = ((MibValueSymbol) mibSymbol).getValue();

			String OID = mibValue.toString();

			writer.println(mibSymbol.getName()
					+ ", " + OID
					+ ", " + getSnmpType(mibType)
					+ ", " + getSnmpAccess(mibType)
					+ ", " + getSyntaxType(mibType)
					+ ", " + getSnmpIndex(mibType, nonAccessIndexWriter)
					+ ", " + getDescription(mibType));
		}
	}

	private String getSnmpAccess(MibType mibType) {
		if (mibType instanceof SnmpObjectType) {
			return ((SnmpObjectType) mibType).getAccess().toString();
		}
		return "";
	}

	private String getSnmpIndex(MibType mibType, PrintWriter nonAccessIndexWriter) {
		if (mibType instanceof SnmpObjectType) {
			return "INDEX--[" + getSnmpIndex(((SnmpObjectType) mibType).getIndex(), nonAccessIndexWriter) + "]";
		}
		return "";
	}

	private String getSnmpIndex(List snmpIndexes, PrintWriter nonAcessIndexWriter) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Object obj : snmpIndexes) {
			SnmpIndex snmpIndex = (SnmpIndex) obj;
			stringBuilder.append(snmpIndex.getValue().getName())
					.append("::");
			MibValue mibValue = snmpIndex.getValue();
			if (mibValue instanceof ObjectIdentifierValue) {
				MibValueSymbol symbol = ((ObjectIdentifierValue) mibValue).getSymbol();
				stringBuilder.append(getSnmpAccess(symbol.getType()));
				if (getSnmpAccess(symbol.getType()).equals("not-accessible")) {
					fillNonAcessIndexInformation(nonAcessIndexWriter, snmpIndex);
				}
			}
			stringBuilder.append(", ");
		}

		return stringBuilder.toString();
	}

	private void fillNonAcessIndexInformation(PrintWriter nonAccessIndexWriter, SnmpIndex snmpIndex) {
		MibValueSymbol symbol = ((ObjectIdentifierValue)snmpIndex.getValue()).getSymbol();
		MibValueSymbol entry = symbol.getParent();
		MibValueSymbol table = entry.getParent();

		nonAccessIndexWriter.print(table.getName());
		nonAccessIndexWriter.print(",");
		nonAccessIndexWriter.print(snmpIndex.getValue().getName());
		nonAccessIndexWriter.print(",");
		nonAccessIndexWriter.println(snmpIndex.getValue());
	}

	private String getSnmpType(MibType mibType) {
		if (mibType instanceof SnmpObjectType) {
			return ((SnmpObjectType) mibType).getSyntax().getName();
		}
		return "";
	}

	private String getSyntaxType(MibType mibType) {
		if (mibType instanceof SnmpObjectType) {
			return ((SnmpObjectType) mibType).getSyntax().toString().replaceAll(",", ";");
		}
		return "";
	}

	private String getDescription(MibType mibType) {
		if (mibType instanceof SnmpObjectType) {
			return ((SnmpObjectType) mibType).getDescription().replaceAll("\n", "").replaceAll("\r", "").replaceAll(",", ";");
		}
		return "";
	}


}
