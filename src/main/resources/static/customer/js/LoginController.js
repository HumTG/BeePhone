// Khởi tạo AngularJS module
const app = angular.module("loginApp", []);

// Tạo LoginController
app.controller("LoginController", function ($scope, $http) {
    $scope.loginData = {
        email: "",
        password: ""
    };

    $scope.errorMessage = "";

    // Xử lý đăng nhập
    $scope.handleLogin = function () {
        console.log("Dữ liệu gửi đi:", $scope.loginData);

        $http.post("http://localhost:8080/rest/khach-hang/login", $scope.loginData)
            .then(function (response) {
                alert("Đăng nhập thành công!");
                console.log("Phản hồi từ API:", response.data);
            })
            .catch(function (error) {
                console.error("Lỗi:", error);
                if (error.status === 401) {
                    $scope.errorMessage = "Email hoặc mật khẩu không đúng!";
                } else {
                    $scope.errorMessage = "Lỗi hệ thống: " + error.statusText;
                }
            });

    };
});
