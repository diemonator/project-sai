package hospital.application;

import hospital.gateway.AppGateway;
import hospital.model.Address;
import hospital.model.HospitalCostsReply;
import hospital.model.HospitalCostsRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

class HospitalController implements Initializable {

    @FXML
    private Label lbHospital;
    @FXML
    private Label lbAddress;
    @FXML
    private TextField tfPrice;
    @FXML
    private ListView<HospitalListLine> lvRequestReply;
    @FXML
    private Button btnSendReply;

    private final String hospitalName;
    private final Address address;
    private final String endpoint;
    private final AppGateway gateway;


    public HospitalController(String hospitalName, Address address, String hospitalRequestQueue) {
        this.address = address;
        this.hospitalName = hospitalName;
        endpoint = hospitalRequestQueue;
        gateway = new AppGateway(endpoint) {
            @Override
            public void onHospitalCostRequestArrived(HospitalCostsRequest request) {
                HospitalListLine listLine = new HospitalListLine(request, null);
                lvRequestReply.getItems().add(listLine);
                lvRequestReply.refresh();
            }
        };
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String fullAddress = this.address.getStreet() + " " + this.address.getNumber() + ", " + this.address.getCity();
        this.lbAddress.setText(fullAddress );
        this.lbHospital.setText(this.hospitalName);

        btnSendReply.setOnAction(event -> {
            sendHospitalReply();
        });
    }

    @FXML
    public void sendHospitalReply(){
        HospitalListLine listLine = this.lvRequestReply.getSelectionModel().getSelectedItem();
        if (listLine != null) {
            double price = Double.parseDouble(tfPrice.getText());
            HospitalCostsReply reply = new HospitalCostsReply(price, this.hospitalName, this.address);
            listLine.setReply(reply);
            lvRequestReply.refresh();
            gateway.sendHospitalCostReply(reply,listLine.getRequest());
        }
    }
}
