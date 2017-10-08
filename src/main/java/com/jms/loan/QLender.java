package com.jms.loan;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QLender implements MessageListener {

	private QueueConnection queueConnection;
	private Queue loanRequestQueue;
	private QueueSession queueSession;
	
	public QLender() throws NamingException, JMSException {
		Context ctx=new InitialContext();
		QueueConnectionFactory  connectionFactory=(QueueConnectionFactory) ctx.lookup("ConnectionFactory");
		queueConnection=connectionFactory.createQueueConnection();
		loanRequestQueue=(Queue)ctx.lookup("jms.LoanRequestQueue");
		queueSession=queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		queueConnection.start();
		QueueReceiver queueReceiver=queueSession.createReceiver(loanRequestQueue);
		queueReceiver.setMessageListener(this);
	}
	
	public static void main(String[] args) throws NamingException, JMSException {
		QLender queueLender=new QLender();
	}

	public void onMessage(Message message) {
		MapMessage mapMessage=(MapMessage)message;
		System.out.println("Message Received! "+mapMessage);
		try {
			double salary=mapMessage.getDoubleProperty("salary");
			double loanAmount=mapMessage.getDoubleProperty("loanAmount");
			boolean accepted=false;
			System.out.println(salary+"   "+loanAmount);
			if(loanAmount>200000) {
				accepted=(salary/loanAmount)> .25;
			}else {
				System.out.println(salary/loanAmount);
				accepted=(salary/loanAmount)> .33;
			}
			Queue replyToQueue=(Queue)mapMessage.getJMSReplyTo();
			QueueSender sender=queueSession.createSender(replyToQueue);
			TextMessage replyMessage=queueSession.createTextMessage();
			String text=accepted?"Accepted!":"Declined!";
			System.out.println("text "+text);
			replyMessage.setText(text);
			replyMessage.setJMSCorrelationID(mapMessage.getJMSMessageID());
			sender.send(replyMessage);
			
			System.out.println("Value of accepted is :"+accepted);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		
	}

}
