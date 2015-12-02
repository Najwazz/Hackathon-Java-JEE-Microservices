/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket.avito.controller;

import websocket.avito.restprovider.RestProvider;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import websocket.avito.metier.Utilisateur;

/**
 *
 * @author Yunho
 */
@Controller
public class AuthentificationController {
    
    
    
    @RequestMapping("/login")
    public ModelAndView getLoginPage()
    {
        return new ModelAndView("login");
    }
    
    @RequestMapping(value="/authentification",method = RequestMethod.POST)
    @ResponseBody
    public Utilisateur authenticate(@RequestParam("user") String user,
            @RequestParam("password") String password,
            HttpSession session)
    {
        
        Utilisateur utilisateur =  RestProvider.authentificate(user, password);
        
        if(utilisateur!=null)
        {
            utilisateur.setPassword("");
            session.setAttribute("user", utilisateur);
        }
        
        return utilisateur;
    }
    
    @RequestMapping(method= RequestMethod.OPTIONS)
    @ResponseBody
    public String test(){
        return "OK";
    }
}
