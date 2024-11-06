var app = angular.module('HoaDonApp', []);

app.controller('HoaDonController', function($scope, $http,$window) {
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.selectedStatus = null;
    $scope.totalPages = 0;

    $scope.statuses = [
        { label: 'Chờ xác nhận', value: 1 },
        { label: 'Đã xác nhận', value: 2 },
        { label: 'Chờ vận chuyển', value: 3 },
        { label: 'Vận chuyển', value: 4 },
        { label: 'Thanh toán', value: 5 },
        { label: 'Hoàn thành', value: 6 },
        { label: 'Hủy', value: 7 }
    ];



    // Hàm để tải danh sách hóa đơn từ API
    $scope.loadHoaDons = function() {
        $http.get('/rest/hoa-don/list', { params: { page: $scope.currentPage, size: $scope.pageSize, trangThai: $scope.selectedStatus } })
            .then(function(response) {
                $scope.hoaDons = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.updateStatusCounts();
            })
            .catch(function(error) {
                console.error('Error loading invoices:', error);
            });
    };

    // Hàm chuyển trang
    $scope.changePage = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.loadHoaDons();
        }
    };

    // Hàm lọc theo trạng thái
    $scope.filterByStatus = function(status) {
        $scope.selectedStatus = status;
        $scope.currentPage = 0;
        $scope.loadHoaDons();
    };

    // Hàm cập nhật số lượng hóa đơn cho từng trạng thái
    $scope.updateStatusCounts = function() {
        $scope.statuses.forEach(function(status) {
            $http.get('/rest/hoa-don/list', { params: { trangThai: status.value } })
                .then(function(response) {
                    status.count = response.data.totalElements;
                });
        });
    };

    // Hiển thị chi tiết hóa đơn
    $scope.viewHoaDon = function(hoaDon) {
        // Chuyển hướng tới trang chi tiết hóa đơn hoặc hiển thị modal chi tiết tại đây
        console.log("Xem chi tiết hóa đơn:", hoaDon);
    };

    // Tải danh sách hóa đơn ban đầu
    $scope.loadHoaDons();



});
