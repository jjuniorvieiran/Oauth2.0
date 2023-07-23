package com.jj.oauth2.oauth2client;

import org.eclipse.microprofile.config.Config;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/authorize")
public class AuthorizationCodeServlet extends HttpServlet {

    @Inject
    private Config config;

    // The flow of getting an authorization code starts with the client by redirecting the
    //  browser to the authorization server's authorization endpoint.
    // After processing the request, the authorization server's authorization endpoint will
    //  generate and append a code, in addition to the received state parameter,
    //  to the redirect_uri and will redirect back
    //  the browser http://localhost:9081/callback?code=A123&state=Y.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //...
        request.getSession().removeAttribute("tokenResponse");
        String state = UUID.randomUUID().toString();
        request.getSession().setAttribute("CLIENT_LOCAL_STATE", state);

        String authorizationUri = config.getValue("provider.authorizationUri", String.class);
        String clientId = config.getValue("client.clientId", String.class);
        String redirectUri = config.getValue("client.redirectUri", String.class);
        String scope = config.getValue("client.scope", String.class);

        String authorizationLocation = authorizationUri + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&scope=" + scope
                + "&state=" + state;
        response.sendRedirect(authorizationLocation);
    }
}
