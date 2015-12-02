/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spam.avito.dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import spam.avito.metier.Keyword;

/**
 *
 * @author Yunho
 */
@Repository("keywordDao")
public class KeywordDAOImpl implements KeywordDAO {
    
    @Autowired
    private MongoOperations mongop;
    
    @Override
    public List<Keyword> getAll()
    {
        return mongop.findAll(Keyword.class);
    }
    
}
