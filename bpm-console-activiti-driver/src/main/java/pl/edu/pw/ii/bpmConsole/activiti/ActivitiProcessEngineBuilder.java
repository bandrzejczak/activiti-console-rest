package pl.edu.pw.ii.bpmConsole.activiti;

import pl.edu.pw.ii.bpmConsole.interfaces.ProcessEngine;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessEngineBuilder;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessEngineInitializationException;

import javax.sql.DataSource;

public class ActivitiProcessEngineBuilder implements ProcessEngineBuilder {

    private DataSource dataSource;
    private String databaseType;

    @Override
    public ProcessEngineBuilder usingDatabase(String databaseType, DataSource dataSource) {
        this.databaseType = databaseType;
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public ProcessEngine build() {
        try {
            ProcessEngine processEngine = new ActivitiProcessEngine(databaseType, dataSource);
            processEngine.init();
            return processEngine;
        } catch (Throwable t) {
            throw new ProcessEngineInitializationException(t);
        }
    }
}
