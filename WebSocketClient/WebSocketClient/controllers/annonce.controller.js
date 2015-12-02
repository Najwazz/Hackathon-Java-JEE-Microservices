myApp.controller("AnnonceController",['$scope','AnnonceService','$rootScope',function($scope,AnnonceService,$rootScope){
    
    $scope.title="Liste des annonces";
    $scope.annonces=[];
    $scope.contactVendeur={};
    $scope.show_contact = false;
    $scope.messageAEnvoyer="";
    AnnonceService.getAnnonces().success(function(resultat){
        
        //$scope.$apply(function(){
            console.log(resultat);
            $scope.annonces = resultat;
            
        //});
    });
    $scope.formater = function(timestamp){
        return moment(timestamp).format('DD/MM/YYYY HH:mm:ss');    
    };
    
    $scope.contacter = function(vendeur){
        $scope.show_contact = true;
        $scope.contactVendeur = vendeur;
        
    };
    
    $scope.envoyerMessage = function(){
        console.log("Envoyer le message : "+$scope.messageAEnvoyer+" a : "+$scope.contactVendeur.identifiant); 

        var message = {
            type:"BASIC",
            contenu:$scope.messageAEnvoyer,
            timestamp:new Date().getTime(),
            emetteur:$rootScope.connectedUser.identifiant,
            recepteur:$scope.contactVendeur.identifiant
        };
        console.log(message);
        $rootScope.webSocket.send(JSON.stringify(message)); 
        $scope.show_contact = false;
        $scope.messageAEnvoyer="";
    }
    
    $scope.annulerMessage = function(){
        $scope.show_contact = false;
        $scope.contactVendeur={};
        $scope.messageAEnvoyer="";
    }
    
}]);