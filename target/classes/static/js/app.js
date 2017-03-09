var app = angular.module('myApp', ["ngRoute"]);

app.controller('springCtrl', function($scope, $http) {
    $scope.getMain = function() {
        $http.get('/').success(function(res) {
            $scope.mainRes = res;
            console.log($scope.mainRes);
        })
    }
    $scope.getMain();
});
app.controller('joinCtrl', function($scope, $http) {
    $scope.getJoin = function() {
        $http.get('/join').success(function(res) {
            $scope.joinRes = res;
            console.log($scope.joinRes);
        })
    };
    $scope.getJoin();
});
app.controller('filtCtrl', function($scope, $http) {
    $scope.fieldF = '';
    $scope.operatorF = '';
    $scope.valueF = '';
    $scope.noR = "";
    $scope.dataFilt = {

    };

    $scope.doFilt = function() {
        $scope.dataF = JSON.stringify($scope.dataFilt);
        console.log($scope.dataF);
        $scope.noR = "";
        $http.post('/filter', $scope.dataF).success(function(res) {
            $scope.filtRes = res;
            if ($scope.filtRes.length == 0) {
                console.log("no result");
                $scope.noR = "no result";
            }
            console.log($scope.filtRes);
        });
    };

});

app.config(function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "./page/main.html",
            controller: "springCtrl"
        })
        .when("/join", {
            templateUrl: "./page/join.html",
            controller: "joinCtrl"
        }).
    when("/filt", {
        templateUrl: "./page/filt.html",
        controller: "filtCtrl"
    })
});