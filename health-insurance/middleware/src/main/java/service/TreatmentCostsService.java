package service;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TreatmentCostsService {

    public model.TreatmentCostsReply connectToTransportCostService(model.TreatmentCostsReply reply) {
        URI baseUri = UriBuilder.fromUri("http://localhost:8080/transportprice/rest/price").build();
        Client client = ClientBuilder.newClient(new ClientConfig());
        WebTarget target = client.target(baseUri);
        Invocation.Builder requestBuilder = target.request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            double entity = response.readEntity(double.class);
            reply.setTransportPrice(reply.getTransportPrice() * entity);
        }
        return reply;
    }
}
