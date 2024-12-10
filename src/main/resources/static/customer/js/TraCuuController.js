app.controller('TraCuuController', function($scope, $http) {
    $scope.maHoaDon = '';
    $scope.hoaDon = null;
    $scope.errorMessage = null;

    $scope.trangThaiMap = {
        1: "Chờ xác nhận",
        2: "Đã xác nhận",
        3: "Chờ vận chuyển",
        4: "Đang vận chuyển",
        5: "Thanh toán",
        6: "Hoàn thành",
        7: "Hủy"
    };

    $scope.traCuuHoaDon = function() {
        // Kiểm tra mã hóa đơn có trống không
        if (!$scope.maHoaDon || $scope.maHoaDon.trim() === '') {
            $scope.errorMessage = 'Mã hóa đơn không được để trống!';
            $scope.showToast($scope.errorMessage, 'error');
            return; // Dừng lại nếu mã hóa đơn trống
        }
        $http.get('/rest/hoa-don/tra-cuu', { params: { maHoaDon: $scope.maHoaDon } })
            .then(function(response) {
                $scope.hoaDon = response.data;
                $scope.errorMessage = null;
                // Hiển thị thông báo thành công
                $scope.showToast('Tra cứu thành công!', 'success');
            }, function(error) {
                $scope.hoaDon = null;
                $scope.errorMessage = error.data || 'Không tìm thấy hóa đơn!';
                // Hiển thị thông báo thất bại
                $scope.showToast('Mã hóa đơn không đúng hoặc không tồn tại!', 'error');
            });
    };

    // Hàm hiển thị thông báo (toast)
    $scope.showToast = function(message, type) {
        // Xử lý hiển thị thông báo tùy theo loại (success hoặc error)
        if (type === 'success') {
            // Hiển thị thông báo thành công
            toastr.success(message, { timeOut: 3000 }); // Toast thành công, hiện thị trong 3 giây
        } else if (type === 'error') {
            // Hiển thị thông báo lỗi
            toastr.error(message, { timeOut: 3000 }); // Toast lỗi, hiện thị trong 3 giây
        }
    };



});

// Chuyển sang dạng VNĐ
app.filter('vndCurrency', function() {
    return function(amount) {
        if (!amount) return '0 VNĐ';
        return parseFloat(amount).toLocaleString('vi-VN') + ' VNĐ';
    };
});

