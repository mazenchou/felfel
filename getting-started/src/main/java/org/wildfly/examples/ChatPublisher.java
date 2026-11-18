package org.wildfly.examples;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Topic;

@ApplicationScoped
public class ChatPublisher {

    @Resource(lookup = "java:/jms/topic/ChatTopic")
    private Topic chatTopic;

    @Resource(lookup = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

    public void publish(String message) {
        try (JMSContext context = connectionFactory.createContext()) {
            context.createProducer().send(chatTopic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}