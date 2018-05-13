package es.um.lego;

import java.util.*;

public class Common {

    private static final String CLIENT_RRHH_ID = "client_rrhh_id";
    private static final String CLIENT_RRHH_SECRET = "client_rrhh_secret";
    private static final String SCOPE_PERSONAL_DATA = "personal";

    private static final String CLIENT_WORKSPACE_ID = "client_workspace_id";
    private static final String CLIENT_WORKSPACE_SECRET = "client_workspace_secret";
    private static final String SCOPE_WORKSPACE = "workspace";

    private static final String CLIENT_CAREER_ID = "client_career_id";
    private static final String CLIENT_CAREER_SECRET = "client_career_secret";
    private static final String SCOPE_CAREER_DATA = "career";

    private static final HashMap<String, String> secrets = new HashMap<String, String>();

    static {
        secrets.put(CLIENT_RRHH_ID, CLIENT_RRHH_SECRET);
        secrets.put(CLIENT_CAREER_ID, CLIENT_CAREER_SECRET);
        secrets.put(CLIENT_WORKSPACE_ID, CLIENT_CAREER_SECRET);
    }

    private static final HashMap<String, Set<String>> scopes = new HashMap<String, Set<String>>();

    static {

        Set<String> rrhhScopes = new HashSet<String>();
        rrhhScopes.add(SCOPE_PERSONAL_DATA);
        scopes.put(CLIENT_RRHH_ID, rrhhScopes);

        Set<String> workspaceScopes = new HashSet<String>();
        workspaceScopes.add(SCOPE_WORKSPACE);
        scopes.put(CLIENT_WORKSPACE_ID, workspaceScopes);

        Set<String> careerScopes = new HashSet<String>();
        careerScopes.add(SCOPE_CAREER_DATA);
        scopes.put(CLIENT_CAREER_ID, careerScopes);
    }

    public static final String USERNAME_ADRI = "adrian";
    private static final String PASSWORD_ADRI = "12345";

    public static final String USERNAME_JORGE = "jorge";
    private static final String PASSWORD_JORGE = "12345";

    private static final HashMap<String, String> credentials = new HashMap<String, String>();

    static {
        credentials.put(USERNAME_ADRI, PASSWORD_ADRI);
        credentials.put(USERNAME_JORGE, PASSWORD_JORGE);
    }

    public static Map<String, AuthInfo> authorizationCodes = new HashMap<String, AuthInfo>();
    public static Map<String, String> accessTokens = new HashMap<String, String>();

    public static boolean existsClient (String clientId) {

        return secrets.containsKey(clientId);
    }

    public static boolean validateScopes (String clientId, Set<String> scopes) {

        Set<String> clientScopes = Common.scopes.get(clientId);

        for (String scope : scopes) {
            if (!clientScopes.contains(scope)) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkClientSecret (String clientId, String clientSecret) {

        return secrets.get(clientId).equals(clientSecret);
    }

    public static boolean login (String username, String password) {
        String truePass = credentials.get(username);
        return password.equals(truePass);
    }


}
