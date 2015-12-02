/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spam.avito.metier;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Yunho
 */
@Document(collection = "bloquages")
public class Bloquage {
    
    private String blocker;
    private String blocked;

    public String getBlocker() {
        return blocker;
    }

    public void setBlocker(String blocker) {
        this.blocker = blocker;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }
    
    
    
}
