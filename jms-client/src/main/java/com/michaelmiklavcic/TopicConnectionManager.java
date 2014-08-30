package com.michaelmiklavcic;

import java.io.Closeable;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.michaelmiklavcic.util.Closer;

public class TopicConnectionManager implements Closeable {
    private Connection connection;
    private TopicSubscriber subscriber;

    public void start(String dest, String topicName) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(dest);
        connection = connectionFactory.createConnection();
        TopicSession session = (TopicSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topicName);
        subscriber = session.createSubscriber(topic);
    }

    public FalconMessage receive(int interval) throws JMSException {
        Message message = subscriber.receive(1000);
        if (message != null) {
            if (message instanceof MapMessage) {
                return new FalconMessage((MapMessage) message);
            } else {
                System.out.println("Unexpected message received of type: " + message.getClass());
            }
        }
        return null;
    }

    public void close() {
        new Closer().close(connection);
    }

}
