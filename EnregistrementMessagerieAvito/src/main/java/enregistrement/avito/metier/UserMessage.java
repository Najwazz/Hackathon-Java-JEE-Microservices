/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enregistrement.avito.metier;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Yunho
 */
public class UserMessage {
    
    private Utilisateur user;
    private List<Message> messages;

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.user);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserMessage other = (UserMessage) obj;
        if (!this.user.getIdentifiant().equals(other.user.getIdentifiant())) {
            return false;
        }
        return true;
    }
    
    
    
}
