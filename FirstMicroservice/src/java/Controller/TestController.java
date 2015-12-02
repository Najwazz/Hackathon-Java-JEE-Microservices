/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Metier.Utilisateur;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Yunho
 */
@Controller
public class TestController {
    
    @RequestMapping(value="/json/user",method = RequestMethod.POST)
    @ResponseBody
    public Utilisateur getUser(@RequestParam("compte") String compte,
            @RequestParam("password") String password){
        
        Utilisateur user = new Utilisateur();
        user.setIdentifiant("BK275058");
        user.setNom("Morabit");
        user.setPrenom("Mouad");
        user.setCompte(compte);
        user.setPassword(password);
        
        
        return user;
        
        
    }
}
