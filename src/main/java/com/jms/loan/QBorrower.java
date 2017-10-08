package com.jms.loan;

import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.MapMessage;
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


public class QBorrower {

	private QueueConnection queueConnection;
	private QueueSession queueSession;
	private Queue requestQueue;
	private Queue responseQueue;
	
	public QBorrower() throws NamingException, JMSException {
		Context ctx=new InitialContext();
		QueueConnectionFactory connectionFactory=(QueueConnectionFactory)ctx.lookup("ConnectionFactory");
		queueConnection=connectionFactory.createQueueConnection();
		requestQueue=(Queue)ctx.lookup("jms.LoanRequestQueue");
		responseQueue=(Queue)ctx.lookup("jms.LoanResponseQueue");
		queueConnection.start();
		queueSession=queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	
	private void sendLoanRequest(double salary,double loanAmount) throws JMSException {
		MapMessage message=queueSession.createMapMessage();
		message.setDoubleProperty("salary", salary);
		message.setDoubleProperty("loanAmount", loanAmount);
		message.setJMSReplyTo(responseQueue);
		QueueSender sender=queueSession.createSender(requestQueue);
		sender.send(message);
		
		String filter="JMSCorrelationID='"+message.getJMSCorrelationID()+"'";
		QueueReceiver queueReceiver=queueSession.createReceiver(responseQueue);
		TextMessage textMessage=(TextMessage)queueReceiver.receive(30000);
		
		if(textMessage==null) {
			System.out.println("Lender is not responding");
		}else {
			System.out.println("loan request was : "+textMessage.getText());
		}
		
	}
	
	private void exit() throws JMSException {
		queueConnection.close();
	}
	
	public static void main(String[] args) throws NamingException, JMSException {
		QBorrower borrower=new QBorrower();
		Scanner in=new Scanner(System.in);
		System.out.println("Please enter your salary and loan amount, type quit to exit");
		String input="continue";
		do {
			double salary=in.nextDouble();
			double loanAmount=in.nextDouble();
			
			borrower.sendLoanRequest(salary, loanAmount);
			input=in.nextLine();
		}while(!input.equals("quit"));
		
	}

}
