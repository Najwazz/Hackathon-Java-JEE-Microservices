/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spam.avito.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import spam.avito.metier.Bloquage;

/**
 *
 * @author Yunho
 */
@Repository("bloquageDao")
public class BloquageDAOImpl implements BloquageDAO {
    
    @Autowired
    private MongoOperations mongop;
    
    @Override
    public void insertNew(Bloquage bloq)
    {
        mongop.insert(bloq);
    }
    
    @Override
    public boolean getPermissionToTalk(String id1,String id2)
    {
        Criteria crit1 = new Criteria();
        crit1.andOperator(Criteria.where("blocker").is(id1),Criteria.where("blocked").is(id2));
        Criteria crit2 = new Criteria();
        crit2.andOperator(Criteria.where("blocker").is(id2),Criteria.where("blocked").is(id1));
        Criteria crit = new Criteria();
        crit.orOperator(crit1,crit2);
        return mongop.findOne(new Query(crit), Bloquage.class)==null;
    }
    
}
