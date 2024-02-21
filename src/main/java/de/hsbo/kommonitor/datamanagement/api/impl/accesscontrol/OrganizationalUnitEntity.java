package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.UUID;

@Entity(name = "OrganizationalUnits")
public class OrganizationalUnitEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String organizationalUnitId = null;

    @Column(nullable = false, unique = true)
    public String name;

    @Column(nullable = false, unique = true)
    public UUID keycloakId;

    @Column(nullable = false)
    public boolean isMandant;

    @Column(nullable = false)
    public String contact;

    @Column(nullable = true)
    public String description;

    @OneToMany(mappedBy = "organizationalUnit", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    public List<PermissionEntity> permissions;

    @ManyToOne
    @JoinColumn(name = "parent")
    public OrganizationalUnitEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    public List<OrganizationalUnitEntity> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(UUID keycloakId) {
        this.keycloakId = keycloakId;
    }

    public boolean isMandant() {
        return isMandant;
    }

    public void setMandant(boolean mandant) {
        isMandant = mandant;
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

    public List<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    public String getOrganizationalUnitId() {
        return organizationalUnitId;
    }

    public void setOrganizationalUnitId(String organizationalUnitId) {
        this.organizationalUnitId = organizationalUnitId;
    }

    public OrganizationalUnitEntity getParent() {
        return parent;
    }

    public void setParent(OrganizationalUnitEntity parent) {
        this.parent = parent;
    }

    public List<OrganizationalUnitEntity> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationalUnitEntity> children) {
        this.children = children;
    }
}