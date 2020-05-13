package com.jmx;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.HashMap;
import java.util.Map;

public class JMXConnectExample {

    public static void main(String[] args) {
        int portNum = 31003;
        String hostname = "10.32.234.164";
        try {
            JMXServiceURL u = new JMXServiceURL(
                    "service:jmx:rmi:///jndi/rmi://" + hostname + ":" + portNum +  "/jmxrmi");

            final Map<String, Object> environment = new HashMap<>();
            String user = "isdksnmp";
            String pass = "isdksnmp";
            environment.put(JMXConnector.CREDENTIALS, new String[]{user,pass});

            JMXConnector c = JMXConnectorFactory.connect(u, environment);
            c.connect();

            System.out.println("connection done");
            c.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
