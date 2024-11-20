package de.hsbo.kommonitor.datamanagement.config.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class MigrationSetup implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    private static final Logger LOG = LoggerFactory.getLogger(MigrationSetup.class);

    @Value("${kommonitor.migrations}")
    private String[] versionMigrationList;

    @Autowired
    DbInitLoadRepository dbInitLoadRepository;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Arrays.stream(versionMigrationList).forEach(v -> {
            Optional<DbInitLoader> initLoaderOpt = dbInitLoadRepository.getDbInitLoader(v);
            if(initLoaderOpt.isPresent()) {
                initLoaderOpt.get().load();
            } else {
                LOG.error("No DbInitLoader exists for version {}. Initial setup will be skipped.", v);
            }
        });
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
