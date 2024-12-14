// var app = angular.module('registerApp', []);
//
// app.controller('RegisterController', function ($scope, $http, $window) {
//     $scope.userData = {};
//     $scope.errorMessage = '';
//
//     // Hàm đăng ký người dùng
//     $scope.registerUser = function () {
//         $http.post('/api/auth/register', $scope.userData)
//             .then(function (response) {
//                 // Sau khi đăng ký, gửi OTP
//                 $http.post('/api/auth/send-otp', { email: $scope.userData.email })
//                     .then(function () {
//                         // Chuyển đến trang verificationOTP.html
//                         $window.location.href = 'verificationOTP.html?email=' + encodeURIComponent($scope.userData.email);
//                     }, function (error) {
//                         $scope.errorMessage = 'Lỗi khi gửi OTP: ' + error.data;
//                     });
//             }, function (error) {
//                 $scope.errorMessage = 'Lỗi đăng ký: ' + error.data;
//             });
//     };
// });
//
// app.controller('VerificationController', function ($scope, $http, $window, $location) {
//     $scope.otpCode = '';
//     $scope.email = new URLSearchParams($location.absUrl().split('?')[1]).get('email');
//     $scope.errorMessage = '';
//
//     // Hàm xác minh OTP
//     $scope.verifyOTP = function () {
//         $http.post('/api/auth/verify-otp', {
//             email: $scope.email,
//             otpCode: $scope.otpCode
//         }).then(function () {
//             // Nếu xác minh OTP thành công, chuyển đến trang cài đặt mật khẩu
//             $window.location.href = 'setPassword.html?email=' + encodeURIComponent($scope.email);
//         }, function (error) {
//             $scope.errorMessage = 'Lỗi xác minh OTP: ' + error.data;
//         });
//     };
// });
//
// app.controller('SetPasswordController', function ($scope, $http, $window, $location) {
//     $scope.passwordData = {};
//     $scope.email = new URLSearchParams($location.absUrl().split('?')[1]).get('email');
//     $scope.errorMessage = '';
//
//     // Hàm thiết lập mật khẩu mới
//     $scope.setNewPassword = function () {
//         if ($scope.passwordData.newPassword !== $scope.passwordData.confirmPassword) {
//             $scope.errorMessage = "Mật khẩu không khớp.";
//             return;
//         }
//         $http.post('/api/auth/set-password', {
//             email: $scope.email,
//             newPassword: $scope.passwordData.newPassword
//         }).then(function () {
//             alert("Đăng ký thành công!");
//             $window.location.href = "index.html"; // Chuyển về trang chủ
//         }, function (error) {
//             $scope.errorMessage = 'Lỗi khi thiết lập mật khẩu: ' + error.data;
//         });
//     };
// });
