package es.um.lego;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@WebServlet("/career")
public class ResourceCareerServer extends HttpServlet{

    private HashMap<String, JSONObject> resources;


    public ResourceCareerServer () {
        super();
        resources = new HashMap<String, JSONObject>();
        resources.put(Common.USERNAME_ADRI, new JSONObject()
                .put("mainHability", "Microservices Architecture")
                .put("projects", new JSONArray()
                        .put(new JSONObject()
                                .put("title", "Hermenegildo Lumeras")
                                .put("description", "Emoji URL shortener"))
                        .put(new JSONObject()
                                .put("title", "Larla")
                                .put("description", "Messages application for not common people"))
                ));
        resources.put(Common.USERNAME_JORGE, new JSONObject()
                .put("mainHability", "Embedded Systems")
                .put("projects", new JSONArray()
                        .put(new JSONObject()
                                .put("title", "Hermenegildo Lumeras")
                                .put("description", "Emoji URL shortener"))
                        .put(new JSONObject()
                                .put("title", "Larla")
                                .put("description", "Messages application for not common people"))
                ));
    }

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Check context time to avoid accessing to this resource in bad hours
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        String actualTime = simpleDateFormat.format(calendar.getTime());

        if (actualTime.compareTo("09:00") == -1 || actualTime.compareTo("21.00") == 1) {
            resp.sendError(401, "Resource unavailable at " + actualTime);
        }

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

            } else if (!authInfo.scopes.contains("career")) {
                System.out.println("Trying to access to an invalid scope");
                resp.sendError(401, "Trying to access to an invalid scope");

            } else {

                String username = authInfo.username;

                // Return the resource
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType(OAuth.ContentType.JSON);
                JSONObject resource = resources.get(username);
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
