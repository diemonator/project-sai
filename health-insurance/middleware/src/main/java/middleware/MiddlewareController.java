package middleware;

import gateway.ClientAppGateway;
import gateway.HospitalAppGateway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.HospitalCostsReply;
import model.HospitalCostsRequest;
import model.TreatmentCostsReply;
import model.TreatmentCostsRequest;

import javax.jms.JMSException;
import java.net.URL;
import java.util.ResourceBundle;

public class MiddlewareController implements Initializable {

    private ClientAppGateway clientAppGateway = new ClientAppGateway() {
        @Override
        public void onTreatmentRequestArrived(TreatmentCostsRequest request) {
            HospitalCostsRequest hospitalCostsRequest = new HospitalCostsRequest(request.getSsn(),request.getTreatmentCode(),request.getAge());
            hospitalAppGateway.sendHospitalRequest(hospitalCostsRequest);
            ListViewLine line = new ListViewLine(request);
            line.setHospitalCostsRequest(hospitalCostsRequest);
            lvListViewLines.getItems().add(line);
        }
    };

    private HospitalAppGateway hospitalAppGateway = new HospitalAppGateway() {
        @Override
        public void onHospitalReplyArrived(HospitalCostsReply reply, HospitalCostsRequest request) {
            ListViewLine line = getRequestReply(request);
            if (line != null) {
                line.setHospitalCostsReply(reply);
                lvListViewLines.refresh();
                clientAppGateway.sendHospitalRequest(line.getTreatmentCostsRequest(),new TreatmentCostsReply(reply.getPrice(),line.getTreatmentCostsRequest().getTransportDistance(),reply.getHospitalName()));
            }
        }
    };

    @FXML
    public ListView<ListViewLine> lvListViewLines;

    /**
     * This method returns the line of lvMessages which contains the given loan request.
     * @param request BankInterestRequest for which the line of lvMessages should be found and returned
     * @return The ListView line of lvMessages which contains the given request
     */
    private ListViewLine getRequestReply(HospitalCostsRequest request) {
        for (int i = 0; i < lvListViewLines.getItems().size(); i++) {
            ListViewLine rr = lvListViewLines.getItems().get(i);
            if (rr.getHospitalCostsRequest() != null && rr.getHospitalCostsRequest() == request) {
                return rr;
            }
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
