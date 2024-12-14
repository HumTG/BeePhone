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
    $scope.newAddress = "";
    $scope.addressList = [];
    $scope.editingAddress = {};


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
            // Reset lại form từ dữ liệu hiện tại
            $scope.taiKhoan = $scope.khachHang.taiKhoan;
            $scope.name = $scope.khachHang.hoTen;
            $scope.email = $scope.khachHang.email;
            $scope.phone = $scope.khachHang.sdt;
            $scope.ngaySinh = $scope.khachHang.ngaySinh ? new Date($scope.khachHang.ngaySinh) : null;
            $scope.gioiTinh = $scope.khachHang.gioiTinh;
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

    // Danh sách các trường mật khẩu
    $scope.passwordFields = [
        {
            id: "currentPassword",
            name: "currentPassword",
            model: "matKhauHienTai",
            label: "Mật khẩu hiện tại",
            placeholder: "Nhập mật khẩu hiện tại",
            required: true,
            errorRequired: "Mật khẩu hiện tại không được để trống.",
            show: false
        },
        {
            id: "newPassword",
            name: "newPassword",
            model: "matKhauMoi",
            label: "Mật khẩu mới",
            placeholder: "Nhập mật khẩu mới",
            required: true,
            pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/,
            errorRequired: "Mật khẩu mới không được để trống.",
            errorPattern: "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số.",
            show: false
        },
        {
            id: "confirmPassword",
            name: "confirmPassword",
            model: "xacNhanMatKhauMoi",
            label: "Xác nhận mật khẩu mới",
            placeholder: "Xác nhận mật khẩu mới",
            required: true,
            errorRequired: "Xác nhận mật khẩu không được để trống.",
            errorPattern: "Mật khẩu xác nhận không khớp.",
            show: false
        }
    ];

    // Toggle hiển thị/ẩn mật khẩu
    $scope.togglePasswordVisibility = function (field) {
        field.show = !field.show;
    };

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
        $scope.errorMessage = '';
        $scope.successMessage = '';
        $scope.isLoading = true;

        const changePasswordData = {
            customerId: savedUser.id,
            matKhauHienTai: $scope.passwordForm.matKhauHienTai,
            matKhauMoi: $scope.passwordForm.matKhauMoi,
            xacNhanMatKhauMoi: $scope.passwordForm.xacNhanMatKhauMoi
        };

        // Gửi yêu cầu đổi mật khẩu
        $http.post(`${API_BASE_URL}/khach-hang/change-password`, changePasswordData)
            .then(function (response) {
                // Ghi log thành công
                console.log("HTTP Success Status:", response.status);
                console.log("Response Data:", response.data);

                if (response.status === 200 && response.data.message) {
                    $scope.successMessage = response.data.message;
                    $scope.passwordForm = {
                        matKhauHienTai: '',
                        matKhauMoi: '',
                        xacNhanMatKhauMoi: ''
                    };
                } else {
                    $scope.errorMessage = "Đổi mật khẩu không thành công.";
                }
                $scope.isLoading = false;
            })
            .catch(function (error) {
                // Ghi log lỗi
                console.error("HTTP Error Status:", error?.status || "Không xác định");
                console.error("Error Data:", error?.data || "Không có dữ liệu lỗi");

                // Xử lý lỗi
                if (error?.status === 400 && error?.data?.message?.includes("Mật khẩu hiện tại không đúng")) {
                    $scope.errorMessage = "Mật khẩu hiện tại sai. Vui lòng thử lại.";
                } else {
                    $scope.errorMessage = error?.data?.message || "Đổi mật khẩu không thành công. Vui lòng thử lại sau.";
                }
                $scope.isLoading = false;
            });
    };

    // Lưu profile
    $scope.saveProfile = function () {
        // Kiểm tra dữ liệu từ các ô input
        if (!$scope.taiKhoan || !$scope.name || !$scope.email || !$scope.phone || !$scope.ngaySinh) {
            alert("Vui lòng điền đầy đủ thông tin bắt buộc.");
            return;
        }

        // Dữ liệu cập nhật gửi đến backend
        const updatedProfile = {
            id: savedUser.id,
            taiKhoan: $scope.taiKhoan,
            hoTen: $scope.name,
            email: $scope.email,
            sdt: $scope.phone,
            ngaySinh: $scope.ngaySinh ? $scope.ngaySinh.toISOString().split('T')[0] : null,
            gioiTinh: $scope.gioiTinh
        };

        // Gửi dữ liệu lên API
        $http.put(`${API_BASE_URL}/khach-hang/update-profile`, updatedProfile)
            .then(function (response) {
                // Đảm bảo API trả về dữ liệu cập nhật
                if (response.status === 200) {
                    // Cập nhật localStorage và giao diện với dữ liệu mới
                    const updatedUser = { ...savedUser, ...response.data };
                    localStorage.setItem("user", JSON.stringify(updatedUser));
                    $scope.khachHang = updatedUser;

                    // Hiển thị dữ liệu mới trên giao diện
                    $scope.taiKhoan = updatedUser.taiKhoan;
                    $scope.name = updatedUser.hoTen;
                    $scope.email = updatedUser.email;
                    $scope.phone = updatedUser.sdt;
                    $scope.ngaySinh = updatedUser.ngaySinh ? new Date(updatedUser.ngaySinh) : null;
                    $scope.gioiTinh = updatedUser.gioiTinh;

                    // Hiển thị thông báo thành công
                    alert("Cập nhật thông tin thành công!");
                    $scope.isEditMode = false; // Thoát chế độ chỉnh sửa
                } else {
                    alert("Cập nhật thất bại. Vui lòng thử lại.");
                }
            })
            .catch(function (error) {
                // Hiển thị thông báo lỗi nếu API trả về lỗi
                console.error("Lỗi khi cập nhật:", error);
                alert("Lỗi cập nhật: " + (error.data?.message || "Đã xảy ra lỗi trong quá trình cập nhật"));
            });
    };

    /* địa chỉ */

    const customerId = savedUser.id;

    // Lấy danh sách địa chỉ
    $scope.getAddresses = function () {
        $http.get(`${API_BASE_URL}/dia-chi/all`).then(function (response) {
            $scope.addressList = response.data;
        }, function (error) {
            console.error("Lỗi khi tải danh sách địa chỉ:", error);
        });
    };

    // Tải danh sách địa chỉ
    $scope.loadAddresses = function () {
        $http.get(`${API_BASE_URL}/dia-chi/${customerId}/all`)
            .then(function (response) {
                $scope.addressList = response.data;
            })
            .catch(function (error) {
                console.error("Lỗi khi tải danh sách địa chỉ:", error);
                $scope.showNotification("Không thể tải danh sách địa chỉ.", "danger");
            });
    };

    // Hiển thị thông báo
    $scope.showNotification = function (message, type = 'success') {
        $scope.notification = {
            message: message,
            class: type === 'success' ? 'alert-success' : 'alert-danger',
            show: true
        };

        // Tự động ẩn sau 3 giây
        setTimeout(() => {
            $scope.notification.show = false;
            $scope.$apply();
        }, 3000);
    };

    // Thay đổi thông tin địa chỉ
    $scope.updateAddressInfo = function () {
        const validationError = $scope.validateAddress($scope.editingAddress.diaChiChiTiet);
        if (validationError) {
            $scope.showNotification(validationError, "danger");
            return;
        }

        const updatedAddress = {
            diaChiChiTiet: $scope.editingAddress.diaChiChiTiet
        };

        $http.put(`/rest/dia-chi/update-info/${$scope.editingAddress.id}`, updatedAddress)
            .then(function (response) {
                $scope.showNotification("Cập nhật địa chỉ thành công!", "success");
                $scope.loadAddresses(); // Reload danh sách địa chỉ
                document.getElementById("editAddressModal").querySelector(".btn-close").click();
            })
            .catch(function (error) {
                console.error("Lỗi khi cập nhật địa chỉ:", error);
                $scope.showNotification("Đã xảy ra lỗi khi cập nhật địa chỉ.", "danger");
            });
    };

    // Thêm địa chỉ mới
    $scope.addAddress = function () {
        const validationError = $scope.validateAddress($scope.newAddress);
        if (validationError) {
            $scope.showNotification(validationError, "danger");
            return;
        }

        const newAddress = {
            diaChiChiTiet: $scope.newAddress
        };

        $http.post(`/rest/dia-chi/${customerId}/add`, newAddress)
            .then(function (response) {
                $scope.showNotification("Thêm địa chỉ thành công!", "success");
                $scope.newAddress = ""; // Reset input
                $scope.loadAddresses(); // Reload danh sách địa chỉ
            })
            .catch(function (error) {
                console.error("Lỗi khi thêm địa chỉ:", error);
                $scope.showNotification("Đã xảy ra lỗi khi thêm địa chỉ.", "danger");
            });
    };

    // Thay đổi trạng thái địa chỉ
    $scope.updateAddressState = function (addressId) {
        $http.put(`/rest/dia-chi/update-state/${customerId}/${addressId}`)
            .then(function (response) {
                $scope.showNotification("Cập nhật trạng thái địa chỉ thành công!", "success");
                $scope.loadAddresses();
            })
            .catch(function (error) {
                console.error("Lỗi khi cập nhật trạng thái địa chỉ:", error);
                $scope.showNotification("Đã xảy ra lỗi khi cập nhật trạng thái địa chỉ.", "danger");
            });
    };

    // Hiển thị modal chỉnh sửa địa chỉ
    $scope.editAddress = function (address) {
        $scope.editingAddress = angular.copy(address);
        new bootstrap.Modal(document.getElementById("editAddressModal")).show();
    };

    // Xóa địa chỉ
    $scope.removeAddress = function (addressId) {
        const confirmDelete = confirm("Bạn có chắc muốn xóa địa chỉ này không?");
        if (confirmDelete) {
            const deletedAddress = {
                id: addressId,
                state: "deleted"
            };

            $http.post(`/rest/dia-chi/${customerId}/sync-addresses`, [deletedAddress])
                .then(function (response) {
                    $scope.showNotification("Địa chỉ đã được xóa thành công!", "success");
                    $scope.loadAddresses(customerId);
                })
                .catch(function (error) {
                    console.error("Lỗi khi xóa địa chỉ:", error);
                    const errorMessage = error.data?.error || "Đã xảy ra lỗi khi xóa địa chỉ.";
                    $scope.showNotification(errorMessage, "danger");
                });
        }
    };

    // Tải danh sách địa chỉ khi khởi chạy
    $scope.loadAddresses();

    // Validate Địa chỉ
    $scope.validateAddress = function (address) {
        const addressRegex = /^[\p{L}\p{N}\s,.-]+$/u; // Regex: Chỉ cho phép chữ, số, dấu cách, dấu phẩy, gạch ngang, dấu chấm
        const parts = address.split(',').map(part => part.trim()); // Tách địa chỉ qua dấu phẩy

        if (!address || address.trim() === '') {
            return "Địa chỉ không được để trống.";
        }

        if (!addressRegex.test(address)) {
            return "Địa chỉ chứa ký tự không hợp lệ.";
        }

        if (address.length < 10) {
            return "Địa chỉ quá ngắn, vui lòng nhập ít nhất 10 ký tự.";
        }

        if (address.length > 255) {
            return "Địa chỉ quá dài, vui lòng nhập ít hơn 255 ký tự.";
        }

        if (parts.length < 2) {
            return "Địa chỉ phải có ít nhất 2 phần cách nhau bởi dấu phẩy (,).";
        }

        return null; // Nếu không có lỗi
    };

    // // Cảnh báo khi rời trang nếu có thay đổi chưa lưu
    // window.onbeforeunload = function () {
    //     if ($scope.khachHang.diaChiChiTiet.some(address => address.state !== "unchange")) {
    //         return "Bạn có thay đổi chưa lưu. Thoát sẽ mất dữ liệu.";
    //     }
    // };
});