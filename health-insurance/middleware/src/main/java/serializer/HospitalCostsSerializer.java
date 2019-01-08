package serializer;

import com.google.gson.Gson;
import model.HospitalCostsReply;
import model.HospitalCostsRequest;

public class HospitalCostsSerializer {

    private static Gson gson = new Gson();

    public static HospitalCostsReply hospitalCostReplyFromJson(String json) {
        return gson.fromJson(json,HospitalCostsReply.class);
    }

    public static String hospitalCostRequestToJson(HospitalCostsRequest hospitalCostsRequest) {
        return gson.toJson(hospitalCostsRequest);
    }
}
