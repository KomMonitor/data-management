package de.hsbo.kommonitor.datamanagement.api.impl.webservice;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "wmsconnectiondetails")
@DiscriminatorValue("0")
public class WmsConnectionDetailsEntity extends ConnectionDetailsEntity {
    @Column(columnDefinition="text")
    private String baseUrl = null;

    @Column(columnDefinition="text")
    private String layerName = null;

    public WmsConnectionDetailsEntity() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }
}
