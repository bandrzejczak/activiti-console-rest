package pl.edu.pw.ii.BpmConsole.Rest.tests.asserts;

import com.sun.jersey.api.client.ClientResponse;
import org.fest.assertions.api.AbstractAssert;

import static org.junit.Assert.assertTrue;

public class ClientResponseAssert<T extends ClientResponseAssert<T>> extends AbstractAssert<ClientResponseAssert<T>, ClientResponse> {

    ClientResponse actual;

    public ClientResponseAssert(ClientResponse actual) {
        super(actual, ClientResponseAssert.class);
        this.actual = actual;
    }

    public static ClientResponseAssert assertThat(ClientResponse response) {
        return new ClientResponseAssert(response);
    }

    public T containsHeader(String name, String value){
        assertTrue(containsHeader(name));
        assertTrue(containsHeaderValue(name, value));
        return (T) this;
    }

    private boolean containsHeader(String name) {
        return actual
                .getHeaders()
                .containsKey(name);
    }

    private boolean containsHeaderValue(String name, String value) {
        return actual
                .getHeaders()
                .get(name)
                .stream()
                .anyMatch(value::equals);
    }

}
