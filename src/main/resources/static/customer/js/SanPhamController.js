// app.controller('SanPhamController', function($scope,$http) {
//
//     $http.get('http://localhost:8080/san-pham')
//         .then(function(response) {
//             // Lưu dữ liệu vào scope để hiển thị
//             $scope.sp = response.data;
//         })
//         .catch(function(error) {
//             console.error('Error fetching data:', error);
//         });
// });
app.controller('SanPhamController', function($scope, $http) {
    // Khởi tạo tham số phân trang
    $scope.currentPage = 0;

    // Hàm để lấy dữ liệu từ API
    $scope.getData = function(page) {
        $http.get('http://localhost:8080/san-pham', { params: { page: page } })
            .then(function(response) {
                // Lưu dữ liệu vào scope để hiển thị
                $scope.sp = response.data.content; // 'content' là nơi chứa danh sách sản phẩm
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
});