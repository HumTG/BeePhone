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
        gioiTinh: 0,
        trangThai: 1,
        diaChiChiTiet: [],
        defaultAddress: null
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
        $http.get(host)
            .then(function (response) {
                $scope.kh = response.data.content;
            })
            .catch(function (error) {
                console.error("Lỗi khi tải dữ liệu:", error);
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
        if (!$scope.khachHang.matKhau) return toastr.warning("Vui lòng nhập mật khẩu!") && false;
        if (!$scope.khachHang.ngaySinh) return toastr.warning("Vui lòng chọn ngày sinh!") && false;
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
            matKhau: $scope.khachHang.matKhau,
            ngaySinh: $scope.khachHang.ngaySinh,
            gioiTinh: parseInt($scope.khachHang.gioiTinh) || 0,
            trangThai: parseInt($scope.khachHang.trangThai) || 1,
            diaChiChiTiet: $scope.khachHang.diaChiChiTiet.map(addr => ({
                diaChiChiTiet: addr.diaChiChiTiet,
                trangThai: addr.trangThai
            }))
        };

        $http.post(`${hosts}/add`, customerData)
            .then(function () {
                toastr.success("Khách hàng mới đã được thêm thành công!");
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
                $scope.khachHang.gioiTinh = String($scope.khachHang.gioiTinh); // chuyển thành chuỗi cho binding radio button
                $scope.khachHang.trangThai = String($scope.khachHang.trangThai); // chuyển thành chuỗi cho binding select option
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu khách hàng:', error);
            });
    };

    $scope.initializeEdit = function () {
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get("id");
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
            matKhau: $scope.khachHang.matKhau,
            ngaySinh: $scope.khachHang.ngaySinh,
            gioiTinh: parseInt($scope.khachHang.gioiTinh) || 0,
            trangThai: parseInt($scope.khachHang.trangThai) || 1,
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

    $scope.filterCustomers = function () {
        // Lấy giá trị từ các trường lọc
        const searchText = document.getElementById('search').value.toLowerCase();
        const dobFrom = document.getElementById('dob_from').value;
        const dobTo = document.getElementById('dob_to').value;
        const status = document.getElementById('status').value;
        const ageRange = [parseInt(document.getElementById('ageRange').value), 60];

        // Lọc dữ liệu khách hàng
        $scope.filteredCustomers = filterCustomers($scope.danhSachKhachHang, searchText, dobFrom, dobTo, status, ageRange);
    };

    $scope.filterCustomers = function () {
        // Lấy giá trị từ các trường lọc
        const searchText = document.getElementById('search').value.toLowerCase();
        const dobFrom = document.getElementById('dob_from').value;
        const dobTo = document.getElementById('dob_to').value;
        const status = document.getElementById('status').value;
        const ageRange = [parseInt(document.getElementById('ageRange').value), 60];

        // Lọc dữ liệu khách hàng
        $scope.filteredCustomers = filterCustomers($scope.danhSachKhachHang, searchText, dobFrom, dobTo, status, ageRange);

        // Hiển thị kết quả lọc trên console
        console.log('Kết quả lọc:', $scope.filteredCustomers);
    };

    function filterCustomers(customers, searchText, dobFrom, dobTo, status, ageRange) {
        return customers.filter(customer => {
            // Lọc theo tên và số điện thoại
            if (searchText && !(`${customer.hoTen.toLowerCase()} ${customer.sdt.toLowerCase()}`.includes(searchText.toLowerCase()))) {
                return false;
            }

            // Lọc theo ngày sinh
            const customerDob = new Date(customer.ngaySinh);
            if (dobFrom && customerDob < new Date(dobFrom)) {
                return false;
            }
            if (dobTo && customerDob > new Date(dobTo)) {
                return false;
            }

            // Lọc theo trạng thái
            if (status && customer.trangThai !== parseInt(status)) {
                return false;
            }

            // Lọc theo khoảng tuổi
            const customerAge = new Date().getFullYear() - customerDob.getFullYear();
            if (customerAge < ageRange[0] || customerAge > ageRange[1]) {
                return false;
            }

            // Nếu vượt qua tất cả các bộ lọc, giữ lại khách hàng này
            return true;
        });
    }


// Hàm làm mới bộ lọc
    $scope.resetFilters = function () {
        document.getElementById('search').value = '';
        document.getElementById('dob_from').value = '';
        document.getElementById('dob_to').value = '';
        document.getElementById('status').value = 'Tất cả';
        document.getElementById('ageRange').value = 18;

        $scope.filterCustomers(); // Gọi lại hàm lọc để hiển thị tất cả khách hàng
    };


});
