var app = angular.module('KhachHangApp', []);
var host = "http://localhost:8080/rest/khach-hang/all";

app.controller('KhachHangController', function ($scope, $http, $window, $location) {
    var hosts = "http://localhost:8080/rest/khach-hang";


    $scope.isSubmitting = false;

    $scope.isEditing = false;

    $scope.danhSachKhachHang = [];

    $scope.khachHang = {
        maKhachHang: '',
        taiKhoan: '',
        hoTen: '',
        email: '',
        sdt: '',
        matKhau: '',
        ngaySinh: '',
        gioiTinh: '',
        trangThai: '',
        diaChiChiTiet: [],
        defaultAddress: null
    };

    // Thêm các biến pagination vào controller
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.totalItems = 0;
    $scope.totalPages = 0;
    $scope.Math = window.Math;

    $scope.filters = {
        search: '',
        dobFrom: null,
        dobTo: null,
        status: '',
        ageRange: 18
    };

    var successMessage = localStorage.getItem('successMessage');
    if (successMessage) {
        toastr.success(successMessage, 'Thành công', {closeButton: true, progressBar: true, timeOut: 3000});
        localStorage.removeItem('successMessage');
    }

    $scope.newCustomer = {hoTen: '', email: '', sdt: '', diaChiChiTiet: []};
    $scope.currentPage = 0;

    // Lấy danh sách khách hàng
    $scope.getAllData = function () {
        $http.get(`${hosts}/list`)
            .then(function (response) {
                $scope.danhSachKhachHang = response.data;
                // Chuyển ngày sinh từ chuỗi thành Date object
                $scope.danhSachKhachHang.forEach(kh => {
                    if (kh.ngaySinh) kh.ngaySinh = new Date(kh.ngaySinh);
                });
            })
            .catch(function (error) {
                console.error('Lỗi khi tải dữ liệu:', error);
            });
    };
    $scope.getAllData();

    $scope.getData = function () {
        const url = `${host}?page=${$scope.currentPage}&size=${$scope.pageSize}`;
        $http.get(url)
            .then(function (response) {
                $scope.originalData = response.data.content;
                $scope.kh = [...$scope.originalData];
                $scope.totalItems = response.data.totalElements;
                $scope.totalPages = response.data.totalPages;
            })
            .catch(function (error) {
                console.error("Lỗi khi tải dữ liệu:", error);
                toastr.error("Không thể tải dữ liệu khách hàng");
            });
    };
    $scope.getData();

    $scope.openAddForm = function () {
        $scope.newCustomer = {};
        $('#addCustomerModal').modal('show');
    };

    $scope.closeAddForm = function () {
        $('#addCustomerModal').modal('hide');
    };

    $scope.address = {};
    $scope.addAddress = function () {
        if ($scope.address.diaChi) {
            const isDefault = $scope.khachHang.diaChiChiTiet.length === 0;
            $scope.khachHang.diaChiChiTiet.push({
                diaChiChiTiet: $scope.address.diaChi,
                trangThai: isDefault ? 1 : 0
            });
            $scope.address = {};
        } else {
            toastr.warning("Vui lòng nhập địa chỉ.");
        }
    };

    $scope.validateCustomerData = function () {
        if (!$scope.khachHang.hoTen) return toastr.warning("Vui lòng nhập họ tên!") && false;
        if (!$scope.khachHang.taiKhoan) return toastr.warning("Vui lòng nhập tài khoản!") && false;
        if (!$scope.khachHang.email) return toastr.warning("Vui lòng nhập email!") && false;
        if (!$scope.khachHang.sdt) return toastr.warning("Vui lòng nhập số điện thoại!") && false;
        if (!$scope.khachHang.ngaySinh) return toastr.warning("Vui lòng chọn ngày sinh!") && false;
        if ($scope.khachHang.gioiTinh === undefined || $scope.khachHang.gioiTinh === '') {
            toastr.warning("Vui lòng chọn giới tính!");
            return false;
        }
        if (!$scope.khachHang.diaChiChiTiet.length) return toastr.warning("Vui lòng thêm ít nhất một địa chỉ!") && false;
        return true;
    };

    $scope.addCustomer = function () {
        if (!$scope.validateCustomerData()) return;

        const customerData = {
            taiKhoan: $scope.khachHang.taiKhoan,
            hoTen: $scope.khachHang.hoTen,
            email: $scope.khachHang.email,
            sdt: $scope.khachHang.sdt,
            ngaySinh: $scope.khachHang.ngaySinh,
            gioiTinh: $scope.khachHang.gioiTinh === '1' ? 1 : 0,
            trangThai: parseInt($scope.khachHang.trangThai) || 0,
            diaChiChiTiet: $scope.khachHang.diaChiChiTiet.map(addr => ({
                diaChiChiTiet: addr.diaChiChiTiet,
                trangThai: addr.trangThai
            }))
        };

        $http.post(`${hosts}/add`, customerData)
            .then(function (response) {
                // Hiển thị mật khẩu được tạo tự động
                if (response.data && response.data.matKhau) {
                    toastr.success(`Khách hàng mới đã được thêm thành công!\nMật khẩu tự động: ${response.data.matKhau}`);
                } else {
                    toastr.success("Khách hàng mới đã được thêm thành công!");
                }
                $window.location.href = '/admin/khach-hang';
            })
            .catch(function (error) {
                console.error("Lỗi khi thêm khách hàng:", error);
                toastr.error(error.data.message || "Có lỗi xảy ra khi thêm khách hàng mới.");
            });
    };

    $scope.setDefaultAddress = function (index) {
        $scope.khachHang.diaChiChiTiet.forEach((addr, i) => {
            addr.trangThai = (i === index) ? 1 : 0;
        });
    };

    $scope.removeAddress = function (index) {
        const removedAddress = $scope.khachHang.diaChiChiTiet[index];
        $scope.khachHang.diaChiChiTiet.splice(index, 1);
        if (removedAddress.trangThai === 1 && $scope.khachHang.diaChiChiTiet.length > 0) {
            $scope.khachHang.diaChiChiTiet[0].trangThai = 1;
        }
    };

    $scope.openAddCustomerPage = function () {
        $window.location.href = "/admin/create-khach-hang.html";
    };

    $scope.openDetail = function (id) {
        $http.get(`${hosts}/${id}`)
            .then(function (response) {
                $scope.khct = response.data;
                if ($scope.khct.ngaySinh) $scope.khct.ngaySinh = new Date($scope.khct.ngaySinh);
                $scope.defaultAddress = $scope.khct.diaChiChiTiet.find(addr => addr.trangThai === 1) || null;
                $scope.otherAddresses = $scope.khct.diaChiChiTiet.filter(addr => addr.trangThai === 0) || [];
                $('#detailModal').modal('show');
            })
            .catch(function (error) {
                console.error("Không tìm thấy khách hàng", error);
                toastr.error("Không thể tải thông tin khách hàng");
            });
    };

    $scope.navigateToEditPage = function (id) {
        window.location.href = `/admin/create-khach-hang?id=${id}`;
    };

    $scope.loadCustomerData = function (id) {
        $http.get(`${hosts}/${id}`)
            .then(function (response) {
                $scope.khachHang = response.data;
                if ($scope.khachHang.ngaySinh) $scope.khachHang.ngaySinh = new Date($scope.khachHang.ngaySinh);
                // Đảm bảo hiển thị đúng giới tính và trạng thái
                $scope.khachHang.gioiTinh = $scope.khachHang.gioiTinh === 1 ? '1' : '0'; // chuyển thành chuỗi cho binding radio button
                $scope.khachHang.trangThai = String($scope.khachHang.trangThai === 0 ? '0' : '1'); // chuyển thành chuỗi cho binding select option
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu khách hàng:', error);
            });
    };

    $scope.initializeEdit = function () {
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get("id");

        // Mặc định set trạng thái là "Kích Hoạt" (1) khi không ở chế độ chỉnh sửa
        if (!id) {
            $scope.khachHang.trangThai = '1';  // Set giá trị mặc định là "Kích Hoạt"
        }

        if (id) {
            $scope.isEditing = true;
            $scope.loadCustomerData(id);
        }
    };

    $scope.updateCustomer = function () {
        if (!$scope.validateCustomerData()) return;

        if (!$scope.khachHang.id || !$scope.khachHang.maKhachHang) {
            toastr.error("Không thể cập nhật vì thiếu ID hoặc mã khách hàng.");
            return;
        }

        const customerData = {
            id: $scope.khachHang.id,
            maKhachHang: $scope.khachHang.maKhachHang,
            taiKhoan: $scope.khachHang.taiKhoan,
            hoTen: $scope.khachHang.hoTen,
            email: $scope.khachHang.email,
            sdt: $scope.khachHang.sdt,
            ngaySinh: $scope.khachHang.ngaySinh,
            gioiTinh: $scope.khachHang.gioiTinh === '1' ? 1 : 0,
            trangThai: parseInt($scope.khachHang.trangThai) || 0,
            diaChiChiTiet: $scope.khachHang.diaChiChiTiet.map(addr => ({
                diaChiChiTiet: addr.diaChiChiTiet,
                trangThai: addr.trangThai
            }))
        };

        $http.put(`${hosts}/update`, customerData)
            .then(function () {
                toastr.success("Thông tin khách hàng đã được cập nhật thành công!");
                $window.location.href = '/admin/khach-hang';
            })
            .catch(function (error) {
                console.error("Lỗi khi cập nhật khách hàng:", error);
                toastr.error("Có lỗi xảy ra khi cập nhật thông tin khách hàng.");
            });
    };

    // Hàm tính tuổi từ ngày sinh
    function calculateAge(birthday) {
        const today = new Date();
        const birthDate = new Date(birthday);
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();

        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
            age--;
        }
        return age;
    }

    $scope.filters = {
        search: '',
        dobFrom: null,
        dobTo: null,
        status: '',
        ageRange: 18
    };

    // Hàm tính tuổi
    function calculateAge(birthday) {
        if (!birthday) return 0;
        const today = new Date();
        const birthDate = new Date(birthday);
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();

        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
            age--;
        }
        return age;
    }

    // Hàm lọc khách hàng
    $scope.filterCustomers = function () {
        // Bắt đầu với dữ liệu gốc
        let filteredCustomers = [...$scope.originalData];

        // Lọc theo tên hoặc số điện thoại
        if ($scope.filters.search) {
            const searchLower = $scope.filters.search.toLowerCase();
            filteredCustomers = filteredCustomers.filter(customer =>
                (customer.hoTen && customer.hoTen.toLowerCase().includes(searchLower)) ||
                (customer.sdt && customer.sdt.includes($scope.filters.search))
            );
        }

        // Lọc theo ngày sinh từ
        if ($scope.filters.dobFrom) {
            const dobFrom = new Date($scope.filters.dobFrom);
            dobFrom.setHours(0, 0, 0, 0);
            filteredCustomers = filteredCustomers.filter(customer => {
                if (!customer.ngaySinh) return false;
                const customerDob = new Date(customer.ngaySinh);
                return customerDob >= dobFrom;
            });
        }

        // Lọc theo ngày sinh đến
        if ($scope.filters.dobTo) {
            const dobTo = new Date($scope.filters.dobTo);
            dobTo.setHours(23, 59, 59, 999);
            filteredCustomers = filteredCustomers.filter(customer => {
                if (!customer.ngaySinh) return false;
                const customerDob = new Date(customer.ngaySinh);
                return customerDob <= dobTo;
            });
        }

        // Lọc theo trạng thái
        if ($scope.filters.status !== '') {
            const statusValue = parseInt($scope.filters.status);
            if (statusValue === 0) {
                filteredCustomers = filteredCustomers.filter(customer => customer.trangThai === 0);
            } else if (statusValue === 1) {
                filteredCustomers = filteredCustomers.filter(customer => customer.trangThai === 1);
            }
        }

        // Lọc theo khoảng tuổi
        if ($scope.filters.ageRange) {
            filteredCustomers = filteredCustomers.filter(customer => {
                const age = calculateAge(customer.ngaySinh);
                return age <= $scope.filters.ageRange;
            });
        }

        // Cập nhật dữ liệu hiển thị
        $scope.kh = filteredCustomers;
        $scope.totalItems = filteredCustomers.length;
        $scope.totalPages = Math.ceil($scope.totalItems / $scope.pageSize);

        // Đảm bảo currentPage hợp lệ
        if ($scope.currentPage >= $scope.totalPages) {
            $scope.currentPage = Math.max(0, $scope.totalPages - 1);
        }

        // Phân trang kết quả
        const start = $scope.currentPage * $scope.pageSize;
        const end = start + $scope.pageSize;
        $scope.kh = filteredCustomers.slice(start, end);

        // Áp dụng $scope.$apply() nếu cần thiết
        if (!$scope.$$phase) {
            $scope.$apply();
        }
    };

    // Hàm reset bộ lọc
    $scope.resetFilters = function () {
        $scope.filters = {
            search: '',
            dobFrom: null,
            dobTo: null,
            status: '',
            ageRange: 18
        };
        // Reset lại danh sách hiển thị về dữ liệu gốc
        $scope.kh = [...$scope.originalData];
    };

    // Theo dõi thay đổi của thanh trượt tuổi
    $scope.$watch('filters.ageRange', function (newValue) {
        if (document.getElementById('ageRangeValue')) {
            document.getElementById('ageRangeValue').textContent = newValue;
        }
    });

    // Hàm để chuyển trang
    $scope.changePage = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getData($scope.currentPage);
        }
    };

    // Kiểm tra email trùng
    $scope.checkEmailDuplicate = function () {
        if (!$scope.khachHang.email) return;

        $http.get(`${hosts}/check-email`, { params: { email: $scope.khachHang.email } })
            .then(function (response) {
                $scope.emailDuplicate = response.data.exists;
            })
            .catch(function (error) {
                console.error("Lỗi kiểm tra email:", error);
                $scope.emailDuplicate = false; // Trong trường hợp lỗi, mặc định là không trùng.
            });
    };

    // Check số điện thoại
    $scope.validatePhoneNumber = function () {
        const phoneRegex = /^0[0-9]{9,10}$/;
        if (!$scope.khachHang.sdt || !phoneRegex.test($scope.khachHang.sdt)) {
            $scope.phoneError = "Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và có 10-11 số.";
            return false;
        }
        $scope.phoneError = null;
        return true;
    };

    // Check ngày sinh
    $scope.validateDateOfBirth = function () {
        const now = new Date();
        const minDate = new Date(now.getFullYear() - 110, now.getMonth(), now.getDate());
        const maxDate = new Date(now.getFullYear() - 13, now.getMonth(), now.getDate());

        const dob = new Date($scope.khachHang.ngaySinh);

        if (dob < minDate || dob > maxDate) {
            $scope.dateOfBirthError = "Ngày sinh phải từ 13 tuổi trở lên và không vượt quá 110 năm.";
            return false;
        }
        $scope.dateOfBirthError = null;
        return true;
    };

    // Check địa chỉ
    $scope.validateAddress = function () {

        // Biểu thức regex kiểm tra định dạng: cho phép cả chữ và số, cách nhau bởi dấu phẩy
        const addressRegex = /^[a-zA-ZÀ-ỹ0-9\s]+,\s*[a-zA-ZÀ-ỹ0-9\s]+,\s*[a-zA-ZÀ-ỹ0-9\s]+$/;

        if (!$scope.address.diaChi || !addressRegex.test($scope.address.diaChi)) {
            $scope.addressError = "Địa chỉ không hợp lệ. Định dạng: Thành phố, Quận/Huyện, Xã.";
            return false;
        }
        $scope.addressError = null; // Không có lỗi
        return true;
    };

    // Gọi để check
    $scope.validateAllFields = function () {
        let isValid = true;

        // Kiểm tra email trùng
        if ($scope.emailDuplicate) {
            isValid = false;
        }

        // Kiểm tra số điện thoại
        if (!$scope.validatePhoneNumber()) {
            isValid = false;
        }

        // Kiểm tra ngày sinh
        if (!$scope.validateDateOfBirth()) {
            isValid = false;
        }

        // Kiểm tra địa chỉ
        if (!$scope.khachHang.diaChiChiTiet.length) {
            $scope.addressError = "Phải thêm ít nhất một địa chỉ.";
            isValid = false;
        } else {
            $scope.addressError = null; // Không có lỗi nếu danh sách địa chỉ không rỗng
        }

        return isValid;
    };

    // Gửi form
    $scope.submitForm = function () {
        $scope.formErrors = []; // Reset lỗi mỗi lần gửi

        // Kiểm tra email trùng lặp
        if ($scope.emailDuplicate) {
            $scope.formErrors.push("Email đã tồn tại. Vui lòng sử dụng email khác.");
        }

        // Các kiểm tra khác
        if (!$scope.khachHang.hoTen || $scope.khachHang.hoTen.trim() === '') {
            $scope.formErrors.push("Họ và tên không được để trống.");
        }
        if (!$scope.khachHang.taiKhoan || $scope.khachHang.taiKhoan.trim() === '') {
            $scope.formErrors.push("Tài khoản không được để trống.");
        }
        if (!$scope.khachHang.ngaySinh) {
            $scope.formErrors.push("Ngày sinh không được để trống.");
        }
        if (!$scope.khachHang.email || $scope.khachHang.email.trim() === '') {
            $scope.formErrors.push("Email không được để trống.");
        } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test($scope.khachHang.email)) {
            $scope.formErrors.push("Email không hợp lệ.");
        }
        if (!$scope.khachHang.sdt || $scope.khachHang.sdt.trim() === '') {
            $scope.formErrors.push("Số điện thoại không được để trống.");
        }
        if ($scope.khachHang.diaChiChiTiet.length === 0) {
            $scope.formErrors.push("Phải thêm ít nhất một địa chỉ.");
        }

        // Nếu có lỗi, không thực hiện thêm hoặc cập nhật
        if ($scope.formErrors.length > 0) {
            return;
        }

        // Nếu không có lỗi, thực hiện thêm mới hoặc cập nhật
        if ($scope.isEditing) {
            $scope.updateCustomer();
        } else {
            $scope.addCustomer();
        }
    };

});
