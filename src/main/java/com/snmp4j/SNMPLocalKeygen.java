/*
* Copyright (C) 2017 Nokia. All rights reserved.
*/
package com.snmp4j;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.InetAddress;

public class SNMPLocalKeygen
{
    private static final int TIMEOUT = 5000; 
    private SNMPLocalKeygen(){}
    public static void main(String[] args) throws IOException {
        String NEprivPwd= "SDMENE3S123";
        String NEauthPwd= "SDMENE3S123";
        String NEprivProtocol="AES128";
        String NEauthProtocol="SHA";
        String IPaddress="10.55.201.63/1161";
        TransportMapping<?> transport;
        Snmp snmp;
        transport = new DefaultUdpTransportMapping(new UdpAddress(InetAddress.getLocalHost(), 0));
        snmp = new Snmp(transport);
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);

        transport.listen();
     
        // Create UserTarget for snmp v3
        Address targetAddress = GenericAddress.parse(IPaddress);
        UserTarget target = new UserTarget();
        target.setAddress(targetAddress);
        target.setRetries(1);
        target.setTimeout(TIMEOUT); // in millisecs
        target.setVersion(SnmpConstants.version3);
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);


        byte[] engineIDBytes = getEngineID( snmp, targetAddress );
        
    SecurityProtocols securityProtocols = SecurityProtocols.getInstance();
    securityProtocols.addDefaultProtocols();
    OctetString authPwd = new OctetString(NEauthPwd);
    OctetString privPwd = new OctetString(NEprivPwd);
    byte[] privPwdBytes=getPrivKey( NEprivProtocol, NEauthProtocol, engineIDBytes, securityProtocols, privPwd ); 
    byte[] authPwdBytes=getAuthKey( NEauthProtocol, engineIDBytes, securityProtocols, authPwd );

    System.out.println( DatatypeConverter.printHexBinary( authPwdBytes ) + " " + DatatypeConverter.printHexBinary( privPwdBytes ));
    }

    private static byte[] getPrivKey(
        String NEprivProtocol,
        String NEauthProtocol,
        byte[] engineIDBytes,
        SecurityProtocols securityProtocols,
        OctetString privPwd )
    {
        byte[] privPwdBytes;
        privPwdBytes = securityProtocols.passwordToKey(getPriv(NEprivProtocol), getAuth(NEauthProtocol), privPwd, engineIDBytes);
        if (privPwdBytes == null) 
        {
            System.exit( 5 );
        }
        return privPwdBytes;
    }

    private static byte[] getAuthKey(
        String NEauthProtocol,
        byte[] engineIDBytes,
        SecurityProtocols securityProtocols,
        OctetString authPwd )
    {
        byte[] authPwdBytes;
        authPwdBytes = securityProtocols.passwordToKey(getAuth(NEauthProtocol), authPwd, engineIDBytes);
        if (authPwdBytes == null) 
        {
            System.exit( 4 );
        }
        return authPwdBytes;
    }

    private static OID getAuth(String authProtocol)
    {
        if("SHA".equalsIgnoreCase( authProtocol ))
        {
            return AuthSHA.ID;
        }
        else if("MD5".equalsIgnoreCase( authProtocol ))
        {
            return AuthMD5.ID;
        }
        return null;
    }
    
    private static OID getPriv(String privProtocol)
    {
        if("AES128".equalsIgnoreCase( privProtocol ))
        {
            return PrivAES128.ID;
        }
        else if("DES256".equalsIgnoreCase( privProtocol ))
        {
            return PrivDES.ID;
        }
        return null;
    }
    
    private static byte[] getEngineID( Snmp snmp, Address targetAddress )
    {
        byte[] engineIDBytes  = snmp.discoverAuthoritativeEngineID(targetAddress, TIMEOUT);
        OctetString authEngineID = OctetString.fromByteArray(engineIDBytes);

        if (authEngineID == null)
        {
            System.exit( 3 );
        }
        return engineIDBytes;
    }
}