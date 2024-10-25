var app = angular.module('NhanVienApp', []);
var host = "http://localhost:8080/rest/nhan-vien";

app.controller('NhanVienController', function($scope, $http) {
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

    // Hàm hiển thị thông báo thành công (Success)
    $scope.showSuccessToast = function(message) {
        toastr.success(message, 'Success', {
            closeButton: true,
            progressBar: true,
            timeOut: 3000
        });


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

});
