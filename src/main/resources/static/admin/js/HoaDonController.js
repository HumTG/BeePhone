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

    $scope.loadHoaDons = function() {
        // Gửi yêu cầu API với trạng thái lọc nếu có
        $http.get('/rest/hoa-don/list', {
            params: {
                page: $scope.currentPage,
                size: $scope.pageSize,
                trangThai: $scope.selectedStatus // Truyền trạng thái đã chọn nếu có
            }
        })
            .then(function(response) {
                $scope.hoaDons = response.data.content;  // Dữ liệu hóa đơn trả về
                $scope.totalPages = response.data.totalPages;  // Tổng số trang
                $scope.updateStatusCounts();  // Cập nhật số lượng hóa đơn theo trạng thái
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

    $scope.filterByStatus = function(status) {
        $scope.selectedStatus = status;  // Cập nhật trạng thái đã chọn
        $scope.currentPage = 0;  // Đặt lại trang về trang đầu
        $scope.loadHoaDons();  // Tải lại danh sách hóa đơn theo trạng thái đã chọn
    };




    $scope.updateStatusCounts = function() {
        $scope.statuses.forEach(function(status) {
            // Gửi yêu cầu lấy số lượng hóa đơn cho từng trạng thái, thay đổi size thành 1 để chỉ lấy tổng số hóa đơn
            $http.get('/rest/hoa-don/list', {
                params: {
                    trangThai: status.value,
                    size: 1 // Chỉ lấy tổng số lượng mà không cần dữ liệu chi tiết
                }
            })
                .then(function(response) {
                    // Cập nhật số lượng hóa đơn cho trạng thái tương ứng
                    status.count = response.data.totalElements;
                })
                .catch(function(error) {
                    console.error('Error updating status counts:', error);
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

    $scope.searchHoaDons = function() {
        const searchTerm = document.getElementById('search').value;
        const orderType = document.getElementById('orderType').value;
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;

        // Nếu không chọn loại đơn, gán orderType là null
        const params = {
            page: $scope.currentPage,
            size: $scope.pageSize,
            search: searchTerm,
            startDate: startDate,
            endDate: endDate
        };

        // Thêm orderType vào params nếu có giá trị hợp lệ
        if (orderType) {
            params.orderType = orderType;
        }

        $http.get('/rest/hoa-don/list', { params: params })
            .then(function(response) {
                $scope.hoaDons = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.updateStatusCounts();
            })
            .catch(function(error) {
                console.error('Error loading invoices:', error);
            });
    };

    $scope.resetFilter = function() {
        // Đặt lại các trường input về giá trị mặc định
        document.getElementById('search').value = '';  // Reset ô tìm kiếm
        document.getElementById('orderType').value = '';  // Reset loại đơn
        document.getElementById('startDate').value = '';  // Reset ngày bắt đầu
        document.getElementById('endDate').value = '';  // Reset ngày kết thúc

        // Gọi lại hàm tìm kiếm để tải lại dữ liệu không có bộ lọc
        $scope.searchHoaDons();
    };







});
