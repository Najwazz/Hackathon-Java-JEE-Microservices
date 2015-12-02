/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enregistrement.avito.controller;

import com.sun.tracing.dtrace.ModuleAttributes;
import enregistrement.avito.dao.MessageDAO;
import enregistrement.avito.metier.Message;
import enregistrement.avito.metier.UserMessage;
import enregistrement.avito.restprovider.RestProvider;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Yunho
 */
@Controller
public class MessageController {
    
    @Autowired
    private MessageDAO messageDao;
    @Autowired
    private RestProvider userProvider;
    
    
    @RequestMapping("/messages")
    @ResponseBody
    public List<Message> getMessages(){
        return messageDao.getAll();
    }

    
    @RequestMapping("/messages/{identifiant}")
    @ResponseBody
    public List<UserMessage> getContactsOfUser(@PathVariable("identifiant") String identifiant)
    {
        return new ArrayList(messageDao.getMessagesOfUser(identifiant));
    }
    
    
    @RequestMapping(value="/messages",method = RequestMethod.POST)
    @ResponseBody
    public Message insertMessage(@ModelAttribute Message msg)
    {
        messageDao.insertMessage(msg);
        return msg;
    }
    
    @RequestMapping(method= RequestMethod.OPTIONS)
    @ResponseBody
    public String test(){
        return "OK";
    }
    
    
}
