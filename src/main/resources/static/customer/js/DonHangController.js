app.controller('DonHangController', function($scope, $http) {

    // Lấy thông tin người dùng từ localStorage
    const savedUser = JSON.parse(localStorage.getItem("user"));
    $scope.name = savedUser.ho_ten;

    $scope.hoaDons = [];
    $scope.selectedTrangThai = null;

    // Danh sách trạng thái
    $scope.trangThais = [
        { id: null, label: 'Tất cả', count: 0 },
        { id: 1, label: 'Chờ xác nhận', count: 0 },
        { id: 3, label: 'Chờ vận chuyển', count: 0 },
        { id: 4, label: 'Vận chuyển', count: 0 },
        { id: 6, label: 'Hoàn thành', count: 0 },
        { id: 7, label: 'Đã hủy', count: 0 }
    ];

    // Lấy danh sách hóa đơn theo trạng thái và đếm số đơn hàng
    $scope.filterByTrangThai = function(trangThai) {
        $scope.selectedTrangThai = trangThai;
        $scope.idKhachHang = savedUser.id;

        let url = `/rest/hoa-don/khach-hang/` + $scope.idKhachHang;
        if (trangThai !== null) {
            url += `?trangThai=${trangThai}`;
        }

        $http.get(url)
            .then(function(response) {
                $scope.hoaDons = response.data;

                // Reset count for each state
                $scope.trangThais.forEach(function(item) {
                    item.count = 0; // Reset all counts
                });

                // Update count for each state
                $scope.hoaDons.forEach(function(hoaDon) {
                    const found = $scope.trangThais.find(item => item.id === hoaDon.trangThai);
                    if (found) {
                        found.count += 1;
                    }
                });
            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
    };

    // Nhãn trạng thái
    $scope.getTrangThaiLabel = function(trangThai) {
        const found = $scope.trangThais.find(item => item.id === trangThai);
        return found ? found.label : 'Không xác định';
    };

    // Tải hóa đơn ban đầu (tất cả)
    $scope.filterByTrangThai(null);

});

// Chuyển sang dạng VNĐ
app.filter('vndCurrency', function() {
    return function(amount) {
        if (!amount) return '0 VNĐ';
        return parseFloat(amount).toLocaleString('vi-VN') + ' VNĐ';
    };
});