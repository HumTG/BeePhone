var app = angular.module('NhanVienApp', []);
var host = "http://localhost:8080/rest/nhan-vien";

app.controller('NhanVienController', function($scope, $http ) {

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
                $scope.nv = response.data.content; // 'content' là nơi chứa danh sách sản phẩm
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


    // Hàm để mở modal và hiển thị thông tin chi tiết
    $scope.openDetail = function(id) {
        var hostNV = "http://localhost:8080/rest/nhan-vien/"+id;
        $http.get(hostNV)
            .then(function(response) {
                // Lưu dữ liệu vào scope để hiển thị
                $scope.nvct = response.data;

                // Chuyển đổi giới tính
                if ($scope.nvct.gioi_tinh === 0) {
                    $scope.nvct.gioi_tinh_display = 'Nam';
                } else {
                    $scope.nvct.gioi_tinh_display = 'Nữ';
                }

                // Kiểm tra và chuyển đổi 'ngay_sinh' thành kiểu Date
                if ($scope.nvct.ngay_sinh) {
                    // Chuyển đổi từ chuỗi 'yyyy-MM-dd' sang đối tượng Date
                    $scope.nvct.ngay_sinh = new Date($scope.nvct.ngay_sinh);
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

    $scope.uploadFile = function (files) {
        if (files && files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $scope.$apply(function () {
                    $scope.imageSrc = e.target.result; // Hiển thị ảnh
                });
            };
            $scope.imageName = files[0].name ;
            reader.readAsDataURL(files[0]); // Đọc ảnh dưới dạng URL
            console.log('Tên file đã chọn:', files[0].name); // In ra tên file trong console
        }
    };

    // Thêm nhân viên
    $scope.confirmAddNhanVien = function() {
        var nhanVienData = {
            ho_ten: $scope.nhanVien.ho_ten,
            email: $scope.nhanVien.email,
            sdt: $scope.nhanVien.sdt,
            ngay_sinh: new Date($scope.nhanVien.ngay_sinh).toISOString().split('T')[0],
            gioi_tinh: $scope.nhanVien.gioi_tinh,
            dia_chi: $scope.nhanVien.dia_chi,
            trang_thai: $scope.nhanVien.trang_thai,
            hinh_anh: $scope.imageName
        };

        $http.post('http://localhost:8080/rest/nhan-vien', nhanVienData)
            .then(function(response) {
                localStorage.setItem('successMessage', 'Nhân viên đã được thêm thành công!');
                window.location.href = 'http://localhost:8080/admin/nhan-vien';
            })
            .catch(function(error) {
                console.error('Lỗi khi thêm nhân viên:', error);
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

});
