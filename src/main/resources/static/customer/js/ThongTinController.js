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

    /* Thông tin */

    // Lấy thông tin user từ localStorage
    const savedUser = JSON.parse(localStorage.getItem("user"));
    if (savedUser) {
        $scope.name = savedUser.ho_ten;
        $scope.email = savedUser.email;
        $scope.phone = savedUser.sdt;
        $scope.taiKhoan = savedUser.tai_khoan;
        $scope.ngaySinh = savedUser.ngay_sinh ? new Date(savedUser.ngay_sinh) : null;
        $scope.gioiTinh = savedUser.gioi_tinh || 0; // 0: Nam, 1: Nữ
        $scope.gioiTinhText = savedUser.gioi_tinh === 1 ? 'Nữ' : 'Nam';
        $scope.khachHang = savedUser; // Lưu toàn bộ thông tin user vào khachHang
        $scope.khachHang.diaChiChiTiet = savedUser.diaChiKhachHang || []; // Đảm bảo danh sách địa chỉ tồn tại
    } else {
        // Nếu không có user, thông báo và chuyển hướng
        $scope.khachHang = {diaChiChiTiet: []};
        alert("Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại!");
        $location.path('/login'); // Điều hướng về trang đăng nhập
    }

    // Đồng bộ giá trị giới tính
    $scope.syncGender = function () {
        if ($scope.gioiTinhText.toLowerCase() === 'nữ') {
            $scope.gioiTinh = 1;
        } else if ($scope.gioiTinhText.toLowerCase() === 'nam') {
            $scope.gioiTinh = 0;
        } else {
            alert('Giới tính không hợp lệ. Vui lòng nhập "Nam" hoặc "Nữ".');
            return false;
        }
        return true;
    };


    // Toggle chế độ chỉnh sửa
    $scope.toggleEditMode = function () {
        $scope.isEditMode = !$scope.isEditMode;
        if (!$scope.isEditMode) {
            $scope.isChangePasswordMode = false;
        }
    };
    // Toggle chế độ đổi mật khẩu
    $scope.toggleChangePasswordMode = function () {
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
    $scope.togglePasswordVisibility = function (field) {
        switch (field) {
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
    $scope.validatePassword = function () {
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
    $scope.changePassword = function () {
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
            .then(function (response) {
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
            .catch(function (error) {
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
        if (!$scope.syncGender()) {
            return; // Dừng lưu nếu giới tính không hợp lệ
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

    /* địa chỉ */

    // Tải danh sách địa chỉ từ API
    $scope.loadAddress = function () {
        $http.get(`${API_BASE_URL}/khach-hang/${savedUser.id}/addresses`)
            .then(function (response) {
                $scope.khachHang.diaChiChiTiet = response.data; // Lấy từ API
                savedUser.diaChiKhachHang = $scope.khachHang.diaChiChiTiet; // Đồng bộ lại localStorage
                localStorage.setItem("user", JSON.stringify(savedUser));
            })
            .catch(function (error) {
                alert("Lỗi khi tải danh sách địa chỉ: " + (error.data?.message || 'Đã xảy ra lỗi'));
            });
    };

    $scope.logAddresses = function () {
        if ($scope.khachHang && $scope.khachHang.diaChiChiTiet.length > 0) {
            console.log("Danh sách địa chỉ của khách hàng:");
            $scope.khachHang.diaChiChiTiet.forEach((address, index) => {
                console.log(`${index + 1}. ${address.dia_chi_chi_tiet} ${address.trang_thai === 1 ? "(Mặc định)" : ""}`);
            });
        } else {
            console.log("Không có địa chỉ nào được lưu.");
        }
    };

    // Gọi loadAddress để tải địa chỉ khi trang được tải
    $scope.loadAddress();

    // Thêm địa chỉ mới
    $scope.addAddress = function () {
        if (!$scope.newAddress) {
            alert("Vui lòng nhập địa chỉ!");
            return;
        }
        const addressData = { diaChiChiTiet: $scope.newAddress };
        $http.post(`${API_BASE_URL}/khach-hang/${savedUser.id}/add-address`, addressData)
            .then(function (response) {
                $scope.khachHang.diaChiChiTiet.push(response.data); // Thêm địa chỉ mới vào danh sách
                savedUser.diaChiKhachHang = $scope.khachHang.diaChiChiTiet; // Đồng bộ lại localStorage
                localStorage.setItem("user", JSON.stringify(savedUser));
                $scope.newAddress = ""; // Reset input
                alert("Thêm địa chỉ thành công!");
            })
            .catch(function (error) {
                alert("Lỗi khi thêm địa chỉ: " + (error.data?.message || 'Đã xảy ra lỗi'));
            });
    };

    // Đặt địa chỉ mặc định
    $scope.setDefaultAddress = function (index) {
        const addressId = $scope.khachHang.diaChiChiTiet[index].id;
        const customerId = savedUser.id;

        $http.post(`${API_BASE_URL}/khach-hang/update-default-address?customerId=${customerId}&addressId=${addressId}`)
            .then(function () {
                // Cập nhật trạng thái trong danh sách địa chỉ
                $scope.khachHang.diaChiChiTiet.forEach(addr => addr.trang_thai = 1); // Tất cả là không mặc định
                $scope.khachHang.diaChiChiTiet[index].trang_thai = 0; // Địa chỉ được chọn là mặc định

                // Đồng bộ lại localStorage
                savedUser.diaChiKhachHang = $scope.khachHang.diaChiChiTiet;
                localStorage.setItem("user", JSON.stringify(savedUser));

                alert("Cập nhật địa chỉ mặc định thành công!");
            })
            .catch(function (error) {
                alert("Lỗi khi cập nhật địa chỉ mặc định: " + (error.data?.message || 'Đã xảy ra lỗi'));
            });
    };

    // Xóa địa chỉ
    $scope.removeAddress = function (index) {
        const Id = $scope.khachHang.diaChiChiTiet[index].id;

        $http.delete(`${API_BASE_URL}/dia-chi/delete-address/${Id}`)
            .then(function () {
                $scope.khachHang.diaChiChiTiet.splice(index, 1); // Xóa địa chỉ khỏi danh sách
                if ($scope.khachHang.diaChiChiTiet.length > 0 && $scope.khachHang.diaChiChiTiet[0].trang_thai === 1) {
                    $scope.khachHang.diaChiChiTiet[0].trang_thai = 1; // Đặt địa chỉ đầu tiên làm mặc định nếu cần
                }
                alert("Xóa địa chỉ thành công!");
            })
            .catch(function (error) {
                alert("Lỗi khi xóa địa chỉ: " + (error.data?.message || 'Đã xảy ra lỗi'));
            });
    };

    // Cập nhật toàn bộ địa chỉ
    $scope.updateAddresses = function () {
        if (!$scope.khachHang || !$scope.khachHang.diaChiChiTiet) {
            alert("Không có dữ liệu địa chỉ để cập nhật.");
            return;
        }

        $scope.isLoading = true; // Hiển thị trạng thái loading

        const addressData = $scope.khachHang.diaChiChiTiet.map(address => ({
            id: address.id || null, // id nếu có, null nếu là địa chỉ mới
            diaChiChiTiet: address.dia_chi_chi_tiet,
            trangThai: address.trang_thai
        }));

        $http.post(`${API_BASE_URL}/dia-chi/${savedUser.id}/update-addresses`, addressData)
            .then(function (response) {
                alert("Cập nhật thông tin thành công!");
                $scope.loadAddress(); // Tải lại địa chỉ mới
            })
            .catch(function (error) {
                alert("Lỗi khi cập nhật địa chỉ: " + (error.data?.message || 'Đã xảy ra lỗi'));
            })
            .finally(function () {
                $scope.isLoading = false; // Tắt trạng thái loading
            });
    };

});