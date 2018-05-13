package es.um.lego;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

public class Common {

    public static final String CLIENT_RRHH_ID = "client_rrhh_id";
    public static final String CLIENT_RRHH_SECRET = "client_rrhh_secret";
    public static final String SCOPE_PERSONAL_DATA = "personal";

    public static final String CLIENT_WORKSPACE_ID = "client_workspace_id";
    public static final String CLIENT_WORKSPACE_SECRET = "client_workspace_secret";
    public static final String SCOPE_WORKSPACE = "workspace";

    public static final String CLIENT_CAREER_ID = "client_career_id";
    public static final String CLIENT_CAREER_SECRET = "client_career_secret";
    public static final String SCOPE_CAREER_DATA = "career";

    public static final String CODE_SERVER_ENDPOINT = "http://auth.fb.com:8889/ServAutorizacion/auth";
    public static final String TOKEN_SERVER_ENDPOINT = "http://auth.fb.com:8889/ServAutorizacion/token";
    public static final String RESOURCE_PERSONAL_SERVER_ENDPOINT = "http://api.fb.com:8889/ServAutorizacion/personal";
    public static final String RESOURCE_WORKSPACE_SERVER_ENDPOINT = "http://api.fb.com:8889/ServAutorizacion/workspace";
    public static final String RESOURCE_CAREER_SERVER_ENDPOINT = "http://api.fb.com:8889/ServAutorizacion/career";

    public static final String CLIENT_APPS_RRHH = "http://apps.ig.com:3000/InstaApps/rrhh";
    public static final String CLIENT_APPS_WORKSPACE = "http://apps.ig.com:3000/InstaApps/workspace";
    public static final String CLIENT_APPS_CAREER = "http://apps.ig.com:3000/InstaApps/career";



    public static OAuthClientRequest getoAuthCodeClientRequest (String authLocation, String redirectURI, String clietId, String scope) throws OAuthSystemException {

        return OAuthClientRequest
                .authorizationLocation(authLocation)
                .setRedirectURI(redirectURI)
                .setClientId(clietId)
                .setResponseType("code")
                .setScope(scope)
                .buildQueryMessage();
    }

    public static OAuthClientRequest getoAuthTokenClientRequest (String tokenLocation, String clietId, String clientSecret, String code, String redirectURI) throws OAuthSystemException {

        return OAuthClientRequest
                .tokenLocation(tokenLocation)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setRedirectURI(redirectURI)
                .setClientId(clietId)
                .setClientSecret(clientSecret)
                .setCode(code)
                .buildBodyMessage();
    }
}
