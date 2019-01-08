package gateway;

import model.HospitalCostsReply;
import model.HospitalCostsRequest;
import serializer.HospitalCostsSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class HospitalAppGateway {
    // destination endpoints
    private MessageSenderGateway senderCatherina = new MessageSenderGateway("catharinaRequestQueue");
    private MessageSenderGateway senderMaxima = new MessageSenderGateway("maximaRequestQueue");
    private MessageSenderGateway senderUmc = new MessageSenderGateway("umcRequestQueue");

    // Hospital Costs Request based on Correlation ID
    private HashMap<String, HospitalCostsRequest> map = new HashMap<>();
    // Recipient list
    private ArrayList<MessageSenderGateway> recipientList = new ArrayList<>();
    // Aggregators
    private ArrayList<Aggregator> aggregators = new ArrayList<>();
    // static int for Messages
    private static int aggregateId = 1;

    public HospitalAppGateway() {
        MessageReceiverGateway receiver = new MessageReceiverGateway("hospitalsReply");
        receiver.setListener(message -> {
            try {
                // finds aggregator with this specific ID
                Aggregator aggregator = getAggregator(message.getIntProperty("aggregation"));
                if (aggregator != null) {
                    TextMessage json = (TextMessage) message;
                    HospitalCostsReply reply = HospitalCostsSerializer.hospitalCostReplyFromJson(json.getText());
                    aggregator.addToAggregatorList(reply);
                    // checks if the aggregator has all of the elements
                    if (aggregator.currentSize() == aggregator.getSize()) {
                        reply = aggregator.getLowestHospitalOffer();
                        HospitalCostsRequest request = map.get(message.getJMSCorrelationID());
                        onHospitalReplyArrived(reply, request);
                    }
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    private Aggregator getAggregator(int id) {
        for (Aggregator aggregate: aggregators) {
            if (aggregate.getId() == id)
                return aggregate;
        }
        return null;
    }

    public abstract void onHospitalReplyArrived(HospitalCostsReply reply, HospitalCostsRequest request);

    public void sendHospitalRequest(HospitalCostsRequest request) {
        String json = HospitalCostsSerializer.hospitalCostRequestToJson(request);
        int age = request.getAge();
        if (age >= 10 && request.getTreatmentCode().contains("ORT"))
            recipientList.add(senderCatherina);
        if (age >= 18)
            recipientList.add(senderMaxima);
        recipientList.add(senderUmc);
        sendRequestToRecipients(json,request);
    }

    // send message to to every recipient
    private void sendRequestToRecipients(String json, HospitalCostsRequest request) {
        Aggregator aggregator = new Aggregator(aggregateId++);
        aggregators.add(aggregator);
        int size = 0;
        for (MessageSenderGateway senderGateway: recipientList) {
            try {
                Message msg = senderGateway.createTextMessage(json);
                msg.setIntProperty("aggregation",aggregator.getId());
                senderGateway.send(msg);
                map.put(msg.getJMSMessageID(), request);
                size++;
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        aggregator.setSize(size);
        recipientList = new ArrayList<>();
    }
}
