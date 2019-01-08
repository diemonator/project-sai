package gateway;

import model.HospitalCostsReply;
import model.HospitalCostsRequest;

import java.util.ArrayList;

public class Aggregator {

    private ArrayList<HospitalCostsReply> requests;
    private int id;
    private int size;

    public Aggregator(int id) {
        this.id = id;
        this.requests = new ArrayList<>();
        this.size = 0;
    }

    public void addToAggregatorList(HospitalCostsReply item) {
        requests.add(item);
    }

    public HospitalCostsReply getLowestHospitalOffer() {
        int index = 0;
        double min = requests.get(index).getPrice();
        for (int i = 0; i < size; i++) {
            double price = requests.get(i).getPrice();
            if (price < min) {
                min = price;
                index = i;
            }
        }
        return requests.get(index);
    }

    public int getId() {
        return id;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public int currentSize() {
        return requests.size();
    }
}
