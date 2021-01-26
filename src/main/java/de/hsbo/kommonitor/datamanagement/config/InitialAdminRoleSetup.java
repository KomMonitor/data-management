package de.hsbo.kommonitor.datamanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.hsbo.kommonitor.datamanagement.api.impl.roles.RolesRepository;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

@Component
public class InitialAdminRoleSetup implements ApplicationListener<ContextRefreshedEvent> {

	Logger logger = LoggerFactory.getLogger(InitialAdminRoleSetup.class);

	boolean alreadySetup = false;

	@Autowired
	private RolesRepository roleRepository;

	@Value("${keycloak.enabled:false}")
	private boolean isKeycloakEnabled;

	@Value("${kommonitor.roles.admin:administrator}")
	private String adminRoleName;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		logger.info("Begin initial setup of configured administrator role if keyloak is enabled.");

		if (!isKeycloakEnabled) {
			logger.info("Keyloak connection is disabled. Hence, no default admin user is registered.");
		} else {
			logger.info(
					"Keyloak connection is enabled. A default admin user with name {} will be registered if not already setup.",
					adminRoleName);

			createRolesEntityIfNotFound(adminRoleName);
		}

		logger.info("Initial setup of default admin role finished.");
	}

	@Transactional
	private RolesEntity createRolesEntityIfNotFound(String name) {

		RolesEntity role = roleRepository.findByRoleName(name);
		if (role == null) {
			role = new RolesEntity();
			role.setRoleName(name);
			roleRepository.save(role);
		}
		return role;
	}

}
