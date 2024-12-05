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
        $scope.khachHang = savedUser; // Lưu toàn bộ thông tin user vào khachHang
        $scope.khachHang.diaChiChiTiet = savedUser.diaChiKhachHang || [];
    } else {
        $scope.khachHang = { diaChiChiTiet: [] };
        alert("Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại!");
        $location.path("/login");
    }


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
        // Kiểm tra dữ liệu từ các ô input
        if (!$scope.name || !$scope.email || !$scope.phone || !$scope.ngaySinh) {
            alert("Vui lòng điền đầy đủ thông tin bắt buộc.");
            return;
        }

        const updatedProfile = {
            id: savedUser.id,
            ho_ten: $scope.name,
            email: $scope.email,
            sdt: $scope.phone,
            ngay_sinh: $scope.ngaySinh ? $scope.ngaySinh.toISOString().split('T')[0] : null,
            gioi_tinh: $scope.gioiTinh,
            diaChiChiTiet: $scope.khachHang.diaChiChiTiet
        };

        // Gửi dữ liệu lên API
        $http.put(`${API_BASE_URL}/khach-hang/update-profile`, updatedProfile)
            .then(function (response) {
                // Cập nhật dữ liệu mới vào localStorage và giao diện
                const updatedUser = { ...savedUser, ...response.data };
                localStorage.setItem("user", JSON.stringify(updatedUser));
                $scope.khachHang = updatedUser;

                // Hiển thị dữ liệu mới trên giao diện
                $scope.name = updatedUser.ho_ten;
                $scope.email = updatedUser.email;
                $scope.phone = updatedUser.sdt;
                $scope.ngaySinh = updatedUser.ngay_sinh ? new Date(updatedUser.ngay_sinh) : null;
                $scope.gioiTinh = updatedUser.gioi_tinh;

                // Hiển thị thông báo thành công
                alert("Cập nhật thông tin thành công!");
                $scope.isEditMode = false;
            })
            .catch(function (error) {
                // Hiển thị thông báo lỗi nếu API trả về lỗi
                console.error("Lỗi khi cập nhật:", error);
                alert("Lỗi cập nhật: " + (error.data?.message || "Đã xảy ra lỗi trong quá trình cập nhật"));
            });
    };

    /* địa chỉ */

    // Tải danh sách địa chỉ từ API
    const addressData = $scope.khachHang.diaChiChiTiet.map(address => ({
        id: address.id,
        diaChiChiTiet: address.diaChiChiTiet,
        trangThai: address.trangThai,
        state: address.state,
        version: address.version, // Gửi version hiện tại từ backend
    }));

    // Tải danh sách địa chỉ
    $scope.loadAddress = function () {
        $http.get(`${API_BASE_URL}/khach-hang/${savedUser.id}/addresses`)
            .then(function (response) {
                $scope.khachHang.diaChiChiTiet = response.data.map(address => ({
                    ...address,
                    state: "unchange", // Gắn trạng thái mặc định
                }));
                localStorage.setItem("user", JSON.stringify($scope.khachHang));
            })
            .catch(function (error) {
                alert("Lỗi khi tải địa chỉ: " + (error.data?.message || 'Đã xảy ra lỗi'));
            });
    };

    // Gọi loadAddress để tải địa chỉ khi trang được tải
    $scope.loadAddress();

    // Thêm địa chỉ mới
    $scope.addAddress = function () {
        if (!$scope.newAddress) {
            alert("Vui lòng nhập địa chỉ!");
            return;
        }

        $scope.khachHang.diaChiChiTiet.push({
            id: null, // Địa chỉ mới chưa có ID
            diaChiChiTiet: $scope.newAddress,
            trangThai: 0, // Không mặc định
            state: "new", // Trạng thái là "new"
            version: null // Không có phiên bản cho địa chỉ mới
        });

        localStorage.setItem("user", JSON.stringify($scope.khachHang));
        $scope.newAddress = ""; // Reset input
    };

    // Sửa địa chỉ
    $scope.editAddress = function (index, newDetail) {
        const address = $scope.khachHang.diaChiChiTiet[index];
        address.diaChiChiTiet = newDetail;
        if (address.state !== "new") {
            address.state = "edited"; // Chỉ thay đổi nếu không phải địa chỉ mới
        }
        localStorage.setItem("user", JSON.stringify($scope.khachHang));
    };

    // Xóa địa chỉ
    $scope.removeAddress = function (index) {
        const address = $scope.khachHang.diaChiChiTiet[index];

        if (address.state === "new") {
            // Xóa ngay lập tức nếu là địa chỉ mới
            $scope.khachHang.diaChiChiTiet.splice(index, 1);
        } else {
            // Đánh dấu là "deleted"
            address.state = "deleted";
        }

        localStorage.setItem("user", JSON.stringify($scope.khachHang));
    };

    // Gửi thay đổi lên server
    $scope.updateAddresses = function () {
        const changes = $scope.khachHang.diaChiChiTiet.filter(address => address.state !== "unchange");

        if (changes.length === 0) {
            alert("Không có thay đổi nào để cập nhật.");
            return;
        }

        $http.post(`${API_BASE_URL}/dia-chi/${savedUser.id}/sync-addresses`, changes)
            .then(function (response) {
                const results = response.data;

                results.forEach(result => {
                    if (result.state === "success") {
                        // Xóa trạng thái thay đổi cho các bản ghi thành công
                        const address = $scope.khachHang.diaChiChiTiet.find(addr => addr.id === result.id || addr.state === "new");
                        if (address) {
                            address.state = "unchange";
                            if (result.id) address.id = result.id; // Cập nhật ID cho địa chỉ mới
                        }
                    } else if (result.state === "conflict") {
                        alert(`Xung đột địa chỉ ID ${result.id}: ${result.message}`);
                        console.log("Dữ liệu mới nhất từ server:", result.updatedData);
                    } else if (result.state === "error") {
                        alert(`Lỗi đồng bộ địa chỉ ID ${result.id}: ${result.message}`);
                    }
                });

                // Tải lại dữ liệu từ server
                $scope.loadAddress();
            })
            .catch(function (error) {
                alert("Lỗi khi đồng bộ địa chỉ: " + (error.data?.message || 'Đã xảy ra lỗi'));
            });
    };

    // Cảnh báo khi rời trang nếu có thay đổi chưa lưu
    window.onbeforeunload = function () {
        if ($scope.khachHang.diaChiChiTiet.some(address => address.state !== "unchange")) {
            return "Bạn có thay đổi chưa lưu. Thoát sẽ mất dữ liệu.";
        }
    };

});