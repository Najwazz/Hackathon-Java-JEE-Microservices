

myApp.config(function($routeProvider){
    
    $routeProvider
        .when('/messagerie',{
            templateUrl : '/template/messagerieTemplate.html',
            controller : 'MainController'
        })
        .when('/annonces',{
            templateUrl : '/template/annoncesTemplate.html',
            controller : 'AnnonceController'
        })
        .otherwise({ redirectTo : '/' });
    
});