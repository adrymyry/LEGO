package es.um.lego;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/career")
public class OAuthClientCareer extends HttpServlet {

	private Map<String, String> tokens = new HashMap<String, String>();
	private Map<String, Date> expireTokensDate = new HashMap<String, Date>();

	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String queryParams = request.getQueryString();

		try {

			if (queryParams == null || (queryParams != null && !queryParams.contains("code"))) {

				//Genera el Oauth request para solicitar el Authorization code if user use the link
				OAuthClientRequest req = Common.getoAuthCodeClientRequest(Common.CODE_SERVER_ENDPOINT, Common.CLIENT_APPS_CAREER, Common.CLIENT_CAREER_ID, Common.SCOPE_CAREER_DATA);

				generateHTMLWithoutOAuth(response, req.getLocationUri());

				return;

			} else {

				String[] params = queryParams.split("&");
				String code = null;
				for (String param : params) {
					if (param.contains("code")) {
						code = param.split("=")[1];
					}
				}

				String token = tokens.get(code);

				OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

				if (token == null || isExpire(token)) {

					token = obtainToken(code, oAuthClient);

				}

				OAuthResourceResponse resourceResponse = getResourceResponse(token, oAuthClient);
				JSONObject resource = new JSONObject(resourceResponse.getBody());

				generateHTMLWithOauth(response, resource);

			}
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}
	}

	private void generateHTMLWithOauth (HttpServletResponse response, JSONObject resource) throws IOException {

		response.setContentType("text/html");
		response.getWriter().append("<h1>Career app</h1>");
		response.getWriter().append("<p>Main hability: " + resource.getString("mainHability") + "</p>");
		response.getWriter().append("<h4>Last Projects</h4>");

		JSONArray projects = resource.getJSONArray("projects");

		for (int i = 0; i < projects.length(); i++) {
			JSONObject project = projects.getJSONObject(i);
			response.getWriter().append("<p>Title: " + project.getString("title") + "</p>");
			response.getWriter().append("<p>Description: " + project.getString("description") + "</p>");
		}
	}

	private void generateHTMLWithoutOAuth (HttpServletResponse response, String oAuthCodeLocation) throws IOException {

		response.setContentType("text/html");
		response.getWriter().append("<h1>Career app</h1>");
		response.getWriter().append("<h2>Check career info of your workers</h2>");
//		response.getWriter().append("<div>");
//		response.getWriter().append("<input type=\"text\" value=\"ColorSchema\"/> ");
//		response.getWriter().append("<input type=\"text\" value=\"Font Size\"/> ");
//		response.getWriter().append("<input type=\"text\" value=\"Font Family\"/> ");
//		response.getWriter().append("</div>");
		response.getWriter().append("<br/><a href=" + oAuthCodeLocation + "> Check how he/she works at FB </a>");
	}

	private OAuthResourceResponse getResourceResponse (String token, OAuthClient oAuthClient) throws OAuthSystemException, OAuthProblemException {

		OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(Common.RESOURCE_CAREER_SERVER_ENDPOINT).setAccessToken(token).buildQueryMessage();
		return oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
	}

	private boolean isExpire (String token) {

		return new Date(System.currentTimeMillis()).before(expireTokensDate.get(token));
	}

	private String obtainToken (String code, OAuthClient oAuthClient) throws OAuthSystemException, OAuthProblemException {

            OAuthClientRequest req = Common.getoAuthTokenClientRequest(Common.TOKEN_SERVER_ENDPOINT, Common.CLIENT_CAREER_ID, Common.CLIENT_CAREER_SECRET, code);

            //Recibimos la respuesta con el access token
            OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(req);

            String token = oAuthResponse.getAccessToken();
            Date expireDate = new Date(System.currentTimeMillis() + (oAuthResponse.getExpiresIn() * 1000));

            tokens.put(code, token);
            expireTokensDate.put(token, expireDate);

		return token;
	}

}
