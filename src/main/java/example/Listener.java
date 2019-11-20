/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example;

import org.apache.qpid.amqp_1_0.jms.impl.*;
import javax.jms.*;

class Listener {

    public static void main(String []args) throws JMSException {

        String user = "admin"; //env("ACTIVEMQ_USER", "admin");
        String password = "activemq";//env("ACTIVEMQ_PASSWORD", "password");
        String host = "localhost";//env("ACTIVEMQ_HOST", "localhost");
        // amq协议的端口号
        int port = 5672; //Integer.parseInt(env("ACTIVEMQ_PORT", "5672"));
        String destination = "topic://event";//arg(args, 0, "topic://event");

//        String user = env("ACTIVEMQ_USER", "admin");
//        String password = env("ACTIVEMQ_PASSWORD", "password");
//        String host = env("ACTIVEMQ_HOST", "localhost");
//        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "5672"));
//        String destination = arg(args, 0, "topic://event");

        ConnectionFactoryImpl factory = new ConnectionFactoryImpl(host, port, user, password);
        Topic    dest = new TopicImpl(destination);
        

        Connection connection = factory.createConnection(user, password);
        connection.setClientID("lvshengnb");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createDurableSubscriber(dest, "lvsheng");
        long start = System.currentTimeMillis();
        long count = 1;
        System.out.println("Waiting for messages...");
        while(true) {
            Message msg = consumer.receive();
            if( msg instanceof  TextMessage ) {
                String body = ((TextMessage) msg).getText();
                if( "SHUTDOWN".equals(body)) {
                    long diff = System.currentTimeMillis() - start;
                    System.out.println(String.format("Received %d in %.2f seconds", count, (1.0*diff/1000.0)));
                    connection.close();
                    System.exit(1);
                } else {
                    try {
                        if( count != msg.getIntProperty("id") ) {
                            System.out.println("mismatch: "+count+"!="+msg.getIntProperty("id"));
                        }
                    } catch (NumberFormatException ignore) {
                    }
                    if( count == 1 ) {
                        start = System.currentTimeMillis();
                    }
                    
                    System.out.println(String.format("Received %d messages.", count));
                    
                    count ++;
                }

            } else {
                System.out.println("Unexpected message type: "+msg.getClass());
            }
        }
    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }

    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }
}