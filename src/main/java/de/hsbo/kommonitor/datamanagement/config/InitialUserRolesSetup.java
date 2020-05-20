//package de.hsbo.kommonitor.datamanagement.config;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import de.hsbo.kommonitor.datamanagement.api.impl.privileges.PrivilegesRepository;
//import de.hsbo.kommonitor.datamanagement.api.impl.roles.RolesRepository;
//import de.hsbo.kommonitor.datamanagement.api.impl.users.UsersRepository;
//import de.hsbo.kommonitor.datamanagement.model.privilege.PrivilegesEntity;
//import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;
//import de.hsbo.kommonitor.datamanagement.model.users.UsersEntity;
//
//@Component
//public class InitialUserRolesSetup implements ApplicationListener<ContextRefreshedEvent> {
//
//	Logger logger = LoggerFactory.getLogger(InitialUserRolesSetup.class);
//
//	boolean alreadySetup = false;
//
//	@Autowired
//	private UsersRepository userRepository;
//
//	@Autowired
//	private RolesRepository roleRepository;
//
//	@Autowired
//	private PrivilegesRepository privilegeRepository;
//
//	@Autowired
//	private BCryptPasswordEncoder passwordEncoder;
//
//	@Override
//	@Transactional
//	public void onApplicationEvent(ContextRefreshedEvent event) {
//
//		logger.info("Begin initial setup of default privileges, roles and users.");
//
//		if (alreadySetup)
//			return;
//		PrivilegesEntity readPrivilegesEntity = createPrivilegesEntityIfNotFound(
//				InitialAuthenticationSetupConstants.INITIAL_READ_PRIVILEGE_NAME);
//		PrivilegesEntity writePrivilegesEntity = createPrivilegesEntityIfNotFound(
//				InitialAuthenticationSetupConstants.INITIAL_WRITE_PRIVILEGE_NAME);
//
//		List<PrivilegesEntity> adminPrivilegesEntitys = Arrays.asList(readPrivilegesEntity, writePrivilegesEntity);
//		createRolesEntityIfNotFound(InitialAuthenticationSetupConstants.INITIAL_ADMIN_ROLE_NAME,
//				adminPrivilegesEntitys);
//		createRolesEntityIfNotFound(InitialAuthenticationSetupConstants.INITIAL_USER_ROLE_NAME,
//				Arrays.asList(readPrivilegesEntity));
//
//		/*
//		 * only create admin user when it is not yet existant
//		 * 
//		 * Hence first check, if privileges, roles and users are already
//		 * defined!
//		 */
//
//		if (!userRepository.existsByUserName(InitialAuthenticationSetupConstants.INITIAL_ADMIN_USER_NAME)) {
//			RolesEntity adminRolesEntity = roleRepository
//					.findByRoleName(InitialAuthenticationSetupConstants.INITIAL_ADMIN_ROLE_NAME);
//			UsersEntity user = new UsersEntity();
//			user.setUserName(InitialAuthenticationSetupConstants.INITIAL_ADMIN_USER_NAME);
//			user.setPassword(passwordEncoder.encode(InitialAuthenticationSetupConstants.INITIAL_ADMIN_PASSWORD));
//			user.setUserRoles(Arrays.asList(adminRolesEntity));
//			userRepository.save(user);
//		}
//
//		alreadySetup = true;
//
//		logger.info(
//				"Initial setup of default privileges, roles and users was succesfull. Created Admin role with name '{}' and password '{}'",
//				InitialAuthenticationSetupConstants.INITIAL_ADMIN_ROLE_NAME,
//				InitialAuthenticationSetupConstants.INITIAL_ADMIN_PASSWORD);
//	}
//
//	@Transactional
//	private PrivilegesEntity createPrivilegesEntityIfNotFound(String name) {
//
//		PrivilegesEntity privilege = privilegeRepository.findByPrivilegeName(name);
//		if (privilege == null) {
//			privilege = new PrivilegesEntity();
//			privilege.setPrivilegeName(name);
//			privilegeRepository.save(privilege);
//		}
//		return privilege;
//	}
//
//	@Transactional
//	private RolesEntity createRolesEntityIfNotFound(String name, Collection<PrivilegesEntity> privileges) {
//
//		RolesEntity role = roleRepository.findByRoleName(name);
//		if (role == null) {
//			role = new RolesEntity();
//			role.setRoleName(name);
//			role.setPrivileges(privileges);
//			roleRepository.save(role);
//		}
//		return role;
//	}
//
//}
