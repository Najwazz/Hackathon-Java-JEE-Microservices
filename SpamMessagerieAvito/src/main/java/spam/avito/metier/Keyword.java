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
@Document(collection = "keywords")
public class Keyword {
    
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    
            
}
