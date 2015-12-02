myApp.service("BlockingService",function($http){
    this.isAllowed = function(blocker,blocked){
        return $http.get("http://localhost:8081/SpamMessagerieAvito/bloquer/check?blocker="+blocker+"&blocked="+blocked);
    };
    
});

myApp.service("KeywordService",function($http){
    
    this.getKeywords = function(){
        return $http.get("http://localhost:8081/SpamMessagerieAvito/keywords");   
    };
    
});