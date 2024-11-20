package de.hsbo.kommonitor.datamanagement.config.db;

public interface DbInitLoader {
    void load();

    String getDbVersion();
}
