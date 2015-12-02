/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spam.avito.dao;

import java.util.List;
import spam.avito.metier.Keyword;

/**
 *
 * @author Yunho
 */
public interface KeywordDAO {

    List<Keyword> getAll();
    
}
