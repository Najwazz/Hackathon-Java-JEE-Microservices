/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spam.avito.dao;

import spam.avito.metier.Bloquage;

/**
 *
 * @author Yunho
 */
public interface BloquageDAO {

    boolean getPermissionToTalk(String id1, String id2);

    void insertNew(Bloquage bloq);
    
}
