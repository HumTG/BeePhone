app.controller('DetailHoaDonController', function($scope, $http,$filter) {
    // Lấy ID từ URL
    var url = window.location.href;
    var id = url.substring(url.lastIndexOf('/') + 1); // Giả sử ID nằm ở cuối URL

    // Gọi API để lấy chi tiết hóa đơn
    $http.get('/rest/hoa-don/' + id + '/detail')
        .then(function(response) {
            // Xử lý dữ liệu từ API
            $scope.hoaDon = response.data.hoaDon;
            $scope.chiTietHoaDonList = response.data.chiTietHoaDonList;


            // Lấy ngay_tao từ dữ liệu và tính ngày dự kiến nhận
            let ngayTao = new Date($scope.hoaDon.ngay_tao);
            let ngayDuKienNhan = new Date(ngayTao);
            ngayDuKienNhan.setDate(ngayDuKienNhan.getDate() + 5);
            // Định dạng ngày dự kiến nhận
            $scope.hoaDon.ngayDuKienNhan = $filter('date')(ngayDuKienNhan, 'dd/MM/yyyy');

            // Tiền giảm
            $scope.tienVorcher = $scope.hoaDon.khuyenMai.gia_tri * $scope.hoaDon.thanh_tien


        })
        .catch(function(error) {
            // Xử lý lỗi
            console.error("Không thể lấy dữ liệu hóa đơn:", error);
            $scope.errorMessage = "Không tìm thấy hóa đơn với ID: " + id;
        });

    // Hàm chuyển đổi trạng thái
    $scope.getTrangThai = function(trangThai) {
        switch (trangThai) {
            case 1: return "Chờ xác nhận";
            case 2: return "Đã xác nhận";
            case 3: return "Chờ vận chuyển";
            case 4: return "Vận chuyển";
            case 5: return "Thanh toán";
            case 6: return "Hoàn thành";
            case 7: return "Hủy";
            default: return "Không xác định";
        }
    };

    // Hàm tính tổng tiền hàng
    $scope.getTongTienHang = function() {
        return $scope.chiTietHoaDonList.reduce(function(total, item) {
            return total + (item.chiTiet.so_luong * item.chiTiet.don_gia);
        }, 0);
    };
});

