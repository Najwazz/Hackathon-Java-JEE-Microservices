/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enregistrement.avito.restprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import enregistrement.avito.metier.Utilisateur;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yunho
 */
@Service
public class RestProvider {
    
    
    public static String USER_MICROSERVICE ="http://192.168.9.183:8082/FirstMicroService";
    
    
    public Utilisateur getUserById(String identifiant)
    {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(RestProvider.USER_MICROSERVICE+"/json/user/"+identifiant);
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            String resultat="";
            while ((line = rd.readLine()) != null) {
                resultat+= line;
            }
            System.out.println("Recuperation de l'utilisateur par ID  : "+resultat);
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
                user.setPassword("");
                return user;
            }
            else
                return null;
            
            
        } catch (IOException ex) {
            Logger.getLogger(RestProvider.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
