package de.hsbo.kommonitor.datamanagement.api.impl;

import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

import java.util.Set;

/**
 * Implemented by all Entities that implement access control defined by Roles
 */
public interface RestrictedByRole {

    Set<RolesEntity> getRoles();
}
