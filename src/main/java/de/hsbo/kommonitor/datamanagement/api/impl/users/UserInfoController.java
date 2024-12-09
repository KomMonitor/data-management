package de.hsbo.kommonitor.datamanagement.api.impl.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.UserInfoApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProviderFactory;
import de.hsbo.kommonitor.datamanagement.model.UserInfoInputType;
import de.hsbo.kommonitor.datamanagement.model.UserInfoOverviewType;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class UserInfoController extends BasePathController implements UserInfoApi {

    private static final Logger LOG = LoggerFactory.getLogger(UserInfoController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserInfoManager userInfoManager;

    @Autowired
    AuthInfoProviderFactory authInfoProviderFactory;

    @Autowired
    public UserInfoController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }


    @Override
    @PreAuthorize("hasRequiredPermissionLevel('viewer')")
    public ResponseEntity<UserInfoOverviewType> addUserInfo(UserInfoInputType userInfoInputType) {
        LOG.info("Received request to insert new additional user infos");
        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        UserInfoOverviewType userInfo;
        try {
            userInfo = userInfoManager.addUserInfo(userInfoInputType, provider);
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);
        }

        if (userInfo != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            String location = userInfo.getUserInfoId();
            try {
                responseHeaders.setLocation(new URI(location));
            } catch (URISyntaxException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }
            return new ResponseEntity<>(userInfo, responseHeaders, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForUserInfo(#userInfoId)")
    public ResponseEntity deleteUserInfo(@P("userInfoId") String userInfoId) {
        LOG.info("Received request to delete user info for id '{}'", userInfoId);
        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {
            boolean isDeleted = userInfoManager.deleteUserInfosById(userInfoId, provider);

            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForUserInfo(#userInfoId)")
    public ResponseEntity<UserInfoOverviewType> getUserInfoById(@P("userInfoId") String userInfoId) {
        LOG.info("Received request to get user info for id '{}'", userInfoId);
        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        String accept = request.getHeader("Accept");

        try {
            if (accept != null && accept.contains("application/json")) {
                UserInfoOverviewType userInfo = userInfoManager.getUserInfoByUserInfoId(userInfoId, provider);
                return new ResponseEntity<>(userInfo, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            return ApiUtils.createResponseEntityFromException(ex);
        }
    }

    @Override
    @PreAuthorize("hasRequiredPermissionLevel('viewer')")
    public ResponseEntity<UserInfoOverviewType> getUserInfoForUser() {
        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        String userId = provider.getUserId();
        LOG.info("Received request to get user info for current user with Keycloak ID '{}'", userId);

        String accept = request.getHeader("Accept");

        try {
            if (accept != null && accept.contains("application/json")) {
                UserInfoOverviewType userInfo = userInfoManager.getUserInfoByKeycloakId(userId, provider);
                return new ResponseEntity<>(userInfo, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            return ApiUtils.createResponseEntityFromException(ex);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForAdminOperations()")
    public ResponseEntity<List<UserInfoOverviewType>> getUserInfos() {
        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        String userId = provider.getUserId();
        LOG.info("Received request to get user info for current user with Keycloak ID '{}'", userId);

        String accept = request.getHeader("Accept");

        try {
            if (accept != null && accept.contains("application/json")) {
                List<UserInfoOverviewType> userInfoList = userInfoManager.getAllUserInfos();
                return new ResponseEntity<>(userInfoList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            return ApiUtils.createResponseEntityFromException(ex);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForUserInfo(#userInfoId)")
    public ResponseEntity<UserInfoOverviewType> updateUserInfo(@P("userInfoId")String userInfoId, UserInfoInputType userInfoData) {
        LOG.info("Received request to get user info for id '{}'", userInfoId);
        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        String accept = request.getHeader("Accept");

        try {
            if (accept != null && accept.contains("application/json")) {
                UserInfoOverviewType userInfo = userInfoManager.updateUserInfoByUserInfoId(userInfoId, userInfoData, provider);
                return new ResponseEntity<>(userInfo, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            return ApiUtils.createResponseEntityFromException(ex);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForUserInfo(#userInfoId)")
    public ResponseEntity<UserInfoOverviewType> updateUserInfoPartially(@P("userInfoId") String userInfoId, UserInfoInputType userInfoData) {
        LOG.info("Received request to get user info for id '{}'", userInfoId);
        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        String accept = request.getHeader("Accept");

        try {
            if (accept != null && accept.contains("application/json")) {
                UserInfoOverviewType userInfo = userInfoManager.updateUserPartiallyInfoByUserInfoId(userInfoId, userInfoData, provider);
                return new ResponseEntity<>(userInfo, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            return ApiUtils.createResponseEntityFromException(ex);
        }
    }
}
