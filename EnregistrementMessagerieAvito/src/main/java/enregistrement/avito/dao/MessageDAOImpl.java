/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enregistrement.avito.dao;

import com.mongodb.BasicDBObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import enregistrement.avito.metier.Message;
import enregistrement.avito.metier.Utilisateur;
import enregistrement.avito.metier.UserMessage;
import enregistrement.avito.restprovider.RestProvider;


/**
 *
 * @author Yunho
 */
@Repository("messageDao")
public class MessageDAOImpl implements MessageDAO {
    
    @Autowired
    private MongoOperations mongop;
    @Autowired
    private RestProvider userProvider;
    
    public void insertMessage(Message msg)
    {
        mongop.insert(msg);
    }
    
    
    public Set<UserMessage> getMessagesOfUser(String identifiant)
    {
        System.out.println("Recherche des contacts de l'utilisateur : "+identifiant);
        Set<UserMessage> liste = new HashSet();
        Utilisateur user;
        List<String> distinctContacts1 = mongop.getCollection("messages").distinct("emetteur",
                new BasicDBObject("recepteur",identifiant));
        List<String> distinctContacts2 = mongop.getCollection("messages").distinct("recepteur",
                new BasicDBObject("emetteur",identifiant));
        
        for(String s : distinctContacts1)
        {
            user = userProvider.getUserById(s);
            if(user!=null)
            {
                UserMessage um = new UserMessage();
                um.setUser(user);
                um.setMessages(this.getMessagesBetweenContacts(identifiant, user.getIdentifiant()));
                liste.add(um);
            }
        }
        for(String s : distinctContacts2)
        {
            user = userProvider.getUserById(s);
            if(user!=null)
            {
                UserMessage um = new UserMessage();
                um.setUser(user);
                um.setMessages(this.getMessagesBetweenContacts(identifiant, user.getIdentifiant()));
                liste.add(um);
            }
        }
        return liste;
    }
    
    public List<Message> getMessagesBetweenContacts(String emetteur,String recepteur)
    {
        Criteria crit1 = new Criteria();
        crit1.andOperator(Criteria.where("emetteur").is(emetteur),Criteria.where("recepteur").is(recepteur));
        Criteria crit2 = new Criteria();
        crit2.andOperator(Criteria.where("emetteur").is(recepteur),Criteria.where("recepteur").is(emetteur));
        Criteria crit = new Criteria();
        crit.orOperator(crit1,crit2);
        return mongop.find(new Query(crit), Message.class);
    }
    
    @Override
    public List<Message> getAll()
    {
        return mongop.findAll(Message.class);
    }
    
    

    public MongoOperations getMongop() {
        return mongop;
    }

    public void setMongop(MongoOperations mongop) {
        this.mongop = mongop;
    }

    public RestProvider getUserProvider() {
        return userProvider;
    }

    public void setUserProvider(RestProvider userProvider) {
        this.userProvider = userProvider;
    }
    
    
    
}
