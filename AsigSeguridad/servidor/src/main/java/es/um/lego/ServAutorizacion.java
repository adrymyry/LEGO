package main.java.es.um.lego;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

/**
 * Servlet implementation class ServAutorizacion
 */
@WebServlet("/ServAutorizacion")
public class ServAutorizacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServAutorizacion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		//Este metodo se va a encargar de proporcionar el Authorization Code
    	System.out.println("Entra en el doGet()");
    	System.out.println("Al arrancar da una excepcion invalid_request debido a que desde el navegador se esta invocando el doGet() sin pasarle los parametros necesarios al request, no preocuparos por esto, el servidor sigue escuchando peticiones");
		
    	OAuthAuthzRequest oauthRequest = null;
		String redirectURI="";
		
		try {
		
			//Recepcion del oauthRequest
			oauthRequest = new OAuthAuthzRequest(request);
			
			//Direccion de respuesta
            redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            
			//Aqui hay que implementar la validacion de que la aplicacion cliente ha sido registrada previamente
        	String clientId = oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID);
        	if (!clientId.equals("id_rrhh")) {
				System.out.println("Invalid client");
				response.sendError(401, "Invalid client");
			} else {
        
				//Se comprueba si que se esta solicitando un Authorization code
				String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
				if (responseType.equals(ResponseType.CODE.toString())) {

					System.out.println("Recibe peticion de codigo");

					//Genera Authorization code
					OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

					//Genera una respuesta con el Authorization Code response
					OAuthResponse resp = OAuthASResponse.authorizationResponse(request,200)
							.location(redirectURI)
							.setCode(oauthIssuerImpl.authorizationCode())
							.buildQueryMessage();

					System.out.println("Genera OauthResponse");

					//Si en la solicitud desde el cliente se ha elegido la opcion 1 (a traves del navegador)
					//Redirige la respuesta a la uri indicada en el request
					response.sendRedirect(resp.getLocationUri());
					System.out.println("Redirige la respuesta a la uri: "+resp.getLocationUri());


					System.out.println(resp.getResponseStatus()+"--"+resp.getLocationUri());

					//Si se ha eledigo la opcion 2
					//Envia la respuesta
//					response.setStatus(resp.getResponseStatus());
//					PrintWriter pw = response.getWriter();
//					pw.print(resp.getLocationUri());
//					pw.flush();
//					pw.close();

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
			String clientId = oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID);
			if (!clientId.equals("id_rrhh")) {
				System.out.println("Invalid client");
				response.sendError(401, "Invalid client");
				//ToDo: comprobar client secret
			} else {
				System.out.println("Recibe access token");

				//Genera Access token
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

				//Genera una respuesta con el Access Token response
				OAuthResponse resp = OAuthASResponse.tokenResponse(200)
						.location(redirectURI)
						.setTokenType(OAuth.DEFAULT_TOKEN_TYPE.toString())
						.setAccessToken(oauthIssuerImpl.accessToken())
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


		} catch (OAuthProblemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
