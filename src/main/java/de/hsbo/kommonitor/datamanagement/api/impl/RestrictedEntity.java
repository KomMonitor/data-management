package de.hsbo.kommonitor.datamanagement.api.impl;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;

import java.util.Set;

/**
 * Implemented by all Entities that implement access control
 */
public interface RestrictedEntity {

    OrganizationalUnitEntity getOwner();

    Set<PermissionEntity> getPermissions();

    Boolean isPublic();
}
