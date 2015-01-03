package pl.edu.pw.ii.bpmConsole.interfaces;

import javax.sql.DataSource;

public interface ProcessEngineBuilder {

    public ProcessEngineBuilder usingDatabase(String databaseType, DataSource dataSource);

    public ProcessEngine build();

}
