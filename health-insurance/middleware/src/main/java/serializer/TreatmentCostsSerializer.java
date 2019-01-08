package serializer;

import com.google.gson.Gson;
import model.TreatmentCostsReply;
import model.TreatmentCostsRequest;

public class TreatmentCostsSerializer {
    private static Gson gson = new Gson();

    public static TreatmentCostsRequest treatmentCostsRequestFromJson(String json) {
        return gson.fromJson(json,TreatmentCostsRequest.class);
    }

    public static String treatmentCostsReplyToJson(TreatmentCostsReply treatmentCostsReply) {
        return gson.toJson(treatmentCostsReply);
    }
}
