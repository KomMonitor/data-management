package de.hsbo.kommonitor.datamanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan("de.hsbo.kommonitor")
@EntityScan( basePackages = {"de.hsbo.kommonitor"} )
public class KomMonitorManagementRunner extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(KomMonitorManagementRunner.class, args);
    }
}