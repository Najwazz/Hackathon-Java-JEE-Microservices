/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket.avito.server;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import websocket.avito.metier.Utilisateur;

/**
 *
 * @author Yunho
 */
public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        
        System.out.println("Etape du handshake !!");
        Utilisateur user = null;
        if(request.getHttpSession()!=null && ((HttpSession)request.getHttpSession()).getAttribute("user")!=null)
            user = (Utilisateur)((HttpSession)request.getHttpSession()).getAttribute("user");
        System.out.println("Utilisateur trouvé dans la session : \n"+user);
        if(user!=null)
            config.getUserProperties().put("user", user);
        
    }
    
    
}
