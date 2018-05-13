package es.um.lego;

import java.util.Date;
import java.util.Set;

class AuthInfo {
    String redirectUri;
    Set<String> scopes;
    String username;
    String lastToken;
    Date expiresLastToken;

    public AuthInfo (String redirectUri, Set<String> scopes) {

        this.redirectUri = redirectUri;
        this.scopes = scopes;
    }
}
