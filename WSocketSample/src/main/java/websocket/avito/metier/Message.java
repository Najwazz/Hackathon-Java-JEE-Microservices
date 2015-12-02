/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket.avito.metier;


public class Message {
    
    private String type;
    private String contenu;
    private long timestamp;
    private String emetteur;
    private String recepteur;
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(String emetteur) {
        this.emetteur = emetteur;
    }

    public String getRecepteur() {
        return recepteur;
    }

    public void setRecepteur(String recepteur) {
        this.recepteur = recepteur;
    }

    @Override
    public String toString() {
        return "Message{" + "type=" + type + ", contenu=" + contenu + ", timestamp=" + timestamp + ", emetteur=" + emetteur + ", recepteur=" + recepteur + '}';
    }

    

    
    
    
    
}
