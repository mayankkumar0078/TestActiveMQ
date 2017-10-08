package com.testing.TestActiveMQ;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSReceiver {

	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection=activeMQConnectionFactory.createConnection();
		connection.start();
		Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue=session.createQueue("EM_TRADE.Q");
		MessageConsumer consumer=session.createConsumer(queue);
		TextMessage msg=(TextMessage)consumer.receive();
		System.out.println(msg);
	}
}
 