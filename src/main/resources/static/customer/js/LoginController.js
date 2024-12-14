// Khởi tạo AngularJS module
const app = angular.module("loginApp", []);

app.controller("LoginController", function ($scope, $http, $window, $timeout) {
    $scope.loginData = {
        email: "",
        matKhau: ""
    };

    $scope.errorMessage = "";

    $scope.isLoginFormValid = function () {
        return $scope.isValidEmail($scope.loginData.email) && $scope.loginData.matKhau;
    };

    $scope.handleLogin = function () {
        // Reset thông báo lỗi
        $scope.errorMessage = '';

        // Kiểm tra email và mật khẩu
        if (!$scope.loginData.email || !$scope.isValidEmail($scope.loginData.email)) {
            $scope.errorMessage = "Email không hợp lệ. Phải có định dạng hợp lệ và kết thúc bằng @gmail.com.";
            return;
        }

        if (!$scope.loginData.matKhau) {
            $scope.errorMessage = "Mật khẩu không được để trống.";
            return;
        }

        // Gửi form đăng nhập nếu hợp lệ
        $http.post("http://localhost:8080/rest/khach-hang/login", $scope.loginData)
            .then(function (response) {
                console.log("Đăng nhập thành công:", response.data);
                localStorage.setItem("user", JSON.stringify(response.data));
                $window.location.href = "http://localhost:8080/index";
            })
            .catch(function (error) {
                console.error("Lỗi khi đăng nhập:", error);
                $scope.errorMessage = "Mật khẩu hoặc email không đúng.";
            });
    };

    /* đăng ký */

    $scope.userData = {};

    $scope.isValidName = function (name) {
        const nameRegex = /^[a-zA-Z\sÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪỬỮỰỲỴÝỶỸửữựỳỵỷỹ]+$/;
        return nameRegex.test(name);
    };

    $scope.isValidPhone = function (phone) {
        const phoneRegex = /^0\d{9,10}$/;
        return phoneRegex.test(phone);
    };

    $scope.isValidEmail = function (email) {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
        return emailRegex.test(email);
    };

    // Hàm đăng ký người
    $scope.registerUser = function () {
        // Reset thông báo lỗi
        $scope.errorMessage = '';
        $scope.successMessage = '';
        $scope.isEmailTaken = false;
        $scope.isPhoneTaken = false;

        // Kiểm tra thông tin người dùng
        if (!$scope.userData.hoTen || !$scope.userData.hoTen.trim()) {
            $scope.errorMessage = "Họ và tên không được để trống hoặc chứa toàn khoảng trắng.";
            return;
        }

        if (!$scope.isValidName($scope.userData.hoTen)) {
            $scope.errorMessage = "Họ và tên không hợp lệ. Không được chứa ký tự đặc biệt hoặc số.";
            return;
        }

        if (!$scope.userData.sdt || !$scope.userData.sdt.trim()) {
            $scope.errorMessage = "Số điện thoại không được để trống hoặc chứa toàn khoảng trắng.";
            return;
        }

        if (!$scope.isValidPhone($scope.userData.sdt)) {
            $scope.errorMessage = "Số điện thoại không hợp lệ. Phải bắt đầu bằng số 0 và có 10-11 số.";
            return;
        }

        if (!$scope.userData.email || !$scope.userData.email.trim()) {
            $scope.errorMessage = "Email không được để trống hoặc chứa toàn khoảng trắng.";
            return;
        }

        if (!$scope.isValidEmail($scope.userData.email)) {
            $scope.errorMessage = "Email không hợp lệ. Phải có đuôi @gmail.com và không chứa ký tự đặc biệt.";
            return;
        }

        // Gửi yêu cầu kiểm tra trùng email và số điện thoại
        const checkData = {
            email: $scope.userData.email.trim(),
            sdt: $scope.userData.sdt.trim()
        };

        $http.post('/api/auth/check-duplicate', checkData)
            .then(function (response) {
                $scope.isEmailTaken = response.data.emailTaken;
                $scope.isPhoneTaken = response.data.phoneTaken;

                if ($scope.isEmailTaken) {
                    $scope.errorMessage = "Email này đã được đăng ký.";
                    return;
                }
                if ($scope.isPhoneTaken) {
                    $scope.errorMessage = "Số điện thoại này đã được đăng ký.";
                    return;
                }

                // Nếu không có lỗi, gửi dữ liệu đăng ký
                const payload = {
                    taiKhoan: $scope.userData.taiKhoan.trim(),
                    hoTen: $scope.userData.hoTen.trim(),
                    email: $scope.userData.email.trim(),
                    sdt: $scope.userData.sdt.trim()
                };

                $http.post('/api/auth/register-info', $scope.userData)
                    .then(function (response) {
                        $scope.successMessage = response.data.message || "Thông tin cá nhân đã lưu.";
                        sessionStorage.setItem('userEmail', $scope.userData.email.trim()); // Lưu email vào sessionStorage
                        return $http.post('/api/auth/send-otp', {email: $scope.userData.email});
                    })
                    .then(function (response) {
                        const otpToken = response.data.otpToken;
                        sessionStorage.setItem('otpToken', otpToken);
                        $window.location.href = '/login/verificationOTP';
                    })
                    .catch(function (error) {
                        $scope.errorMessage = error.data.message || "Đã xảy ra lỗi.";
                    });

            })
            .catch(function (error) {
                console.error("Lỗi khi kiểm tra trùng email/số điện thoại:", error);
                $scope.errorMessage = "Không thể kiểm tra trùng email/số điện thoại.";
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
                $timeout(function () {
                    $window.location.href = '/login/password';
                }, 3000); //

            })
            .catch(function (error) {
                console.error('Server Error:', error);
                $scope.errorMessage = error.data.message || 'OTP không chính xác hoặc token hết hạn.';
                $scope.successMessage = '';
            });
    };

    // Hàm gửi lại OTP
    $scope.resendOtp = function () {
        // Kiểm tra xem email có tồn tại không
        const email = $scope.userData.email ? $scope.userData.email.trim() : sessionStorage.getItem('userEmail');

        if (!email) {
            $scope.errorMessage = 'Không thể gửi lại mã OTP. Vui lòng cung cấp email.';
            return;
        }

        console.log('Resend OTP triggered for email:', email);

        const requestData = {
            email: email
        };

        $http.post('/api/auth/send-otp', requestData)
            .then(function (response) {
                console.log('OTP Token Resent:', response.data.otpToken);
                $scope.successMessage = 'Mã OTP đã được gửi lại!';
                $scope.errorMessage = '';
                sessionStorage.setItem('otpToken', response.data.otpToken); // Lưu token mới
            })
            .catch(function (error) {
                console.error('Error Resending OTP:', error);
                $scope.errorMessage = 'Lỗi khi gửi lại mã OTP.';
                $scope.successMessage = '';
            });
    };

    /* Cài đặt mật khẩu */

    // Mật khẩu mạnh
    $scope.passwordStrength = function (password) {
        if (!password) return 'weak'; // Mật khẩu trống

        const strongRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$/; // Mạnh: ít nhất 8 ký tự, chữ hoa, chữ thường và số
        const mediumRegex = /^(?=.*[a-zA-Z])(?=.*[0-9]).{6,}$/; // Trung bình: ít nhất 6 ký tự, có chữ và số

        if (strongRegex.test(password)) return 'strong';
        if (mediumRegex.test(password)) return 'medium';
        return 'weak'; // Còn lại là yếu
    };

    // Hàm cài đặt mật khẩu
    $scope.setupPassword = function () {
        $scope.errorMessage = '';
        $scope.successMessage = '';

        const password = $scope.passwordFields[0].value;
        const confirmPassword = $scope.passwordFields[1].value;

        // Kiểm tra trống
        if (!password || !password.trim()) {
            $scope.errorMessage = "Mật khẩu không được để trống.";
            return;
        }

        if (!confirmPassword || !confirmPassword.trim()) {
            $scope.errorMessage = "Vui lòng nhập xác nhận mật khẩu.";
            return;
        }

        // Kiểm tra ký tự đặc biệt
        const specialCharRegex = /[^a-zA-Z0-9]/;
        if (specialCharRegex.test(password)) {
            $scope.errorMessage = "Mật khẩu không được chứa ký tự đặc biệt.";
            return;
        }

        if (specialCharRegex.test(confirmPassword)) {
            $scope.errorMessage = "Xác nhận mật khẩu không được chứa ký tự đặc biệt.";
            return;
        }

        // Kiểm tra độ khớp mật khẩu
        if (password !== confirmPassword) {
            $scope.errorMessage = "Mật khẩu và xác nhận mật khẩu không khớp.";
            return;
        }

        // Gửi dữ liệu nếu hợp lệ
        const requestData = {
            token: sessionStorage.getItem("otpToken"), // Lấy token từ sessionStorage
            password: password,
            confirmPassword: confirmPassword
        };

        $http.post("/api/auth/setup-password", requestData)
            .then(function (response) {
                console.log("Đổi mật khẩu thành công:", response.data);
                $scope.successMessage = response.data.message || "Mật khẩu đã được cài đặt thành công!";
                $timeout(function () {
                    $window.location.href = '/login';
                }, 3000); // Chờ 5 giây
            })
            .catch(function (error) {
                console.error("Lỗi khi đổi mật khẩu:", error);
                $scope.errorMessage = error.data?.message || "Lỗi hệ thống.";
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

