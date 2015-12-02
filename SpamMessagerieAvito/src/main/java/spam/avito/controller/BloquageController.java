/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spam.avito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import spam.avito.dao.BloquageDAO;
import spam.avito.metier.Bloquage;


/**
 *
 * @author Yunho
 */
@Controller
public class BloquageController {
    
    @Autowired
    private BloquageDAO bloquageDao;
    
    @RequestMapping(value="/bloquer",method = RequestMethod.POST)
    @ResponseBody
    public ConfirmResponse bloquerUtilisateur(@RequestParam("blocker") String blocker,@RequestParam("blocked") String blocked)
    {
        System.out.println("L'utilisateur "+blocker+" veut bloquer : "+blocked);
        Bloquage bloq = new Bloquage();
        bloq.setBlocker(blocker);
        bloq.setBlocked(blocked);
        bloquageDao.insertNew(bloq);
        return new ConfirmResponse("OK");
    }
    
    @RequestMapping(value="/bloquer/check",method=RequestMethod.GET)
    @ResponseBody
    public ConfirmResponse isBloqued(@RequestParam("blocker") String blocker,@RequestParam("blocked") String blocked)
    {
        if(bloquageDao.getPermissionToTalk(blocker, blocked))
            return new ConfirmResponse("OK");
        else
            return new ConfirmResponse("NOK");
    }
    
    @RequestMapping(method= RequestMethod.OPTIONS)
    @ResponseBody
    public String test(){
        return "OK";
    }
    
    private static class ConfirmResponse
    {
        private String status;

        public ConfirmResponse() {
        }

        public ConfirmResponse(String status){
            this.status=status;
        }
        
        
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        
        
    }
}
