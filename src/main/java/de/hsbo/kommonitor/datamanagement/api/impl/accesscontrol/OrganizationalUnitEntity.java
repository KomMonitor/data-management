package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import de.hsbo.kommonitor.datamanagement.model.AdminRoleType;
import de.hsbo.kommonitor.datamanagement.model.GroupAdminRolesType;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity(name = "OrganizationalUnits")
public class OrganizationalUnitEntity {

    @Id
    @UuidGenerator
    private String organizationalUnitId = null;

    @Column(nullable = false, unique = true)
    public String name;

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
    @JoinColumn(name = "mandant", nullable = true)
    public OrganizationalUnitEntity mandant;

    @ManyToOne
    @JoinColumn(name = "parent")
    public OrganizationalUnitEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    public List<OrganizationalUnitEntity> children;

    @Transient
    private List<AdminRoleType> userAdminRoles;

    @Transient
    private List<GroupAdminRolesType> adminRoles;

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

    public void setIsMandant(boolean mandant) {
        isMandant = mandant;
    }

    public OrganizationalUnitEntity getMandant() {
        return mandant;
    }

    public void setMandant(OrganizationalUnitEntity mandant) {
        this.mandant = mandant;
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

    // Gets all descendants (children, grandchildren, grandgrandchildren, etc.)
    public List<OrganizationalUnitEntity> getDescendants() {
        List<OrganizationalUnitEntity> collect = new LinkedList<>();
        collect.add(this);
        collect.addAll(children.stream()
                .flatMap(child -> child.getDescendants().stream())
                .toList());
        return collect;
    }

    public void setChildren(List<OrganizationalUnitEntity> children) {
        this.children = children;
    }

    public List<AdminRoleType> getUserAdminRoles() {
        return userAdminRoles;
    }

    public void setUserAdminPermissions(List<AdminRoleType> userAdminRoles) {
        this.userAdminRoles = userAdminRoles;
    }
}