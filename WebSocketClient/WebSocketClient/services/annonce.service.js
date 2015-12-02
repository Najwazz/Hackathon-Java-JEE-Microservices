
myApp.service("AnnonceService",function($http){
    
    this.getAnnonces = function(){
        return $http.get("http://192.168.9.183:8082/FirstMicroService/json/annonce");
        
    };
});