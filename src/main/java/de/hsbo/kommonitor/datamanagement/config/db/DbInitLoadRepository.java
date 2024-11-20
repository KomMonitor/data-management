package de.hsbo.kommonitor.datamanagement.config.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class DbInitLoadRepository {
    @Autowired
    private List<DbInitLoader> initLoaderList;

    public Optional<DbInitLoader> getDbInitLoader(String dbVersion) {
        Optional<DbInitLoader> converterOpt = this.initLoaderList.stream()
                .filter(i -> i.getDbVersion().equals(dbVersion))
                .findFirst();
        return converterOpt;
    }

    public List<DbInitLoader> getAll() {
        return Collections.unmodifiableList(this.initLoaderList);
    }
}
