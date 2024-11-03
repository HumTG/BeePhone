var app = angular.module('BanHangTaiQuayApp', []);
var url = "http://localhost:8080/rest/hoa-don"

app.controller('BanHangTaiQuayCtrl',function ($scope,$http){
    console.log("bán hàng tại quầy ctrl")
    $scope.viTriHoaDon = 0;
    $scope.hoa_don = {};

    ///lấy hóa đơn
    $scope.getAllHoaDon = function (){
        $http.get(url).then(function(response) {
            if (response.status === 200) {
                $scope.listHD = response.data;
                console.log(response.data);
            } else {
                console.error("Error: ", response.status);
            }
        }).catch(function(error) {
            console.error("Error occurred: ", error);
        });
    }
    $scope.getAllHoaDon();

    ///đổi hóa đơn
    $scope.changeHoaDon = function(index,a){
        $scope.viTriHoaDon = index;
        $scope.hoa_don = angular.copy(a);
        console.log($scope.hoa_don);

    }

});