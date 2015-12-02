/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket.avito.server;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websocket.avito.metier.Message;
import websocket.avito.metier.Utilisateur;
import websocket.avito.restprovider.RestProvider;

/**
 *
 * @author Yunho
 */
@ServerEndpoint(value="/messagerie",
        encoders = EncoderDecoder.class,
        decoders = EncoderDecoder.class,
        configurator = HttpSessionConfigurator.class)
public class WServer {
    
    
    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    public static List<Utilisateur> utilisateurs = Collections.synchronizedList(new ArrayList<Utilisateur>());
    private static Map<Session,Utilisateur> established = Collections.synchronizedMap(new HashMap<Session,Utilisateur>());
    
    static
    {
        utilisateurs.add(new Utilisateur("BK275058","Morabit","Mouad"));
        utilisateurs.add(new Utilisateur("WA242424","Ouasmine","Mohammed Amine"));
        
    }
    
    @OnOpen
    public void onOpen(Session session)
    {
        System.out.println("Nouvelle connexion : "+session.getId());
        Utilisateur user = (Utilisateur)session.getUserProperties().get("user");
        if(user!=null)
        {
            
            try {
                Message msg = new Message();
                msg.setEmetteur(user.getIdentifiant());
                msg.setRecepteur("");
                msg.setType("STATUS");
                msg.setContenu("online");
                msg.setTimestamp(new Date().getTime());
                    
                for(Session s: sessions)
                {
                    if(s!=null && s.getBasicRemote()!=null)
                        s.getBasicRemote().sendObject(msg);
                }
                
                sessions.add(session);
                established.put(session,user);

                msg = new Message();
                msg.setEmetteur("Server");
                msg.setRecepteur(user.getIdentifiant());
                msg.setType("INFOS");
                StringWriter sw = new StringWriter();
                JsonGenerator generateur = Json.createGenerator(sw);
                generateur.writeStartObject();
                generateur.write("identifiant",user.getIdentifiant());
                generateur.write("nom",user.getNom());
                generateur.write("prenom",user.getPrenom());
                generateur.write("compte",user.getCompte());
                generateur.writeEnd();
                generateur.flush();
                msg.setContenu(sw.toString());
                msg.setTimestamp(new Date().getTime());
                session.getBasicRemote().sendObject(msg);
                Set<String> connectedIdentifiants = new HashSet();
                for (Session s : established.keySet())
                {
                    connectedIdentifiants.add(established.get(s).getIdentifiant());
                }
                String resultat="";
                for(String s : connectedIdentifiants)    
                    resultat += s+",";
                
                msg = new Message();
                msg.setEmetteur("Server");
                msg.setRecepteur(user.getIdentifiant());
                msg.setType("WHOSONLINE");
                msg.setContenu(resultat.substring(0,resultat.length()-1));
                msg.setTimestamp(new Date().getTime());
                session.getBasicRemote().sendObject(msg);
                
            } catch (IOException ex) {
                Logger.getLogger(WServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EncodeException ex) {
                Logger.getLogger(WServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            try {
                Message msg = new Message();
                msg.setEmetteur("Server");
                msg.setRecepteur("Visiteur");
                msg.setType("ERROR");
                msg.setContenu("Veuillez vous authentifier !");
                msg.setTimestamp(new Date().getTime());
                session.getBasicRemote().sendObject(msg);
            } catch (IOException ex) {
                Logger.getLogger(WServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EncodeException ex) {
                Logger.getLogger(WServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @OnMessage
    public void onMessage(Message message,Session session)
    {
        
        Utilisateur user = ((Utilisateur)session.getUserProperties().get("user"));
        message.setEmetteur(user.getIdentifiant());
        RestProvider.insertMessage(message);
        switch(message.getType())
        {
            case "BASIC":
                for (Session s : established.keySet())
                {
                    if(established.get(s).getIdentifiant().equals(message.getRecepteur()))
                    {
                        try {
                            s.getBasicRemote().sendObject(message);
                        } catch (IOException ex) {
                            Logger.getLogger(WServer.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (EncodeException ex) {
                            Logger.getLogger(WServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            break;
            
        }
        
    }
    
    @OnClose
    public void onClose(Session session)
    {
        System.out.println("Fermeture de la connexion de  : "+session.getId());
        sessions.remove(session);
        established.remove(session);
        
        Utilisateur user = (Utilisateur)session.getUserProperties().get("user");
        Message msg = new Message();
        msg.setEmetteur(user.getIdentifiant());
        msg.setRecepteur("");
        msg.setType("STATUS");
        msg.setContenu("offline");
        msg.setTimestamp(new Date().getTime());

        for(Session s: sessions)
        {
            if(s!=null && s.getBasicRemote()!=null)
                try {
                    s.getBasicRemote().sendObject(msg);
            } catch (IOException ex) {
                Logger.getLogger(WServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EncodeException ex) {
                Logger.getLogger(WServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
}
