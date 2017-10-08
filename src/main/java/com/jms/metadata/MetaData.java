package com.jms.metadata;

import java.util.Enumeration;

import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MetaData {

	public static void main(String[] args) throws NamingException, JMSException {
		Context ctx=new InitialContext();
		TopicConnectionFactory topicConnectionFactory=(TopicConnectionFactory)ctx.lookup("ConnectionFactory");
		TopicConnection topicConnection=topicConnectionFactory.createTopicConnection();
		
		ConnectionMetaData metaData=topicConnection.getMetaData();
		
		System.out.println("Jms Version :"+metaData.getJMSMajorVersion() );
		System.out.println("Jms providerName :"+metaData.getJMSProviderName() );
		
		Enumeration jmxProperties=metaData.getJMSXPropertyNames();
		while(jmxProperties.hasMoreElements()) {
			System.out.println(jmxProperties.nextElement());
		}
	}

}
