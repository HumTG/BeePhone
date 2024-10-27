var app = angular.module('KhachHangApp', []);
var host = "http://localhost:8080/rest/khach-hang";

app.controller('KhachHangController', function($scope, $http) {
    // Khởi tạo tham số phân trang
    $scope.currentPage = 0;


    // Hàm để lấy dữ liệu từ API
    $scope.getData = function(page) {

        $http.get(host, { params: { page: page } })
            .then(function(response) {
                // Lưu dữ liệu vào scope để hiển thị
                $scope.kh = response.data.content; // 'content' là nơi chứa danh sách khách hàng
                $scope.totalPages = response.data.totalPages; // Số trang tổng cộng

                console.log(response.data.content);

                // Kiểm tra dữ liệu
                console.log('Danh sách khách hàng:', $scope.kh);
                if (!$scope.kh || $scope.kh.length === 0) {
                    console.log('Không có dữ liệu khách hàng nào.');
                }
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

});
