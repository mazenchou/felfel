package org.wildfly.examples;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

@JMSDestinationDefinition(
    name = "java:/jms/topic/ChatTopic",
    interfaceName = "jakarta.jms.Topic",
    destinationName = "ChatTopic"
)
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/topic/ChatTopic"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Topic")
})
public class ChatMessageMDB implements MessageListener {

    @Inject
    MessageService messageService;

    @Override
    public void onMessage(jakarta.jms.Message message) {
        try {
            String text = ((TextMessage) message).getText();
            System.out.println("JMS received: " + text);

            int idx = text.indexOf(":");
            if (idx > 0) {
                messageService.save(text.substring(0, idx).trim(), text.substring(idx + 1).trim());
            }
            ChatEndpoint.broadcast(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}