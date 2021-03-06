package gateway;

import model.TreatmentCostsReply;
import model.TreatmentCostsRequest;
import org.glassfish.jersey.client.ClientConfig;
import serializer.TreatmentCostsSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;

public abstract class ClientAppGateway {

    private MessageSenderGateway sender = new MessageSenderGateway("middlewareClient");
    private HashMap<TreatmentCostsRequest,String> map = new HashMap<>();

    /**
     * Creates a listener for messages and forewords them to abstract method
     */
    public ClientAppGateway() {
        MessageReceiverGateway receiver = new MessageReceiverGateway("clientMiddleware");
        receiver.setListener(message -> {
            try {
                TextMessage dematerialized = (TextMessage) message;
                TreatmentCostsRequest treatmentCostsRequest = TreatmentCostsSerializer.treatmentCostsRequestFromJson(dematerialized.getText());
                map.put(treatmentCostsRequest,message.getJMSMessageID());
                onTreatmentRequestArrived(treatmentCostsRequest);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Abstract method is created and sends the parameters to the GUI
     * @param request Sends the Treatment Costs Request to the GUI
     */
    public abstract void onTreatmentRequestArrived(TreatmentCostsRequest request);

    /**
     * Method gets reply and checks if transport price is bigger than 0 and sends it to the client
     * @param request receives request parameter from GUI
     * @param reply receives reply parameter from GUI
     */
    public void sendHospitalRequest(TreatmentCostsRequest request, TreatmentCostsReply reply) {
        String id = map.get(request);
        String json = TreatmentCostsSerializer.treatmentCostsReplyToJson(reply);
        Message message = sender.createTextMessage(json);
        try {
            message.setJMSCorrelationID(id);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        sender.send(message);
    }

}
