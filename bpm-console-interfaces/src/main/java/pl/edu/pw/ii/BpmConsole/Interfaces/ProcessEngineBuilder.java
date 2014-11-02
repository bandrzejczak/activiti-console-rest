package pl.edu.pw.ii.BpmConsole.Interfaces;

import javax.sql.DataSource;

public interface ProcessEngineBuilder {

    public ProcessEngineBuilder usingDatabase(String databaseType, DataSource dataSource);

    public ProcessEngine build();

}
