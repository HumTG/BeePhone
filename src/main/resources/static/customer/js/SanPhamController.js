app.controller('SanPhamController', function($scope,$http) {
    $http.get('http://localhost:8080/san-pham')
        .then(function(response) {
            // Lưu dữ liệu vào scope để hiển thị
            $scope.sp = response.data;
        })
        .catch(function(error) {
            console.error('Error fetching data:', error);
        });
});