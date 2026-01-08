package de.hsbo.kommonitor.datamanagement.api.impl.webservice;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.ServiceTypeEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

@Entity(name = "connectiondetails")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "serviceType", discriminatorType = DiscriminatorType.INTEGER)
public abstract class ConnectionDetailsEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private String id = null;

//    private ServiceTypeEnum serviceType = null;

    public ConnectionDetailsEntity() {
    }

    public String getId() {
        return id;
    }

}
