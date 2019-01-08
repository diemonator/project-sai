package insurance.gateway;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class MessageSenderGateway {

    private Session session;
    private MessageProducer producer;

    /**
     * Creates Active mq connection and destination
     * @param channelName required name of destination's endpoint
     */
    public MessageSenderGateway(String channelName) {
        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            props.put(("queue."+channelName), channelName);
            Context jndiContext = new InitialContext(props);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                    .lookup("ConnectionFactory");
            Connection connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) jndiContext.lookup(channelName);
            producer = session.createProducer(destination);
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method creates text message
     * @param body String of type JSON is passed to create message
     * @return returns the created text message from the session
     */
    public Message createTextMessage(String body) {
        Message msg = null;
        try {
            msg = session.createTextMessage(body);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * Method sends the msg
     * @param msg a message is required so it can be sent
     */
    public void send(Message msg) {
        try {
            producer.send(msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
