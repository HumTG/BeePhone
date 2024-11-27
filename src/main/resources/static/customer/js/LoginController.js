// Khởi tạo AngularJS module
const app = angular.module("loginApp", []);

app.controller("LoginController", function ($scope, $http, $window ) {
    $scope.loginData = {
        email: "",
        matKhau: ""
    };

    $scope.errorMessage = "";

    $scope.handleLogin = function () {
        $http.post("http://localhost:8080/rest/khach-hang/login", $scope.loginData)
            .then(function (response) {
                // Lưu thông tin người dùng vào localStorage
                localStorage.setItem("user", JSON.stringify(response.data));

                // Lưu trạng thái thông báo
                localStorage.setItem("loginSuccess", "true");

                // Chuyển hướng sang trang index
                $window.location.href = "http://localhost:8080/index";
            })
            .catch(function (error) {
                toastr.error('Mật khẩu hoặc email không đúng', 'Lỗi đăng nhập'); // Sử dụng toastr để hiển thị lỗi
            });
    };

});

