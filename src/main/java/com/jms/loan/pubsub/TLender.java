package com.jms.loan.pubsub;

import java.util.Scanner;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TLender {

	private TopicConnection connection;
	private  TopicSession topicSession;
	private Topic topic;
	
	public TLender() throws NamingException, JMSException {
		Context ctx=new InitialContext();
		TopicConnectionFactory topicConnectionFactory=(TopicConnectionFactory)ctx.lookup("ConnectionFactory");
		connection=topicConnectionFactory.createTopicConnection();
		topicSession=connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		topic=(Topic)ctx.lookup("jms.topic1");
		
	}
	
	public static void main(String[] args) throws NamingException, JMSException {
		TLender lender=new TLender();
		Scanner in=new Scanner(System.in);
		System.out.println("Type C to continue");
		while(!in.nextLine().equals("quit")) {
			System.out.println("Enter the interest rate");
			lender.publishRate(in.nextDouble());
			System.out.println("Enter quit to quit");
			
		}
		
		lender.exit();
		
	}
	
	private void exit() {
		try {
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void publishRate(double value) throws JMSException {
		TopicPublisher publisher = topicSession.createPublisher(topic);
		BytesMessage message=topicSession.createBytesMessage();
		message.setDoubleProperty("interestRate", value);
		publisher.publish(message);
	}
}
