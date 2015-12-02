/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket.avito.restprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import websocket.avito.metier.Message;
import websocket.avito.metier.Utilisateur;

/**
 *
 * @author Yunho
 */

public class RestProvider {
    
    
    public static String USER_MICROSERVICE ="http://192.168.9.183:8082/FirstMicroService";
    public static String MESSAGE_MICROSERVICE ="http://localhost:8081/EnregistrementMessagerieAvito";
    
    
    public static Utilisateur authentificate(String compte,String password)
    {
        try {
            String resultat="";
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(RestProvider.USER_MICROSERVICE+"/json/user");
            List nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("compte",compte));
            nameValuePairs.add(new BasicNameValuePair("password",password)); 
            
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response;
            response = client.execute(post);
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                resultat+= line;
            }
            
            if(resultat.length()>0)
            {
                Reader reader = new StringReader(resultat);
                JsonReader jsonReader = Json.createReader(reader);
                JsonObject objet = jsonReader.readObject();
                Utilisateur user = new Utilisateur();
                user.setIdentifiant(objet.getJsonString("identifiant").getString());
                user.setNom(objet.getJsonString("nom").getString());
                user.setPrenom(objet.getJsonString("prenom").getString());
                user.setCompte(objet.getJsonString("compte").getString());
                user.setPassword(objet.getJsonString("password").getString());
                return user;
            }
            else
                return null;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RestProvider.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(RestProvider.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(RestProvider.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    public static void insertMessage(Message msg)
    {
     
        try {
            
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(RestProvider.MESSAGE_MICROSERVICE+"/messages");
            List nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("type",msg.getType()));
            nameValuePairs.add(new BasicNameValuePair("contenu",msg.getContenu()));
            nameValuePairs.add(new BasicNameValuePair("timestamp",""+msg.getTimestamp()));
            nameValuePairs.add(new BasicNameValuePair("emetteur",msg.getEmetteur()));
            nameValuePairs.add(new BasicNameValuePair("recepteur",msg.getRecepteur())); 
            
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response;
            response = client.execute(post);
            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RestProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RestProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
        
    }
    
    
    
}
