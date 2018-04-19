package cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.ext.dynamicreg.client.OAuthRegistrationClient;
import org.apache.oltu.oauth2.ext.dynamicreg.client.request.OAuthClientRegistrationRequest;
import org.apache.oltu.oauth2.ext.dynamicreg.client.response.OAuthClientRegistrationResponse;
import org.apache.oltu.oauth2.ext.dynamicreg.common.OAuthRegistration;
import org.apache.oltu.oauth2.integration.Common;
import org.apache.oltu.oauth2.integration.CommonExt;

public class OAuthClientTest {

    public static void main(String[] args) throws OAuthSystemException, IOException {

        //Genear Request para el registro del cliente 
		System.out.println("Falta implementar la generación del request para el registro del cliente");

		//Enviar Petición de registro al servidor de registro
		System.out.println("Falta implementar el envio del request para el registro del cliente");
		
		//Genera el Oauth request para solicitar el Authorization code
		OAuthClientRequest request = OAuthClientRequest
				.authorizationLocation("http://localhost:8080/AsigSeguridad/ServAutorizacion")
				.setRedirectURI("http://localhost:8080/redirect")
				.setClientId("id_prueba_registro")
				.setResponseType("code")
				.buildQueryMessage();

		//Dos alternativas para enviar la petición
		
		//1- Desde el navegador. Para realizar la solicitud hay que pegar en el navegador la uri indicada en la consola 
		//IMPORTANTE: Revisad que la uri está bien generada, a veces se incluyen caracteres extraños
		//System.out.println("Visita: " + request.getLocationUri() + "\n y copia/pega el código de la uri aqui:");
		
		//2-Desde esta clase java
		URL url = new URL(request.getLocationUri());
		HttpURLConnection c = (HttpURLConnection)url.openConnection();
		
		//Procesar respuesta
		BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//Respuesta con el Authorization Code
		System.out.println(response.toString());
		
		System.out.println("Hay que extraer el Authorization Code de la response anterior e integrarlo en los pasos posteriores");
		
		//Generamos el request para pedir el access token
		System.out.println("Falta implementar la generación del request para solicitar el access token");

		//Esta vez hay que realizar la petición sin necesidad del navegador web
		System.out.println("Falta implementar el envio del request para la solicitud del access token");

		//Recibimos la respuesta con el access token
		System.out.println("Falta implementar la recepción del response con el access token");

		//Realizamos la petición de un recurso al servidor de recursos 
		System.out.println("Falta implementar la comunicación con el Resource Server para acceder al recurso deseado presentando el access token");
    }

}
