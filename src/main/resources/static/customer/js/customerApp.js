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
        .when("/cart", {
            templateUrl : "/customer/views/cart.html",
            controller : "SanPhamController"
        })
        .when("/thanh-toan", {
            templateUrl : "/customer/views/thanh-toan.html",
            controller : "SanPhamController"
        })
        .otherwise({
            redirectTo: '/'
        });

});


app.controller('HomeController', function($scope,$http) {
    // Gọi API để lấy dữ liệu top 5 best sellers
    $http.get('http://localhost:8080/top-5-best-seller')
        .then(function(response) {
            // Lưu dữ liệu vào scope để hiển thị
            $scope.bestSellers = response.data;
        })
        .catch(function(error) {
            console.error('Error fetching data:', error);
        });
});


// Tạo filter để định dạng giá tiền Việt Nam Đồng
app.filter('currencyVND', function() {
    return function(input) {
        if (!input) return '';
        // Chuyển số thành chuỗi với định dạng 1.000.000
        return parseFloat(input).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    };
});

