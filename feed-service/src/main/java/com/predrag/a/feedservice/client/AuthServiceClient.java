package com.predrag.a.feedservice.client;

import com.predrag.a.feedservice.client.dto.JwtAuthenticationResponse;
import com.predrag.a.feedservice.client.dto.ServiceLoginRequest;
import com.predrag.a.feedservice.client.dto.UserSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient("${dependencies.auth.service.url}")
public interface AuthServiceClient {

    @PostMapping("/auth")
    ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody ServiceLoginRequest request);

    @PostMapping("/users/summary/in")
    ResponseEntity<List<UserSummaryResponse>> findByUsernameIn(
            @RequestHeader("Authorization") String token,
            @RequestBody List<String> usernames);
}
