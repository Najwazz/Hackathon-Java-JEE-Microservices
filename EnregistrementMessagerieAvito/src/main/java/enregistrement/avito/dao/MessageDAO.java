/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enregistrement.avito.dao;

import java.util.List;
import java.util.Set;
import enregistrement.avito.metier.Message;
import enregistrement.avito.metier.UserMessage;

/**
 *
 * @author Yunho
 */
public interface MessageDAO {

    List<Message> getAll();
    public void insertMessage(Message msg);
    //public List<Message> getMessagesByUser(String identifiant);
    public List<Message> getMessagesBetweenContacts(String emetteur,String recepteur);
    public Set<UserMessage> getMessagesOfUser(String identifiant);
}
