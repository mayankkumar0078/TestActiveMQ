package com.jms.chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class ChatApp implements javax.jms.MessageListener{
	private TopicSession pubSession;
	private TopicSession subSession;
	private TopicConnectionFactory connectionFactory;
	private TopicConnection connection;
	private String userName; 
	private TopicPublisher publisher;
	
	public ChatApp(String connectionFactoryName,String topicName,String username) throws NamingException, JMSException {
		Context ctx=new InitialContext();
		connectionFactory=(TopicConnectionFactory)ctx.lookup(connectionFactoryName);
		connection=connectionFactory.createTopicConnection();
		pubSession=connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		subSession=connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic=(Topic)ctx.lookup(topicName);
		publisher=pubSession.createPublisher(topic);
		TopicSubscriber subscriber=subSession.createSubscriber(topic, null, true);
		subscriber.setMessageListener(this);
		userName=username;
		connection.start();
	}
	
	private void close() throws JMSException {
		connection.close();
	}
	public static void main(String[] args) {
		if(args.length!=3) {
			System.out.println("Factory, Topic, or username missing");
		}
		
		try {
			ChatApp app=new ChatApp(args[0],args[1],args[2]);
			
			BufferedReader commandLineReader=new BufferedReader(new InputStreamReader(System.in));
			while(true) {
				String s=null;
				try {
					s = commandLineReader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(s.equals("exit")) {
					app.close();
					System.exit(0);
				}else {
					app.writeMessage(s);
				}
				
				
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void writeMessage(String message) throws JMSException {
		TextMessage textMessage =pubSession.createTextMessage(message);
		publisher.publish(textMessage);
	}


	public void onMessage(Message message) {
		TextMessage messageText=(TextMessage)message;
		try {
			System.out.println(messageText.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}
