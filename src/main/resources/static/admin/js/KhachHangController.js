var app = angular.module('KhachHangApp', []);

app.controller('KhachHangController', function($scope, $http, $window) {
    var host = "http://localhost:8080/rest/khach-hang";
    $scope.kh = []; // Khởi tạo danh sách khách hàng
    $scope.newCustomer = {}; // Dữ liệu khách hàng mới

    // Hàm để lấy danh sách khách hàng
    $scope.getData = function() {
        $http.get(host)
            .then(function(response) {
                $scope.kh = response.data.content; // Lưu danh sách khách hàng vào scope
            })
            .catch(function(error) {
                console.error("Lỗi khi tải dữ liệu:", error);
            });
    };

    // Gọi hàm lấy dữ liệu khi trang được tải
    $scope.getData();

    // Mở form thêm mới khách hàng
    $scope.openAddForm = function() {
        document.getElementById("addCustomerModal").style.display = "flex";
    };

    // Đóng form thêm mới khách hàng
    $scope.closeAddForm = function() {
        $('#addCustomerModal').modal('hide'); // Đóng modal đúng ID
    };

    // Hàm thêm mới khách hàng
    $scope.addCustomer = function() {
        console.log("Dữ liệu khách hàng mới:", $scope.newCustomer); // Kiểm tra dữ liệu trước khi gửi
        $http.post(host, $scope.newCustomer) // Sử dụng biến host
            .then(function(response) {
                console.log("Khách hàng mới đã được thêm:", response.data);
                $scope.getData(); // Cập nhật danh sách khách hàng sau khi thêm
                $scope.closeAddForm(); // Đóng form sau khi thêm thành công
            })
            .catch(function(error) {
                console.error("Lỗi khi thêm khách hàng mới:", error);
                alert("Đã xảy ra lỗi khi thêm khách hàng.");
            });
    };

    // Mở trang thêm mới khách hàng
    $scope.openAddCustomerPage = function() {
        $window.location.href = "/admin/create-khach-hang.html";
    };

    // Hàm mở modal và hiển thị thông tin chi tiết
    $scope.openDetail = function(id) {
        var hostKH = `${host}/${id}`;
        $http.get(hostKH)
            .then(function(response) {
                $scope.khct = response.data;

                // Chuyển đổi giới tính
                $scope.khct.gioi_tinh_display = $scope.khct.gioi_tinh === 0 ? 'Nam' : 'Nữ';

                // Kiểm tra và chuyển đổi 'ngay_sinh' thành kiểu Date
                if ($scope.khct.ngay_sinh) {
                    $scope.khct.ngay_sinh = new Date($scope.khct.ngay_sinh);
                }

            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });

        var detailModal = new bootstrap.Modal(document.getElementById('detailModal'), { keyboard: false });
        detailModal.show();
    };
});
