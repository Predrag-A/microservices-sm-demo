package com.predrag.a.feedservice.service;

import java.util.List;
import java.util.Map;

public interface AuthService {

    String getAccessToken();

    Map<String, String> getUserProfilePics(String token, List<String> usernames);
}
