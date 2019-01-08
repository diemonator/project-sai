package insurance.serializer;

import com.google.gson.Gson;
import insurance.model.TreatmentCostsReply;
import insurance.model.TreatmentCostsRequest;

public class TreatmentCostsSerializer {
    private static Gson gson = new Gson();

    /**
     * This method is created to serialize treatmentCostsRequest object to String in JSON format
     * @param treatmentCostsRequest requires object to be serialized
     * @return Returns String of type JSON
     */
    public static String treatmentCostsRequestToJson(TreatmentCostsRequest treatmentCostsRequest) {
        return gson.toJson(treatmentCostsRequest);
    }

    /**
     * Method creates object out of JSON String type
     * @param json receives json in string type
     * @return and returns object of type TreatmentCostsReply
     */
    public static TreatmentCostsReply treatmentCostsReplyFromJson(String json) {
        return gson.fromJson(json,TreatmentCostsReply.class);
    }
}
