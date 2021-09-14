package com.predrag.a.feedservice.service.impl;

import com.predrag.a.feedservice.client.AuthServiceClient;
import com.predrag.a.feedservice.client.dto.JwtAuthenticationResponse;
import com.predrag.a.feedservice.client.dto.ServiceLoginRequest;
import com.predrag.a.feedservice.client.dto.UserSummaryResponse;
import com.predrag.a.feedservice.exception.UnableToGetAccessTokenException;
import com.predrag.a.feedservice.exception.UnableToGetUsersException;
import com.predrag.a.feedservice.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class DefaultAuthService implements AuthService {

    private static final String AUTH_TOKEN_TEMPLATE = "%s %s";

    private final AuthServiceClient authServiceClient;
    private final ServiceLoginRequest serviceLoginRequest;

    @Autowired
    public DefaultAuthService(final AuthServiceClient authServiceClient, final ServiceLoginRequest serviceLoginRequest) {
        this.authServiceClient = authServiceClient;
        this.serviceLoginRequest = serviceLoginRequest;
    }

    @Override
    public String getAccessToken() {
        final ResponseEntity<JwtAuthenticationResponse> response = authServiceClient.signin(serviceLoginRequest);
        final JwtAuthenticationResponse jwtResponse = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || jwtResponse == null) {
            final String exceptionMsg = String.format("Unable to get access token for service account, %s",
                    response.getStatusCode());
            log.error(exceptionMsg);
            throw new UnableToGetAccessTokenException(exceptionMsg);
        }

        return String.format(AUTH_TOKEN_TEMPLATE, jwtResponse.tokenType(), jwtResponse.accessToken());
    }

    @Override
    public Map<String, String> getUserProfilePics(final String token, final List<String> usernames) {
        final ResponseEntity<List<UserSummaryResponse>> response =
                authServiceClient.findByUsernameIn(token, usernames);
        final List<UserSummaryResponse> userSummaries = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || userSummaries == null) {
            final String message = String.format("Unable to get user summaries %s",
                    response.getStatusCode());

            log.error(message);
            throw new UnableToGetUsersException(message);
        }

        return userSummaries.stream()
                .filter(DefaultAuthService::profilePictureAndUsernameAreNotNull)
                .collect(toMap(UserSummaryResponse::username, UserSummaryResponse::profilePicture));
    }

    private static Boolean profilePictureAndUsernameAreNotNull(final UserSummaryResponse userSummaryResponse) {
        return StringUtils.isNotBlank(userSummaryResponse.profilePicture()) &&
                StringUtils.isNotBlank(userSummaryResponse.username());
    }
}
