
myApp.controller("MainController",['$scope','MessageService','$location','$rootScope','BlockingService','KeywordService','$interval',function($scope,MessageService,$location,$rootScope,BlockingService,KeywordService,$interval){
    
    $scope.connectedUser;
    $scope.authentification={};
    $scope.title_authentification ="Authentification";
    $scope.user_messages=[];
    $scope.selectedUser ={};
    $scope.keywords="";
    var webSocket;
    KeywordService.getKeywords().success(function(data){
        
        $scope.keywords = data.map(function(element){
            return element.keyword;
        }).join("|");    
    });
    $scope.authentifier = function(){
            $.ajax({
                    method :'POST',
                    url : 'http://localhost:8081/WSocketSample/authentification',
                    data:   'user='+encodeURIComponent($scope.authentification.user)
                       +'&password='+encodeURIComponent($scope.authentification.password),
                    headers: {'Content-Type': 'application/x-www-form-urlencoded',
                              'Access-Control-Allow-Credentials': 'true'},
                    crossDomain: true,
                    xhrFields: {
                      withCredentials: true
                    }
                  })
            .success(function(data){
                if(data)
                {
                    console.log("Connexion réussie");
                    $scope.seConnecter();
                    
                }
                else
                {
                    console.log("Connexion échouée");
                    $scope.authentification_error = "Veuillez vérifier vos données saisis";
                }
                    
            });
    }
    
    $scope.bloquer = function(){
        $.ajax({
                method :'POST',
                url : 'http://localhost:8081/SpamMessagerieAvito/bloquer',
                data:   'blocker='+encodeURIComponent($scope.connectedUser.identifiant)
                   +'&blocked='+encodeURIComponent($scope.selectedUser.user.identifiant),
                headers: {'Content-Type': 'application/x-www-form-urlencoded',
                          'Access-Control-Allow-Credentials': 'true'},
                crossDomain: true,
                xhrFields: {
                  withCredentials: true
                }
              })
            .success(function(data){
                console.log("Resultat d'envoi de la requete : "+data);
                $scope.$apply(function(){
                    $scope.selectedUser.denied=true;
                });
            });
        
    }
    
    
    $scope.selectUser = function(user){
        $scope.selectedUser = user;   
        $scope.selectedUser.new_messages = false;
        setTimeout(function(){
            $("#tchatbody").scrollTop($("#tchatbody").get(0).scrollHeight);
            $('[data-toggle="tooltip"]').tooltip();
        },100);
        
        /*$(function () {
          $('[data-toggle="tooltip"]').tooltip();
        });*/
    };
    
    
    $scope.seConnecter = function(){
        
        if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
            writeResponse("Socket déja ouverte");
            return;
        }

        webSocket = new WebSocket("ws://localhost:8081/WSocketSample/messagerie");
        $rootScope.webSocket = webSocket;
        webSocket.onopen = function(event){
            /*if(event.data === undefined)
                return;
            */
            console.log("Socket connectée !");
            
        };

        webSocket.onmessage = function(event){
            //console.log(event.data);
            var message = JSON.parse(event.data);
            if(message.type=='INFOS')
            {
                console.log("Recu message Infos avec contenu : "+message.contenu);
                $scope.$apply(function(){
                    $scope.connectedUser = JSON.parse(message.contenu);
                    $rootScope.connectedUser = $scope.connectedUser;
                    MessageService.getMessages($scope.connectedUser.identifiant)
                        .success(function(data){
                            $scope.user_messages = data;
                            $scope.user_messages.forEach(function(um){
                                console.log(um);
                                BlockingService.isAllowed($scope.connectedUser.identifiant,um.user.identifiant)
                                .success(function(data){
                                    console.log($scope.connectedUser.identifiant+"<=>"+um.user.identifiant+" : "+data.status);  
                                    um.denied=(data.status!="OK");
                                });
                            });
                        });
                    
                });
            }
            else if(message.type=='ERROR')
            {
                console.log("Recu message d erreur !?");
                $scope.$apply(function(){
                    $scope.authentification_error = "Veuillez vous authentifier";
                });
                webSocket.close();
            }
            else if(message.type=='WHOSONLINE')
            {
                console.log("Recu message WHOSONLINE: "+message.contenu); 
                var connected = message.contenu.split(",");
                var stop = $interval(function(){
                    if($scope.user_messages.length>0)
                    {   
                        $interval.cancel(stop);
                        $scope.user_messages.forEach(function(um){
                            um.status="offline";
                            connected.forEach(function(con){
                                if(um.user.identifiant == con)
                                    um.status ="online";
                            });
                        });
                        
                    }
                },1000);
                
            }
            else if(message.type=='STATUS')
            {
                console.log(message.emetteur+" a passé au status : "+message.contenu);
                $scope.$apply(function(){
                    $scope.user_messages.forEach(function(elem){
                        if(elem.user.identifiant==message.emetteur)
                            elem.status = message.contenu;
                    });
                });
            }
            else
            {
                //ajouterMessage(message.emetteur+" : "+message.contenu);   
                console.log("RECU MESSAGE :");
                console.log(message);
                var b = false;
                $scope.user_messages.forEach(function(userm){
                   if(userm.user.identifiant==message.emetteur)
                   {
                       if(!String.prototype.endsWith.call(window.location,"messagerie"))
                       {
                            new PNotify({
                                title: userm.user.nom+ ' '+userm.user.prenom,
                                text: message.contenu,
                                type:"info",
                                icon: 'glyphicon glyphicon-envelope',
                                addclass: "stack-bottomright",
                                stack : {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25}
                            });   
                       }
                       $scope.$apply(function(){
                            userm.messages.push(message);
                            if($scope.selectedUser.user)
                            {
                                if(userm.user.identifiant!=$scope.selectedUser.user.identifiant)
                                    userm.new_messages=true;

                                if(userm.user.identifiant==$scope.selectedUser.user.identifiant)
                                {
                                   setTimeout(function(){
                                    $("#tchatbody").scrollTop($("#tchatbody").get(0).scrollHeight);
                                    },100);
                                }
                            }
                       });
                       b = true;
                       
                   }
                });
                if(b==false)
                {
                    console.log("Recu un message d'un nouveau contact");
                    MessageService.getUserById(message.emetteur).success(function(data){
                        if(data)
                        {
                            //$scope.$apply(function(){
                                
                                if(!String.prototype.endsWith.call(window.location,"messagerie"))
                               {
                                    new PNotify({
                                        title: data.nom+ ' '+data.prenom,
                                        text: message.contenu,
                                        type:"info",
                                        icon: 'glyphicon glyphicon-envelope',
                                        addclass: "stack-bottomright",
                                        stack : {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25}
                                    });   
                               }
                                $scope.user_messages.push({
                                    user : data,
                                    messages : [message]  
                                }); 
                            //});
                        }
                    });
                    
                }
            }
            
            
        };
        webSocket.onclose = function(event){
            //ajouterMessage("Connexion fermée");    
        };
    }
    
    $scope.seConnecter();
    
    
    $scope.envoyer = function(){
        if($scope.message.length>0)
        {
            console.log("Test regex : "+$scope.message.search(new RegExp($scope.keywords,"i")));
            if($scope.message.search(new RegExp($scope.keywords,"i"))==-1)
            {
                var message = {
                    type:"BASIC",
                    contenu:$scope.message,
                    timestamp:new Date().getTime(),
                    emetteur:$scope.connectedUser.identifiant,
                    recepteur:$scope.selectedUser.user.identifiant

                };
                webSocket.send(JSON.stringify(message));   
                $scope.selectedUser.messages.push(message);
                setTimeout(function(){
                    $("#tchatbody").scrollTop($("#tchatbody").get(0).scrollHeight);
                },100);
                $scope.message="";
            }
            else
            {
                var message = {
                    type:"BASIC",
                    contenu:"Veuillez soigner votre langage !",
                    timestamp:new Date().getTime(),
                    emetteur:$scope.connectedUser.identifiant,
                    recepteur:$scope.selectedUser.user.identifiant

                };
                $scope.selectedUser.messages.push(message);
                setTimeout(function(){
                    $("#tchatbody").scrollTop($("#tchatbody").get(0).scrollHeight);
                },100);
                $scope.message="";    
            }
        }
    }
    
    $scope.formater = function(timestamp)
    {
        return moment(timestamp).format('DD/MM/YYYY HH:mm:ss');    
    }
    
}]);