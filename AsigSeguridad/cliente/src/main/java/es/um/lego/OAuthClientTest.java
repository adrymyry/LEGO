package es.um.lego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.apache.oltu.oauth2.ext.dynamicreg.client.OAuthRegistrationClient;
//import org.apache.oltu.oauth2.ext.dynamicreg.client.request.OAuthClientRegistrationRequest;
//import org.apache.oltu.oauth2.ext.dynamicreg.client.response.OAuthClientRegistrationResponse;
//import org.apache.oltu.oauth2.ext.dynamicreg.common.OAuthRegistration;
//import org.apache.oltu.oauth2.integration.Common;
//import org.apache.oltu.oauth2.integration.CommonExt;

@WebServlet("/rrhh")
public class OAuthClientTest extends HttpServlet {

	private Map<String, String> tokens = new HashMap<String, String>();
	private Map<String, Date> expireTokensDate = new HashMap<String, Date>();

	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String queryParams = request.getQueryString();

		try {

			if (queryParams == null || (queryParams != null && !queryParams.contains("code"))) {

				//Genera el Oauth request para solicitar el Authorization code
				OAuthClientRequest req = OAuthClientRequest
						.authorizationLocation("http://localhost:8889/AsigSeguridad/ServAutorizacion")
						.setRedirectURI("http://localhost:3000/InstaApps/rrhh")
						.setClientId("id_rrhh")
						.setResponseType("code")
						.buildQueryMessage();

				response.setContentType("text/html");
				response.getWriter().append("<h1>RRHH app</h1>");
				response.getWriter().append("<h2>Welcome to Instagram!</h2>");
				response.getWriter().append("<h3>Tell us your personal data</h3>");
				response.getWriter().append("<div>");
				response.getWriter().append("<input type=\"text\" value=\"Name\"/> ");
				response.getWriter().append("<input type=\"text\" value=\"Payment type\"/> ");
				response.getWriter().append("<input type=\"text\" value=\"Payment address\"/> ");
				response.getWriter().append("</div>");
				response.getWriter().append("<br/><a href=" + req.getLocationUri() + "> I'm in a Hackamonth! </a>");
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

				response.setContentType("text/html");
				response.getWriter().append("<h1>RRHH app</h1>");
				response.getWriter().append("<p>Name: " + resource.getString("name") + "</p>");
				response.getWriter().append("<h4>Payment</h4>");
				response.getWriter().append("<p>Type: " + resource.getJSONObject("payment").getString("type") + "</p>");
				response.getWriter().append("<p>Address: " + resource.getJSONObject("payment").getString("account") + "</p>");

			}
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}
	}

	private OAuthResourceResponse getResourceResponse (String token, OAuthClient oAuthClient) throws OAuthSystemException, OAuthProblemException {

		OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest("http://server:8080/AsigSeguridad/resource").setAccessToken(token).buildQueryMessage();
		return oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
	}

	private boolean isExpire (String token) {

		return new Date(System.currentTimeMillis()).before(expireTokensDate.get(token));
	}

	private String obtainToken (String code, OAuthClient oAuthClient) throws OAuthSystemException, OAuthProblemException {

            OAuthClientRequest req = OAuthClientRequest
										.tokenLocation("http://server:8080/AsigSeguridad/ServAutorizacion")
										.setGrantType(GrantType.AUTHORIZATION_CODE)
										.setClientId("id_rrhh")
										.setClientSecret("3acb294b071c9aec86d60ae3daf32a93")
										.setRedirectURI("http://www.as.com")
										.setCode(code)
										.buildBodyMessage();

            //Recibimos la respuesta con el access token
            OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(req);

            String token = oAuthResponse.getAccessToken();
            Date expireDate = new Date(System.currentTimeMillis() + oAuthResponse.getExpiresIn());

            tokens.put(code, token);
            expireTokensDate.put(token, expireDate);

		return token;
	}


	public static void main(String[] args) throws OAuthSystemException, IOException {

		//Genear Request para el registro del cliente
		System.out.println("Falta implementar la generacion del request para el registro del cliente");
//		OAuthClientRequest request = OAuthClientRequest
//				.authorizationLocation("Http://localhost:8089/AsigSeguridad/ServAutorizacion")
//				.setClientId("131804060198305")
//				.setRedirectURI("http://localhost:8080/")
//				.buildQueryMessage();

		//Enviar Petici�n de registro al servidor de registro
		System.out.println("Falta implementar el envio del request para el registro del cliente");

		//Genera el Oauth request para solicitar el Authorization code
		OAuthClientRequest request = OAuthClientRequest
				.authorizationLocation("http://localhost:8889/AsigSeguridad/ServAutorizacion")
				.setRedirectURI("http://www.marca.com")
				.setClientId("id_prueba_registro")
				.setResponseType("code")
				.buildQueryMessage();

		//Dos alternativas para enviar la petici�n

		//1- Desde el navegador. Para realizar la solicitud hay que pegar en el navegador la uri indicada en la consola
		// IMPORTANTE: Revisad que la uri est� bien generada, a veces se incluyen caracteres extra�os
		System.out.println("Visita: " + request.getLocationUri() + "\n y copia/pega el c�digo de la uri aqui:");

		//2-Desde esta clase java
		//URL url = new URL(request.getLocationUri());
		//HttpURLConnection c = (HttpURLConnection) url.openConnection();


		//Procesar respuesta
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		//BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
		String response = in.readLine();
		in.close();

		//Respuesta con el Authorization Code
		System.out.println(response.toString());

		System.out.println("Hay que extraer el Authorization Code de la response anterior e integrarlo en los pasos posteriores");
		String code = response.substring(response.lastIndexOf("=") + 1);
		System.out.println(code);

		//Generamos el request para pedir el access token
		request = OAuthClientRequest
				.tokenLocation("http://localhost:8889/AsigSeguridad/ServAutorizacion")
				.setGrantType(GrantType.AUTHORIZATION_CODE)
				.setClientId("id_prueba_registro")
				.setClientSecret("3acb294b071c9aec86d60ae3daf32a93")
				.setRedirectURI("http://www.as.com")
				.setCode(code)
				.buildBodyMessage();

		//Recibimos la respuesta con el access token
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

		OAuthJSONAccessTokenResponse oAuthResponse = null;
		try {
			oAuthResponse = oAuthClient.accessToken(request);
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}

		//Respuesta con el Access Token
		System.out.println("Access Token: " + oAuthResponse.getAccessToken() + ", Expires in: " + oAuthResponse
				.getExpiresIn());

		//Realizamos la peticion de un recurso al servidor de recursos
		System.out.println("Falta implementar la comunicaci�n con el Resource Server para acceder al recurso deseado presentando el access token\n" + oAuthResponse.getParam("redirect_uri"));
		try {
			OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest("http://localhost:8889/AsigSeguridad/resource").setAccessToken(oAuthResponse.getAccessToken()).buildQueryMessage();
			OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
			System.out.println(resourceResponse.getBody());
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}
    }

}
