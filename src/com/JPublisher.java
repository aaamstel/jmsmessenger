package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JPublisher {

	static QueueConnectionFactory qfactory;
	static Queue qq;
	static QueueConnection qconnection;
	static QueueSession qsession;
	static InitialContext ctx;
	static MessageProducer mproducer;
	static TextMessage msg;
	
	public static void main(String qrgs[]) {
		
		 Properties props = new Properties();
         props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
         props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		
		 try {
			//Create InitialContext
			ctx = new InitialContext(props);	
			//Find Queue connection factory via JNDI on Glassfish server
			qfactory = (QueueConnectionFactory) ctx.lookup("jms/__defaultConnectionFactory");
			//Find Queue via JNDI on Glassfish server
			qq = (Queue) ctx.lookup("jms/GFTestQueue");
			qconnection = qfactory.createQueueConnection();
			//Create Session on Queue connection
			qsession = qconnection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);	
			//Create Message producer on Queue session instance
			mproducer = qsession.createProducer(qq);
			//Start connection
			qconnection.start();
			//Create Text message on Queue session
			msg = qsession.createTextMessage();
			
			 System.out.println("Connection started");
			
			 BufferedReader br = null;
		     br = new BufferedReader(new InputStreamReader(System.in));
		     System.out.println("Enter key:value, q - Exit");
			 //Set text message that will be sent in Queue
		     msg.setText("Test of my personal Queue on Glassfish without EJB");	
		     //Producer sends message in Queue on server
			 mproducer.send(msg);
	     
		     while (true) {
		            String input = br.readLine();
		            String[] split = input.split(":");

		            if ("q".equals(input)) {
		            	qconnection.stop();
		                System.out.println("Exit!");
		                System.exit(0);
		            } else {
		                switch (split.length) {
		                    case 1:		                
		                    	msg.setText(split[0]);
		                    	mproducer.send(msg);
		                        break;
		                    case 2:
		                    	msg.setText(split[1]);
		                    	mproducer.send(msg);
		                        break;
		                    case 3:
		                    	msg.setText(split[2]);
		                    	mproducer.send(msg);
		                        break;
		                    default:
		                        System.out.println("Enter key:value, q - Exit");
		                }
		            }
		     }		
			
		} catch (NamingException | JMSException | IOException e) {
	
			e.printStackTrace();
		}			
	}		
}
