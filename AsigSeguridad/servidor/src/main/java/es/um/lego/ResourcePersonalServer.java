package es.um.lego;

import org.apache.oltu.oauth2.as.request.OAuthRequest;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

@WebServlet("/personal")
public class ResourcePersonalServer extends HttpServlet{

    private HashMap<String, JSONObject> resources;


    public ResourcePersonalServer () {
        super();
        resources = new HashMap<String, JSONObject>();
        resources.put(Common.USERNAME_ADRI, new JSONObject()
                .put("name", "Adrian Miralles")
                .put("payment", new JSONObject()
                        .put("type", "Paypal")
                        .put("account", "www.paypal.me/adrymyry")
                ));
        resources.put(Common.USERNAME_JORGE, new JSONObject()
                .put("name", "Jorge Gallego")
                .put("payment", new JSONObject()
                        .put("type", "bank transfer")
                        .put("account", "ES3475661593616856526088")
                ));
    }

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        try {
            // Make the OAuth Request out of this request
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(req,
                    ParameterStyle.QUERY);
            // Get the access token
            String accessToken = oauthRequest.getAccessToken();

            System.out.println(accessToken);
            // Validate the access token
            String code = Common.accessTokens.get(accessToken);
            AuthInfo authInfo = Common.authorizationCodes.get(code);

            if (authInfo == null) {
                System.out.println("Invalid access token");
                resp.sendError(401, "Invalid access token");

            } else if (authInfo.expiresLastToken.before(new Date())) {
                System.out.println("Access token is caducated");
                resp.sendError(401, "Access token is caducated");

            } else if (!authInfo.scopes.contains("personal")) {
                System.out.println("Trying to access to an invalid scope");
                resp.sendError(401, "Trying to access to an invalid scope");

            } else {

                String username = authInfo.username;

                // Return the resource
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType(OAuth.ContentType.JSON);
                JSONObject resource = resources.get(username);
                System.out.println("username = " + username + " " + resource.toString());
                PrintWriter pw = resp.getWriter();
                pw.print(resource.toString());
                pw.flush();
                pw.close();

            }

        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        }



    }
}
