package insurance.application;

import insurance.gateway.AppGateway;
import insurance.model.TreatmentCostsReply;
import insurance.model.TreatmentCostsRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class InsuranceClientController implements Initializable {
    @FXML
    private ListView<ClientListLine> lvRequestsReplies;
    @FXML
    private TextField tfSsn;
    @FXML
    private TextField tfAge;
    @FXML
    private TextField tfTreatmentCode;
    @FXML
    private CheckBox cbTransport;
    @FXML
    private TextField tfKilometers;

    /**
     * gateway variable with abstract method to receive the data and display in GUI
     */
    private AppGateway gateway = new AppGateway() {
        @Override
        public void onTreatmentReplyArrived(TreatmentCostsRequest request, TreatmentCostsReply reply) {
            ClientListLine listLine = getListLine(request);
            if (listLine != null) {
                listLine.setReply(reply);
                lvRequestsReplies.refresh();
            }
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfSsn.setText("123456");
        tfAge.setText("56");
        tfTreatmentCode.setText("ORT125");
        cbTransport.setSelected(false);
        tfKilometers.setDisable(true);
    }

    private ClientListLine getListLine(TreatmentCostsRequest request) {
        for (ClientListLine clientListLine : lvRequestsReplies.getItems()){
            if (clientListLine.getRequest() == request) {
                return clientListLine;
            }
        }
        return null;
    }

    public void transportChanged(){
        System.out.println(cbTransport.isSelected());
        if (!cbTransport.isSelected()){
            tfKilometers.setText("");
        }
        this.tfKilometers.setDisable(!this.cbTransport.isSelected());
    }

    public void btnSendClicked(){
        int ssn = Integer.parseInt(this.tfSsn.getText());
        String treatmentCode = this.tfTreatmentCode.getText();
        int age = Integer.parseInt(this.tfAge.getText());
        int transportDistance = 0;
        if (this.cbTransport.isSelected()) {
            transportDistance = Integer.parseInt(this.tfKilometers.getText());
        }
        TreatmentCostsRequest request = new TreatmentCostsRequest(ssn, age, treatmentCode, transportDistance);
        gateway.applyForTreatment(request);
        ClientListLine clientListLine = new ClientListLine(request,null);
        lvRequestsReplies.getItems().add(clientListLine);
    }
}
