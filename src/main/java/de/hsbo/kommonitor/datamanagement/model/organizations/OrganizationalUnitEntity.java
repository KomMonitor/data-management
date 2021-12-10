package de.hsbo.kommonitor.datamanagement.model.organizations;

import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "OrganizationalUnits")
public class OrganizationalUnitEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String organizationalUnitId = null;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String contact;

    @Column(nullable = true)
    public String description;

    @OneToMany(mappedBy = "organizationalUnit", cascade = CascadeType.REMOVE)
    public List<RolesEntity> roles;

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

    public List<RolesEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesEntity> roles) {
        this.roles = roles;
    }

    public String getOrganizationalUnitId() {
        return organizationalUnitId;
    }

    public void setOrganizationalUnitId(String organizationalUnitId) {
        this.organizationalUnitId = organizationalUnitId;
    }
}
