app.controller('ThongTinController', function ($scope, $http, $window, $location) {
    // Định nghĩa base URL cho API
    const API_BASE_URL = 'http://localhost:8080/rest';

    // Khởi tạo các biến
    $scope.isEditMode = false;
    $scope.isChangePasswordMode = false;
    $scope.passwordForm = {
        matKhauHienTai: '',
        matKhauMoi: '',
        xacNhanMatKhauMoi: ''
    };
    $scope.showCurrentPassword = false;
    $scope.showNewPassword = false;
    $scope.showConfirmPassword = false;
    $scope.errorMessage = '';
    $scope.successMessage = '';
    $scope.isLoading = false;

// Lấy thông tin user từ localStorage
    const savedUser = JSON.parse(localStorage.getItem("user"));
    if (savedUser) {
        $scope.name = savedUser.ho_ten;
        $scope.email = savedUser.email;
        $scope.phone = savedUser.sdt;
        $scope.ngaySinh = savedUser.ngay_sinh ? new Date(savedUser.ngay_sinh) : null;
        $scope.gioiTinh = Number(savedUser.gioi_tinh || 1);

        // Xử lý địa chỉ
        const addresses = savedUser.diaChiKhachHang || [];
        $scope.addressDetail = addresses.find(addr => addr.trang_thai === 1)?.dia_chi_chi_tiet || ""; // Địa chỉ mặc định
        $scope.otherAddressDetail = addresses.find(addr => addr.trang_thai === 0)?.dia_chi_chi_tiet || ""; // Địa chỉ không mặc định
    }

// Lưu thông tin địa chỉ
    $scope.saveAddress = function() {
        if (!$scope.addressDetail && !$scope.otherAddressDetail) {
            alert("Vui lòng điền đầy đủ thông tin địa chỉ.");
            return;
        }

        const updatedAddresses = [
            { trang_thai: 1, dia_chi_chi_tiet: $scope.addressDetail },
            { trang_thai: 0, dia_chi_chi_tiet: $scope.otherAddressDetail }
        ];

        // Gửi yêu cầu cập nhật
        $http.put(`${API_BASE_URL}/khach-hang/update-address`, { id: savedUser.id, diaChiKhachHang: updatedAddresses })
            .then(function(response) {
                alert("Cập nhật địa chỉ thành công!");
                // Cập nhật localStorage
                savedUser.diaChiKhachHang = updatedAddresses;
                localStorage.setItem("user", JSON.stringify(savedUser));
            })
            .catch(function(error) {
                alert("Lỗi: " + (error.data?.message || 'Đã xảy ra lỗi khi cập nhật địa chỉ'));
            });
    };


    // Toggle chế độ chỉnh sửa
    $scope.toggleEditMode = function () {
        $scope.isEditMode = !$scope.isEditMode;
        if (!$scope.isEditMode) {
            $scope.isChangePasswordMode = false;
        }
    };

    // Toggle chế độ đổi mật khẩu
    $scope.toggleChangePasswordMode = function() {
        $scope.isChangePasswordMode = !$scope.isChangePasswordMode;
        if ($scope.isChangePasswordMode) {
            $scope.isEditMode = true;
        }
        // Reset form
        $scope.passwordForm = {
            matKhauHienTai: '',
            matKhauMoi: '',
            xacNhanMatKhauMoi: ''
        };
        $scope.errorMessage = '';
        $scope.successMessage = '';
    };

    // Toggle hiển thị/ẩn mật khẩu
    $scope.togglePasswordVisibility = function(field) {
        switch(field) {
            case 'current':
                $scope.showCurrentPassword = !$scope.showCurrentPassword;
                break;
            case 'new':
                $scope.showNewPassword = !$scope.showNewPassword;
                break;
            case 'confirm':
                $scope.showConfirmPassword = !$scope.showConfirmPassword;
                break;
        }

    };
    console.log(savedUser);

    // Validate mật khẩu
    $scope.validatePassword = function() {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
        if (!$scope.passwordForm.matKhauMoi) {
            return 'Vui lòng nhập mật khẩu mới';
        }
        if (!passwordRegex.test($scope.passwordForm.matKhauMoi)) {
            return 'Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số';
        }
        if ($scope.passwordForm.matKhauMoi !== $scope.passwordForm.xacNhanMatKhauMoi) {
            return 'Mật khẩu xác nhận không khớp';
        }
        return '';
    };

    // Đổi mật khẩu
    $scope.changePassword = function() {
        $scope.errorMessage = $scope.validatePassword();
        if ($scope.errorMessage) {
            return;
        }

        $scope.isLoading = true;
        const changePasswordData = {
            customerId: savedUser.id,
            matKhauHienTai: $scope.passwordForm.matKhauHienTai,
            matKhauMoi: $scope.passwordForm.matKhauMoi,
            xacNhanMatKhauMoi: $scope.passwordForm.xacNhanMatKhauMoi
        };

        $http.post(`${API_BASE_URL}/khach-hang/change-password`, changePasswordData)
            .then(function(response) {
                $scope.successMessage = 'Đổi mật khẩu thành công!';
                $scope.passwordForm = {
                    matKhauHienTai: '',
                    matKhauMoi: '',
                    xacNhanMatKhauMoi: ''
                };
                $scope.isLoading = false;
                // Tự động ẩn form sau 3 giây
                setTimeout(() => {
                    $scope.$apply(() => {
                        $scope.toggleChangePasswordMode();
                    });
                }, 3000);
            })
            .catch(function(error) {
                $scope.errorMessage = error.data?.message || 'Đã xảy ra lỗi khi đổi mật khẩu';
                $scope.isLoading = false;
            });
    };

    // Lưu profile
    $scope.saveProfile = function () {
        if (!$scope.name || !$scope.phone || !$scope.ngaySinh || !$scope.addressDetail) {
            alert("Vui lòng điền đầy đủ thông tin.");
            return;
        }

        const updatedProfile = {
            id: savedUser.id,
            ho_ten: $scope.name,
            sdt: $scope.phone,
            ngay_sinh: $scope.ngaySinh,
            gioi_tinh: $scope.gioiTinh,
            dia_chi_chi_tiet: $scope.addressDetail,
        };

        $http.put(`${API_BASE_URL}/khach-hang/update-profile`, updatedProfile)
            .then(function (response) {
                // Cập nhật localStorage
                const updatedUser = {...savedUser, ...updatedProfile};
                localStorage.setItem("user", JSON.stringify(updatedUser));
                alert("Cập nhật thông tin thành công!");
                $scope.isEditMode = false;
            })
            .catch(function (error) {
                alert("Lỗi: " + (error.data?.message || 'Đã xảy ra lỗi khi cập nhật thông tin'));
            });
    };
});