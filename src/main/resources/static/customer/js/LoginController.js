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

    // Hàm đăng ký người dùng trong LoginController
    $scope.registerUser = function () {
        console.log("Payload being sent:", $scope.userData);

        $http.post('/api/auth/register-info', $scope.userData)
            .then(function (response) {
                console.log("Registration success:", response);
                $scope.successMessage = response.data.message || "Thông tin cá nhân đã lưu.";

                // Gửi OTP
                return $http.post('/api/auth/send-otp', { email: $scope.userData.email });
            })
            .then(function (response) {
                console.log("OTP sent successfully:", response.data);
                const otpToken = response.data.otpToken;  // Lấy token từ phản hồi
                sessionStorage.setItem('otpToken', otpToken); // Lưu token vào sessionStorage
                console.log("Token saved to sessionStorage:", otpToken);
                $window.location.href = '/login/verificationOTP';
            })
            .catch(function (error) {
                console.error("Error during registration or OTP:", error);
                $scope.errorMessage = error.data.message || "Đã xảy ra lỗi.";
            });
    };

    /* xác minh OTP */

    $scope.otp = ["", "", "", "", "", ""];
    $scope.success = false;

    // Lưu token khi nhận được OTP
    function storeToken(token) {
        sessionStorage.setItem('otpToken', token); // Lưu token vào SessionStorage
    }

    // Chuyển focus sang ô tiếp theo
    $scope.focusNext = function (event, index) {
        if ($scope.otp[index - 1] && index < 6) {
            document.querySelectorAll(".otp-input")[index].focus();
        }
    };

    $scope.logToken = function () {
        const token = sessionStorage.getItem('otpToken');
        console.log('Giá trị token hiện tại:', token);
    };

    // Hàm xác minh OTP
    $scope.verifyOtp = function () {
        const enteredOtp = $scope.otp.join('');
        const token = sessionStorage.getItem('otpToken');

        console.log('Token:', token);
        console.log('OTP Entered:', enteredOtp);

        if (!token || enteredOtp.length < 6) {
            $scope.errorMessage = 'Vui lòng nhập đầy đủ thông tin.';
            return;
        }

        const requestData = {
            token: token,
            otpCode: enteredOtp
        };

        $http.post('/api/auth/verify-otp', requestData)
            .then(function (response) {
                console.log('Server Response:', response.data);
                $scope.successMessage = response.data.message || 'OTP xác minh thành công!';
                $scope.errorMessage = '';
                $window.location.href = '/login/password';

            })
            .catch(function (error) {
                console.error('Server Error:', error);
                $scope.errorMessage = error.data.message || 'OTP không chính xác hoặc token hết hạn.';
                $scope.successMessage = '';
            });
    };

    // Hàm gửi lại OTP
    $scope.resendOtp = function () {
        console.log('Resend OTP triggered');

        const requestData = {
            email: 'user@example.com' // Thay bằng email thực tế của người dùng
        };

        $http.post('/api/auth/send-otp', requestData)
            .then(function (response) {
                console.log('OTP Token Resent:', response.data.otpToken);
                $scope.successMessage = 'Mã OTP đã được gửi lại!';
                $scope.errorMessage = '';
                storeToken(response.data.otpToken); // Lưu token mới
            })
            .catch(function (error) {
                console.error('Error Resending OTP:', error);
                $scope.errorMessage = 'Lỗi khi gửi lại mã OTP.';
                $scope.successMessage = '';
            });
    };

    /* Cài đặt mật khẩu */

    // Hàm cài đặt mật khẩu
    $scope.setupPassword = function () {
        const password = $scope.passwordFields[0].value;
        const confirmPassword = $scope.passwordFields[1].value;

        // Kiểm tra mật khẩu khớp
        if (password !== confirmPassword) {
            $scope.errorMessage = "Mật khẩu không khớp.";
            $scope.successMessage = "";
            return;
        }

        // Gửi dữ liệu đến backend
        const requestData = {
            token: sessionStorage.getItem("otpToken"), // Lấy token từ sessionStorage
            password: password,
            confirmPassword: confirmPassword
        };

        $http.post("/api/auth/setup-password", requestData)
            .then(function (response) {
                console.log("Response:", response.data);
                $scope.successMessage = response.data.message || "Cài đặt mật khẩu thành công!";
                $scope.errorMessage = "";
            })
            .catch(function (error) {
                // Xử lý lỗi với kiểm tra an toàn
                console.error("Error:", error);
                $scope.errorMessage = error.data?.message || "Lỗi khi cài đặt mật khẩu.";
                $scope.successMessage = "";
            });
    };

    // Khởi tạo trường nhập mật khẩu
    $scope.passwordFields = [
        {
            id: "password",
            name: "password",
            label: "Mật khẩu",
            placeholder: "Nhập mật khẩu mới",
            value: "",
            show: false,
            required: true,
            pattern: /^.{6,}$/,
            errorRequired: "Mật khẩu là bắt buộc.",
            errorPattern: "Mật khẩu phải có ít nhất 6 ký tự."
        },
        {
            id: "confirmPassword",
            name: "confirmPassword",
            label: "Xác nhận mật khẩu",
            placeholder: "Nhập lại mật khẩu",
            value: "",
            show: false,
            required: true,
            errorRequired: "Vui lòng xác nhận mật khẩu.",
            errorPattern: "Mật khẩu không khớp."
        }
    ];

    // Hiển thị/Ẩn mật khẩu
    $scope.togglePasswordVisibility = function (field) {
        field.show = !field.show;
    };

    // Hàm gửi form
    $scope.changePassword = function () {
        const [password, confirmPassword] = $scope.passwordFields.map(f => f.value);

        if (password !== confirmPassword) {
            $scope.errorMessage = "Mật khẩu không khớp.";
            $scope.successMessage = "";
            return;
        }

        // Mô phỏng gửi form thành công
        $scope.errorMessage = "";
        $scope.successMessage = "Mật khẩu đã được thay đổi thành công!";
    };
});

