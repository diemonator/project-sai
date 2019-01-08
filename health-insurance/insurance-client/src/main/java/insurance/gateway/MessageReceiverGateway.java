package insurance.gateway;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class MessageReceiverGateway {

    private MessageConsumer consumer;

    /**
     * Method establishes connection with active mq
     * @param channelName Channel is being created in active mq
     */
    public MessageReceiverGateway(String channelName) {
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
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) jndiContext.lookup(channelName);
            consumer = session.createConsumer(destination);
            connection.start();
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method sets listener event
     * @param ml request message listener parameter
     */
    public void setListener(MessageListener ml) {
        try {
            consumer.setMessageListener(ml);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
