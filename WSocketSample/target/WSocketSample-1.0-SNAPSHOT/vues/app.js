var wsclient = angular.module('WSClient',[]);




wsclient.controller('MainController',['$scope','$http',function($scope,$http){
    
    $scope.title = "Messagerie instantannée";   
    $scope.messages =[];
    $scope.emetteur;
    $scope.recepteur="";
    $scope.authentification={};
    var webSocket;
    $scope.seConnecter = function(){
        
        if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
            writeResponse("Socket déja ouverte");
            return;
        }

        webSocket = new WebSocket("ws://localhost:8081/WSocketSample/messagerie");

        webSocket.onopen = function(event){
            /*if(event.data === undefined)
                return;
            */
            //ajouterMessage("Connexion effectuée, envoi de l'identification...");
          
        };

        webSocket.onmessage = function(event){
            console.log(event.data);
            var message = JSON.parse(event.data);
            if(message.type=='INFOS')
            {
                $scope.emetteur = message.contenu;
                ajouterMessage("Serveur : Bienvenue "+message.contenu);
            }
            else if(message.type=='ERROR')
            {
                ajouterMessage("Serveur : "+message.contenu);
                webSocket.close();
            }
            else
            {
                ajouterMessage(message.emetteur+" : "+message.contenu);    
                
            }
            
            
        };
        webSocket.onclose = function(event){
            ajouterMessage("Connexion fermée");    
        };
    }
    
    $scope.seConnecter();
    
    $scope.envoyer = function(){
        var message = {
            type:"BASIC",
            contenu:$scope.texte,
            timestamp:new Date().getTime(),
            emetteur:$scope.emetteur,
            recepteur:$scope.recepteur

        };
        webSocket.send(JSON.stringify(message));   
        $scope.messages.push({texte:message.emetteur+" : "+message.contenu});
        $scope.texte="";
    }
    
    $scope.authentifier = function(){
            $.ajax({
                    method :'POST',
                    url : 'http://localhost:8081/WSocketSample/authentification',
                    data:   'user='+encodeURIComponent($scope.authentification.user)
                       +'&password='+encodeURIComponent($scope.authentification.password),
                    headers: {'Content-Type': 'application/x-www-form-urlencoded',
                             'Access-Control-Allow-Origin': 'http://localhost:80801/',
                              'Access-Control-Allow-Credentials': 'true'
                             },
                    crossDomain: true,
                    xhrFields: {
                      withCredentials: true
                    }/*,
                    transformResponse: [function (data) {
                          return data;
                    }]*/
                  })
            .success(function(data){
                if(data=='OK')
                   $scope.seConnecter();
                else
                    $scope.authentification_error = "Veuillez vérifier vos données saisies";
                
                console.log(data);
                    
            });
    }
    
    function ajouterMessage(message){
        $scope.$apply(function(){
            $scope.messages.push({texte:message});
        });
    }
    
    
}]);