package middleware;

import model.HospitalCostsReply;
import model.HospitalCostsRequest;
import model.TreatmentCostsRequest;

/**
 * This class is an item/line for a ListView. It makes it possible to put both BankInterestRequest and BankInterestReply object in one item in a ListView.
 */
class ListViewLine {
	
	private TreatmentCostsRequest treatmentCostsRequest;
	private HospitalCostsReply hospitalCostsReply;
	private HospitalCostsRequest hospitalCostsRequest;

	public ListViewLine(TreatmentCostsRequest treatmentCostsRequest) {
		setTreatmentCostsRequest(treatmentCostsRequest);
		setHospitalCostsReply(null);
	}

	public TreatmentCostsRequest getTreatmentCostsRequest() {
		return treatmentCostsRequest;
	}
	
	private void setTreatmentCostsRequest(TreatmentCostsRequest treatmentCostsRequest) {
		this.treatmentCostsRequest = treatmentCostsRequest;
	}

	public void setHospitalCostsReply(HospitalCostsReply hospitalCostsReply) {
		this.hospitalCostsReply = hospitalCostsReply;
	}

	public HospitalCostsRequest getHospitalCostsRequest() {
		return hospitalCostsRequest;
	}

	public void setHospitalCostsRequest(HospitalCostsRequest hospitalCostsRequest) {
		this.hospitalCostsRequest = hospitalCostsRequest;
	}

	@Override
	public String toString() {
	   return treatmentCostsRequest.toString() + "  --->  " + ((hospitalCostsReply !=null)? hospitalCostsReply.toString():"waiting for loan reply...");
	}
	
}
