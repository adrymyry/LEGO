package es.um.lego;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.um.lego.Common;
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
 * Servlet implementation class ServAutorizacionCode
 */
@WebServlet("/auth")
public class ServAutorizacionCode extends HttpServlet {
	private static final long serialVersionUID = 1L;


    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServAutorizacionCode () {
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
        	if (!Common.existsClient(clientId)) {
				System.out.println("Invalid client");
				response.sendError(401, "Invalid client");
			} else {
        
				//Se comprueba si que se esta solicitando un Authorization code
				String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
				if (responseType.equals(ResponseType.CODE.toString())) {

					System.out.println("Recibe peticion de codigo");

					//Se comprueba si solicita scopes permitidos
					Set<String> scopes = oauthRequest.getScopes();

					if (!Common.validateScopes(clientId, scopes)) {
						System.out.println("Invalid scopes");
						response.sendError(401, "Invalid scopes");
					} else {

						//Genera Authorization code
						OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

						String code = oauthIssuerImpl.authorizationCode();
						AuthInfo authInfo = new AuthInfo(redirectURI, scopes);
						System.out.println("Generado code " + code);

						Common.authorizationCodes.put(code, authInfo);

						response.setStatus(200);
						response.setContentType("text/html");
						response.getWriter().append("<h1>Login</h1>");
						response.getWriter().append("<h2>Do you want to grant access to Instagram to Career</h2>");
						response.getWriter().append("<form action=\"?code=" + code + "\" method=\"POST\">");
						response.getWriter().append("<input type=\"text\" name=\"username\" placeholder=\"username\" />");
						response.getWriter().append("<input type=\"password\" name=\"password\" placeholder=\"password\" />");
						response.getWriter().append("<button type=\"submit\">Submit</button>");
						response.getWriter().append("</form>");


					}

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

    	String code = request.getParameter("code");
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");

    	AuthInfo authInfo = Common.authorizationCodes.get(code);

		System.out.println("code: " + code + " username: " + username + " password: " + password);

		if (!Common.login(username, password)) {

			response.sendError(401, "Invalid credentials");
		} else {

			authInfo.username = username;

			//Genera una respuesta con el Authorization Code response
			OAuthResponse resp = null;
			try {
				resp = OAuthASResponse.authorizationResponse(request,200)
						.location(authInfo.redirectUri)
						.setCode(code)
						.buildQueryMessage();

				System.out.println("Genera OauthResponse");

				//Si en la solicitud desde el cliente se ha elegido la opcion 1 (a traves del navegador)
				//Redirige la respuesta a la uri indicada en el request
				response.sendRedirect(resp.getLocationUri());
				System.out.println("Redirige la respuesta a la uri: "+resp.getLocationUri());

				System.out.println(resp.getResponseStatus()+"--"+resp.getLocationUri());


			} catch (OAuthSystemException e) {
				e.printStackTrace();
			}
		}


	}

}
