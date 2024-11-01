var app = angular.module('KhachHangApp', []);
var host = "http://localhost:8080/rest/khach-hang";

app.controller('KhachHangController', function ($scope, $http) {
    // $scope.kh = []; // Khởi tạo danh sách khách hàng
    // $scope.newCustomer = {}; // Dữ liệu khách hàng mới
    // $scope.khct = {}; // Dữ liệu chi tiết khách hàng
    // $scope.selectedKhachHang = {}; // Dữ liệu khách hàng được chọn để cập nhật

    var successMessage = localStorage.getItem('successMessage');
    if (successMessage) {
        // Hiển thị toastr thông báo
        toastr.success(successMessage, 'Thành công', {
            closeButton: true,
            progressBar: true,
            timeOut: 3000
        });

        // Xóa thông báo sau khi hiển thị
        localStorage.removeItem('successMessage');
    }

    // Khởi tạo tham số phân trang
    $scope.currentPage = 0;

    // Hàm để lấy dữ liệu từ API
    $scope.getData = function(page) {
        $http.get(host, { params: { page: page } })
            .then(function(response) {
                // Lưu dữ liệu vào scope để hiển thị
                $scope.kh = response.data.content; // 'content' là nơi chứa danh sách sản phẩm
                $scope.totalPages = response.data.totalPages; // Số trang tổng cộng
            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
    };

    // Gọi hàm để lấy dữ liệu trang đầu tiên
    $scope.getData($scope.currentPage);

    // Hàm để chuyển trang
    $scope.changePage = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getData($scope.currentPage);
        }
    };

    // Hàm để lấy danh sách khách hàng
    $scope.getData = function () {
        $http.get(host)
            .then(function (response) {
                $scope.kh = response.data.content; // Lưu danh sách khách hàng vào scope
            })
            .catch(function (error) {
                console.error("Lỗi khi tải dữ liệu:", error);
            });
    };

    // Gọi hàm lấy dữ liệu khi trang được tải
    $scope.getData();

    // Mở form thêm mới khách hàng
    $scope.openAddForm = function () {
        $scope.newCustomer = {}; // Đặt lại dữ liệu khách hàng mới
        $('#addCustomerModal').modal('show');
    };

    // Đóng form thêm mới khách hàng
    $scope.closeAddForm = function () {
        $('#addCustomerModal').modal('hide'); // Đóng modal đúng ID
    };

    // Thêm khách hàng
    $scope.confirmAddKhachHang = function() {
        var khachHangData = {
            ho_ten: $scope.khachHang.ho_ten,
            email: $scope.khachHang.email,
            sdt: $scope.khachHang.sdt,
            ngay_sinh: new Date($scope.khachHang.ngay_sinh).toISOString().split('T')[0],
            gioi_tinh: $scope.khachHang.gioi_tinh,
            dia_chi: $scope.khachHang.dia_chi,
            trang_thai: $scope.khachHang.trang_thai,
            // hinh_anh: $scope.imageName
        };

        $http.post('http://localhost:8080/rest/khach-hang', khachHangData)
            .then(function(response) {
                localStorage.setItem('successMessage', 'Khách hàng đã được thêm thành công!');
                window.location.href = 'http://localhost:8080/admin/khach-hang';
            })
            .catch(function(error) {
                console.error('Lỗi khi thêm khách hàng:', error);
                toastr.error('Có lỗi xảy ra!', 'Error', {
                    closeButton: true,
                    progressBar: true,
                    timeOut: 3000
                });
            });

        // Ẩn modal sau khi xác nhận
        var modalElement = document.getElementById('confirmModal');
        var modal = bootstrap.Modal.getInstance(modalElement);
        modal.hide();
    };

    // Mở trang thêm mới khách hàng
    $scope.openAddCustomerPage = function () {
        $window.location.href = "/admin/create-khach-hang.html";
    };

    // Hàm để mở modal và hiển thị thông tin chi tiết
    $scope.openDetail = function(id) {
        var hostKH = "http://localhost:8080/rest/khach-hang/"+id;
        $http.get(hostKH)
            .then(function(response) {
                // Lưu dữ liệu vào scope để hiển thị
                $scope.khct = response.data;

                // Chuyển đổi giới tính
                if ($scope.khct.gioi_tinh === 0) {
                    $scope.khct.gioi_tinh_display = 'Nam';
                } else {
                    $scope.khct.gioi_tinh_display = 'Nữ';
                }

                // Kiểm tra và chuyển đổi 'ngay_sinh' thành kiểu Date
                if ($scope.khct.ngay_sinh) {
                    // Chuyển đổi từ chuỗi 'yyyy-MM-dd' sang đối tượng Date
                    $scope.khct.ngay_sinh = new Date($scope.nvct.ngay_sinh);
                }

                // $scope.showSuccessToast('Dữ liệu đã được lưu thành công!');
                console.log(id)

            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
        var detailModal = new bootstrap.Modal(document.getElementById('detailModal')); // Sử dụng Bootstrap Modal
        detailModal.show(); // Hiển thị modal
    };

    $scope.imageSrc = null; // Lưu ảnh đã chọn
    $scope.imageName = null; // Lưu tên ảnh đã chọn

    $scope.triggerFileInput = function () {
        document.getElementById('fileInput').click();
    };

    // $scope.uploadFile = function (files) {
    //     if (files && files[0]) {
    //         var reader = new FileReader();
    //         reader.onload = function (e) {
    //             $scope.$apply(function () {
    //                 $scope.imageSrc = e.target.result; // Hiển thị ảnh
    //             });
    //         };
    //         $scope.imageName = files[0].name ;
    //         reader.readAsDataURL(files[0]); // Đọc ảnh dưới dạng URL
    //         console.log('Tên file đã chọn:', files[0].name); // In ra tên file trong console
    //     }
    // };


    // Mở modal cập nhật
    $scope.openUpdateModal = function (khachHang) {
        $scope.selectedKhachHang = angular.copy(khachHang);

        // Chuyển đổi 'ngay_sinh' từ chuỗi sang đối tượng Date (nếu tồn tại)
        if ($scope.selectedKhachHang.ngay_sinh) {
            $scope.selectedKhachHang.ngay_sinh = new Date($scope.selectedKhachHang.ngay_sinh);
        }

        // Đảm bảo các giá trị giới tính và trạng thái được chuyển đúng kiểu
        $scope.selectedKhachHang.gioi_tinh = parseInt($scope.selectedKhachHang.gioi_tinh);
        $scope.selectedKhachHang.trang_thai = parseInt($scope.selectedKhachHang.trang_thai, 10);

        var updateModal = new bootstrap.Modal(document.getElementById('updateKhachHangModal'));
        updateModal.show(); // Hiển thị modal
    };

    // Cập nhật khách hàng
    $scope.updateKhachHang = function () {
        var khachHangData = {
            ho_ten: $scope.selectedKhachHang.ho_ten,
            email: $scope.selectedKhachHang.email,
            sdt: $scope.selectedKhachHang.sdt,
            ngay_sinh: new Date($scope.selectedKhachHang.ngay_sinh).toISOString().split('T')[0],
            gioi_tinh: $scope.selectedKhachHang.gioi_tinh,
            trang_thai: $scope.selectedKhachHang.trang_thai,
            // hinh_anh: $scope.selectedKhachHang.hinh_anh
        };

        $http.put('http://localhost:8080/rest/khach-hang/' + $scope.selectedKhachHang.id, khachHangData)
            .then(function (response) {
                toastr.success('Cập nhật khách hàng thành công!', 'Thành công', {
                    closeButton: true,
                    progressBar: true,
                    timeOut: 3000
                });

                $scope.getData($scope.currentPage); // Tải lại dữ liệu sau khi cập nhật

                var modalElement = document.getElementById('updateNhanVienModal');
                var modal = bootstrap.Modal.getInstance(modalElement);
                modal.hide(); // Đóng modal sau khi cập nhật
            })
            .catch(function (error) {
                console.error('Lỗi khi cập nhật nhân viên:', error);
                toastr.error('Có lỗi xảy ra khi cập nhật nhân viên!', 'Lỗi', {
                    closeButton: true,
                    progressBar: true,
                    timeOut: 3000
                });
            });
    };
});
