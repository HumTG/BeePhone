var app = angular.module('SanPhamApp', []);

app.controller('SanPhamController', function($scope, $http) {
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pageSize = 5; // Số phần tử mỗi trang

    // Hàm lấy danh sách sản phẩm với phân trang
    $scope.getData = function (page) {
        const params = {
            page: page || 0,
            size: $scope.pageSize
        };

        $http.get('http://localhost:8080/rest/san-pham/list', { params: params })
            .then(function (response) {
                $scope.sp = response.data.content;
                $scope.totalPages = response.data.totalPages;
                console.log(response.data.content)
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy danh sách sản phẩm:', error);
            });
    };

    // Chuyển trang
    $scope.changePage = function (page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getData(page);
        }
    };

    // Gọi API để lấy dữ liệu trang đầu tiên
    $scope.getData($scope.currentPage);
})
