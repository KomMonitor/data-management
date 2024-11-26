package de.hsbo.kommonitor.datamanagement.api.impl.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.UserInfoApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.auth.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.auth.AuthInfoProviderFactory;
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
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class UserInfoController extends BasePathController implements UserInfoApi {

    private static Logger LOG = LoggerFactory.getLogger(UserInfoController.class);

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
    public ResponseEntity<UserInfoOverviewType> getUserInfoById(String userInfoId) {
        LOG.debug("Received request to get user info for id '{}'", userInfoId);
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
        return null;
    }

    @Override
    @PreAuthorize("isAuthorizedForAdminOperations()")
    public ResponseEntity<List<UserInfoOverviewType>> getUserInfos() {
        return null;
    }

    @Override
    @PreAuthorize("isAuthorizedForUserInfo(#userInfoId)")
    public ResponseEntity<UserInfoOverviewType> updateUserInfo(String userInfoId, UserInfoInputType userInfoData) {
        return null;
    }
}