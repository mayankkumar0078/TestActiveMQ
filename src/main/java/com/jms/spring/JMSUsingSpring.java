package com.jms.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jndi.JndiTemplate;

public class JMSUsingSpring {

	public static void main(String[] args) {
		ApplicationContext context=new ClassPathXmlApplicationContext("ApplicationContext.xml");
		
		JndiTemplate template=context.getBean("jndiTemplate",JndiTemplate.class);
		JmsTemplate jmsTemplate=context.getBean("jmsTemplate",JmsTemplate.class);
		
		jmsTemplate.convertAndSend("My name is Mayank");
		System.out.println("Mayank");
	}

}
