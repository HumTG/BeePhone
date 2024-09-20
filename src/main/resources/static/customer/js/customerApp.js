var app = angular.module('customerApp', ['ngRoute']);

app.config(function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl : "/customer/views/home.html",
            controller : "HomeController"
        })
        .when("/san-pham", {
            templateUrl : "/customer/views/san-pham.html",
            controller : "SanPhamController"
        })
        // .when("/settings", {
        //     templateUrl : "settings.html",
        //     controller : "SettingsController"
        // })
        .otherwise({
            redirectTo: '/'
        });
});


app.controller('HomeController', function($scope) {
    $scope.message = "Welcome to the Dashboard!";
});

app.controller('SanPhamController', function($scope) {
    $scope.users = [
        {name: 'User 1'},
        {name: 'User 2'},
        {name: 'User 3'}
    ];
});

app.controller('SettingsController', function($scope) {
    $scope.message = "This is the settings page.";
});