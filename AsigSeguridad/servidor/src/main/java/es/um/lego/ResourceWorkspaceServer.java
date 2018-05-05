package es.um.lego;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/workspace")
public class ResourceWorkspaceServer extends HttpServlet{

    private HashMap<String, JSONObject> resources;


    public ResourceWorkspaceServer () {
        super();
        resources = new HashMap<String, JSONObject>();
        resources.put("adrian", new JSONObject()
                .put("colorSchema", "perun")
                .put("font", new JSONObject()
                        .put("size", "14pt")
                        .put("family", "Arial")
                ));
        resources.put("jorge", new JSONObject()
                .put("colorSchema", "monokai")
                .put("font", new JSONObject()
                        .put("size", "12pt")
                        .put("family", "Roboto")
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
            if (true) {

                // Return the resource
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType(OAuth.ContentType.JSON);
                JSONObject resource = resources.get("adrian");
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