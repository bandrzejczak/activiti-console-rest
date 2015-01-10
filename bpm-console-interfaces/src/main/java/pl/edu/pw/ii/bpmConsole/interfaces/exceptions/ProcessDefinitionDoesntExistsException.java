package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class ProcessDefinitionDoesntExistsException extends ProcessEngineException {
    public ProcessDefinitionDoesntExistsException(String processDefinitionId) {
        super("Process definition with id " + processDefinitionId + " doesn't exist!");
    }
}
