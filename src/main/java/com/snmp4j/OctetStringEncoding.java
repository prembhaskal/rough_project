package com.snmp4j;

import org.snmp4j.smi.OctetString;

import java.nio.charset.StandardCharsets;

public class OctetStringEncoding {

    public static void main(String[] args) {

        String chineseChars = "见/見";
        byte[] chineseBytes = chineseChars.getBytes(StandardCharsets.UTF_8);
        OctetString chineseOctString = OctetString.fromByteArray(chineseBytes);

        System.out.println("chinese characters: " + chineseChars);

        System.out.println("chinese octet string toString() " + chineseOctString.toString());

        System.out.println("chinese octet string toString() " + new String(chineseOctString.toByteArray(), StandardCharsets.UTF_8));
        //OctetString octetString = new
        
        // testing all ascii characters
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 32; i < 127; i++) {
            stringBuilder.append(Character.toChars(i));
        }

        System.out.println("stringbuilder " + stringBuilder.toString());

        System.out.println("ascii octet string " + OctetString.fromByteArray(stringBuilder.toString().getBytes(StandardCharsets.UTF_8)));
    }
}
