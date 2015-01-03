package pl.edu.pw.ii.bpmConsole.rest.tests.asserts;


import org.assertj.core.api.AbstractAssert;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertTrue;

public class ResponseAssert<T extends ResponseAssert<T>> extends AbstractAssert<ResponseAssert<T>, Response> {

    Response actual;

    public ResponseAssert(Response actual) {
        super(actual, ResponseAssert.class);
        this.actual = actual;
    }

    public static ResponseAssert assertThat(Response response) {
        return new ResponseAssert(response);
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
