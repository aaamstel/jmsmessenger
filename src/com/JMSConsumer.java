package com;


import java.util.Properties;

//Igor Syrov

import javax.jms.*;

import javax.jms.JMSException;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSConsumer  {
	
	static QueueConnectionFactory qfactory;
	static Queue qq;
	static QueueConnection qconnection;
	static QueueSession qsession;
	static InitialContext ctx;
	static MessageConsumer mconsumer;
	static TextMessage msg;	
	
	public static void main(String args[]) {
			
		 Properties props = new Properties();
         props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
         props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
         
         try {
        	//New Context with properties
 			ctx = new InitialContext(props);			
 			//ConnectionFactory to lookup JMS ConnectionFactory on Glassfish server
 			qfactory = (QueueConnectionFactory) ctx.lookup("jms/__defaultConnectionFactory");
 			//Queue on server
 			qq = (Queue) ctx.lookup("jms/GFTestQueue");
 			//Get a Queue Connection on ConnectionFactory instance
 			qconnection = qfactory.createQueueConnection();
 			//Create Session on Queue Connection
 			qsession = qconnection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);		
 			//Create Message consumer in Queue session instance
 			mconsumer = qsession.createConsumer(qq);
 			//Connect to Queue
 			qconnection.start();
 			 			
 			while (true)  {
 				
 				msg = (TextMessage) mconsumer.receive();
 				System.out.println(msg.getText() + " Message has been received!");
 			
 			}
 	
 		} catch (NamingException | JMSException e) {
 	
 			e.printStackTrace();
 		}
         
  	 }
	}



