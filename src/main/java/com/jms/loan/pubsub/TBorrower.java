package com.jms.loan.pubsub;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TBorrower implements MessageListener {

	private TopicConnection connection;
	private TopicSession topicSession;
	private Topic topic;

	public TBorrower() throws NamingException, JMSException {
		Context ctx = new InitialContext();
		TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ctx.lookup("ConnectionFactory");
		connection = topicConnectionFactory.createTopicConnection();
		topicSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = (Topic) ctx.lookup("jms.topic1");
		connection.start();
		TopicSubscriber subscriber = topicSession.createSubscriber(topic);
		subscriber.setMessageListener(this);
	}

	public static void main(String[] args) throws NamingException, JMSException {
		TBorrower borrower = new TBorrower();

	}

	private void exit() {
		try {
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void onMessage(Message message) {
		BytesMessage bytesMessage = (BytesMessage) message;
		try {
			double rate = bytesMessage.getDoubleProperty("interestRate");
			System.out.println("rate is " + rate);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
