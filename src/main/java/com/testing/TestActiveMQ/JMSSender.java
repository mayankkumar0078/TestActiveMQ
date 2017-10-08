package com.testing.TestActiveMQ;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection; 
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JMSSender 
{
    public static void main( String[] args ) throws Exception
    {
    	
    		
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection c=activeMQConnectionFactory.createConnection();
        c.start();
        Session session=c.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue=session.createQueue("EM_TRADE.Q");
        MessageProducer sender=session.createProducer(queue);
        TextMessage  msg=session.createTextMessage("MY name is Mayank");
        
        sender.send(msg);
        System.out.println("Message Sent!");
        
        c.close();
        
    }
}
