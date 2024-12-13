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

    /* đăng ký */

    $scope.userData = {};
    $scope.errorMessage = '';

    // Hàm đăng ký người dùng
    $scope.registerUser = function () {
        console.log("Payload being sent:", $scope.userData);

        $http.post('/api/auth/register-info', $scope.userData, {
            transformResponse: function (data) {
                try {
                    return JSON.parse(data);
                } catch (e) {
                    return { message: data };
                }
            }
        })
            .then(function (response) {
                console.log("Registration success:", response);
                $scope.successMessage = response.data.message || "Thông tin cá nhân đã lưu.";

                // Gửi OTP
                return $http.post('/api/auth/send-otp', { email: $scope.userData.email });
            })
            .then(function (response) {
                console.log("OTP sent successfully:", response);
                $window.location.href = '/login/verificationOTP';
            })
            .catch(function (error) {
                console.error("Error during registration or OTP:", error);
                $scope.errorMessage = error.data.message || error.data || "Đã xảy ra lỗi.";
            });
    };

});

