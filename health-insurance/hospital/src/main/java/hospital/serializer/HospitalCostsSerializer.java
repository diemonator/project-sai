package hospital.serializer;

import com.google.gson.Gson;
import hospital.model.HospitalCostsReply;
import hospital.model.HospitalCostsRequest;

public class HospitalCostsSerializer {

    private static Gson gson = new Gson();

    public static String hospitalCostReplyToJson(HospitalCostsReply hospitalCostsReply) {
        return gson.toJson(hospitalCostsReply);
    }

    public static HospitalCostsRequest hospitalCostRequestFromJson(String json) {
        return gson.fromJson(json,HospitalCostsRequest.class);
    }
}
