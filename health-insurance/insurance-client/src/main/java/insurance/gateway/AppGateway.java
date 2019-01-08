package insurance.gateway;

import insurance.model.TreatmentCostsReply;
import insurance.model.TreatmentCostsRequest;
import insurance.serializer.TreatmentCostsSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.HashMap;

public abstract class AppGateway {

    private MessageSenderGateway sender = new MessageSenderGateway("clientMiddleware");
    private HashMap<String, TreatmentCostsRequest> map = new HashMap<>();

    /**
     * Setting message receiver and passing correct parameters
     */
    public AppGateway() {
        MessageReceiverGateway receiver = new MessageReceiverGateway("middlewareClient");
        receiver.setListener(message -> {
            try {
                TextMessage msgBody = (TextMessage) message;
                TreatmentCostsReply treatmentCostsReply = TreatmentCostsSerializer.treatmentCostsReplyFromJson(msgBody.getText());
                TreatmentCostsRequest treatmentCostsRequest = map.get(message.getJMSCorrelationID());
                onTreatmentReplyArrived(treatmentCostsRequest,treatmentCostsReply);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Method sends Treatment cost's request
     * @param request pass created request for submission
     */
    public void applyForTreatment(TreatmentCostsRequest request) {
        String reply = TreatmentCostsSerializer.treatmentCostsRequestToJson(request);
        Message message = sender.createTextMessage(reply);
        sender.send(message);
        try {
            map.put(message.getJMSMessageID(),request);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * Abstract method used to pass parameters to GUI
     * @param request returned
     * @param reply returned
     */
    public abstract void onTreatmentReplyArrived(TreatmentCostsRequest request, TreatmentCostsReply reply);
}
