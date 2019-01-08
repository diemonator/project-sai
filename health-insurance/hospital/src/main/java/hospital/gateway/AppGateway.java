package hospital.gateway;

import hospital.model.HospitalCostsReply;
import hospital.model.HospitalCostsRequest;
import hospital.serializer.HospitalCostsSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.HashMap;

public abstract class AppGateway {

    // channel name is being passed to sender
    private MessageSenderGateway sender = new MessageSenderGateway("hospitalsReply");
    // from the hospital request holds, it's message in hash map
    private HashMap<HospitalCostsRequest,Message> map = new HashMap<>();

    /**
     * This method is used to receive messages from the Middleware and it holds a reference to abstract method on arrived
     * @param endpoint this is the endpoint which is passed from The Main class to create a message queue
     */
    public AppGateway(String endpoint) {
        MessageReceiverGateway receiver = new MessageReceiverGateway(endpoint);
        receiver.setListener(message -> {
            try {
                TextMessage deserialize = (TextMessage) message;
                HospitalCostsRequest hospitalCostsRequest = HospitalCostsSerializer.hospitalCostRequestFromJson(deserialize.getText());
                map.put(hospitalCostsRequest,message);
                onHospitalCostRequestArrived(hospitalCostsRequest);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method sends the replay message to the Middleware
     * @param reply it gets the reply
     * @param request
     */
    public void sendHospitalCostReply(HospitalCostsReply reply, HospitalCostsRequest request) {
        String json = HospitalCostsSerializer.hospitalCostReplyToJson(reply);
        Message message = sender.createTextMessage(json);
        Message answer = map.get(request);
        try {
            message.setIntProperty("aggregation",answer.getIntProperty("aggregation"));
            message.setJMSCorrelationID(answer.getJMSMessageID());
        } catch (JMSException e) {
            e.printStackTrace();
        }
        sender.send(message);
    }

    /**
     * Abstract method is used to pass parameter to GUI
     * @param request Hospital Costs Request object is being passed to GUI
     */
    public abstract void onHospitalCostRequestArrived(HospitalCostsRequest request);
}
