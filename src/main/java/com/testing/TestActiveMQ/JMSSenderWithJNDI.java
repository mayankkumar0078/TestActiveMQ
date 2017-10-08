package com.testing.TestActiveMQ;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSSenderWithJNDI {
	public static void main(String []args) throws NamingException, JMSException{
		Context ctx=new InitialContext();
		Connection connection=((ConnectionFactory)ctx.lookup("ConnectionFactory")).createConnection();
		connection.start();
		Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue=(Queue)ctx.lookup("EM_TRADE.Q");
		MessageProducer sender=session.createProducer(queue);
		TextMessage msg=session.createTextMessage("My name is Mayank.");
		sender.send(msg);
		System.out.println("Hi Mayank!");
		connection.close();
		
	}

}
