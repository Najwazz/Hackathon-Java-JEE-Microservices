
myApp.service("MessageService",function($http){

    this.getMessages = function(identifiant){
        return $http.get("http://localhost:8081/EnregistrementMessagerieAvito/messages/"+identifiant);
        
    };
    
    this.getUserById = function(identifiant){
        return $http.get("http://192.168.9.183:8082/FirstMicroService/json/user/"+identifiant);    
    }
    
});