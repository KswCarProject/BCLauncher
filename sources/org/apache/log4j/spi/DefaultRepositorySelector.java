package org.apache.log4j.spi;

public class DefaultRepositorySelector implements RepositorySelector {
    final LoggerRepository repository;

    public DefaultRepositorySelector(LoggerRepository repository2) {
        this.repository = repository2;
    }

    public LoggerRepository getLoggerRepository() {
        return this.repository;
    }
}
