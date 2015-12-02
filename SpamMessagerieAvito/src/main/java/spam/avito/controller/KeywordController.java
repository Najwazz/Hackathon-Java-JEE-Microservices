/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spam.avito.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import spam.avito.dao.KeywordDAO;
import spam.avito.metier.Keyword;

/**
 *
 * @author Yunho
 */
@Controller
public class KeywordController {
    
    
    @Autowired
    private KeywordDAO keywordDao;
    
    @RequestMapping("/keywords")
    @ResponseBody
    public List<Keyword> getAllKeywords()
    {
        return keywordDao.getAll();
    }
}
