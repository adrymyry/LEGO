package es.um.lego;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Set;

/**
 * Servlet implementation class ServAutorizacionCode
 */
@WebServlet("/token")
public class ServAccessToken extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServAccessToken () {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());

    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	
    	//Este metodo se va a encargar de proporcionar el Access token
    	System.out.println("Aqui se debe gestionar el Access Token");

		OAuthTokenRequest oauthRequest = null;
		String redirectURI="";


		try {

			//Recepcion del oauthRequest
			oauthRequest = new OAuthTokenRequest(request);

			//Direccion de respuesta
			redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

			//Aqui hay que implementar la validacion de que la aplicacion cliente ha sido registrada previamente
			//y presenta un client secret correcto
			String clientId = oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID);
			String clientSecret = oauthRequest.getParam(OAuth.OAUTH_CLIENT_SECRET);
			if (!Common.existsClient(clientId) && !Common.checkClientSecret(clientId, clientSecret)) {
				System.out.println("Invalid client");
				response.sendError(401, "Invalid client");
			} else {

				String code = oauthRequest.getParam(OAuth.OAUTH_CODE);
				AuthInfo authInfo = Common.authorizationCodes.get(code);

				if (authInfo == null) {

					response.sendError(401, "Invalid code");
				} else {

					//Genera Access token
					OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

					String accessToken = oauthIssuerImpl.accessToken();
					authInfo.lastToken = accessToken;
					authInfo.expiresLastToken = new Date(System.currentTimeMillis() + 3600 * 1000);

					Common.accessTokens.put(accessToken, code);

					//Genera una respuesta con el Access Token response
					OAuthResponse resp = OAuthASResponse.tokenResponse(200)
							.location(redirectURI)
							.setTokenType(OAuth.DEFAULT_TOKEN_TYPE.toString())
							.setAccessToken(accessToken)
							.setExpiresIn("3600")
							.buildJSONMessage();

					System.out.println("Genera OauthResponse");

					System.out.println(resp.getResponseStatus()+"--"+resp.getLocationUri());

					//Envia la respuesta

					response.setStatus(resp.getResponseStatus());
					response.setContentType(OAuth.ContentType.JSON);
					PrintWriter pw = response.getWriter();
					pw.print(resp.getBody());
					pw.flush();
					pw.close();
				}

			}


		} catch (OAuthProblemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
