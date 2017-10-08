package com.testing.TestActiveMQ;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class AsyncMessageReceiver implements MessageListener {

	public AsyncMessageReceiver() throws Exception {
		ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection c=activeMQConnectionFactory.createConnection();
		c.start();
				Session session=c.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Queue queue=session.createQueue("EM_TRADE.Q");
		MessageConsumer consumer=session.createConsumer(queue);
		consumer.setMessageListener(this);
		System.out.println("Waiting!!!!!!");
		
	}
	
	
	public void onMessage(Message message) {
		System.out.println(message);
	}
	
	
	public static void main(String []args){
		new Thread(){
			@Override
			public void run(){
				try {
					new AsyncMessageReceiver();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

}

