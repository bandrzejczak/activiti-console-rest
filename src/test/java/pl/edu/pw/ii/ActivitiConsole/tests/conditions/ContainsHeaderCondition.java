package pl.edu.pw.ii.ActivitiConsole.tests.conditions;

import com.sun.jersey.api.client.ClientResponse;
import org.fest.assertions.core.Condition;

public class ContainsHeaderCondition extends Condition<ClientResponse> {

    private String name;
    private String value;

    @Override
    public boolean matches(ClientResponse response) {
        return containsHeader(response) && containsHeaderValue(response);
    }

    private boolean containsHeader(ClientResponse response) {
        return response
                .getHeaders()
                .containsKey(name);
    }

    private boolean containsHeaderValue(ClientResponse response) {
        return response
                .getHeaders()
                .get(name)
                .stream()
                .anyMatch(value::equals);
    }

    public static ContainsHeaderCondition containsHeader(String name, String value){
        ContainsHeaderCondition containsHeaderCondition = new ContainsHeaderCondition();
        containsHeaderCondition.name = name;
        containsHeaderCondition.value = value;
        return containsHeaderCondition;
    }
}
