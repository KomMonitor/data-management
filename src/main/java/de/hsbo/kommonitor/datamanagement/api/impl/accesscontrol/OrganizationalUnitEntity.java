package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity(name = "OrganizationalUnits")
public class OrganizationalUnitEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String organizationalUnitId = null;

    @Column(nullable = false, unique = true)
    public String name;

    @Column(nullable = false)
    public String contact;

    @Column(nullable = true)
    public String description;

    @OneToMany(mappedBy = "organizationalUnit", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    public List<PermissionEntity> roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PermissionEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<PermissionEntity> roles) {
        this.roles = roles;
    }

    public String getOrganizationalUnitId() {
        return organizationalUnitId;
    }

    public void setOrganizationalUnitId(String organizationalUnitId) {
        this.organizationalUnitId = organizationalUnitId;
    }
}
