var app = angular.module('ThongKeApp', []);


app.controller('ThongKeController', function($scope, $http) {
    $scope.monthlySales = null;
    $scope.todaySales = null;
    $scope.monthlySoldProducts = null;
    $scope.lowStockProducts = [];
    $scope.bestSellingProducts = [];

    // Lấy doanh số tháng này
    $http.get('/api/thong-ke/monthly-sales')
        .then(function(response) {
            $scope.monthlySales = response.data;
        })
        .catch(function(error) {
            console.error('Lỗi khi lấy doanh số tháng này:', error);
        });

    // Lấy doanh số hôm nay
    $http.get('/api/thong-ke/today-sales')
        .then(function(response) {
            $scope.todaySales = response.data;
        })
        .catch(function(error) {
            console.error('Lỗi khi lấy doanh số hôm nay:', error);
        });

    // Lấy số lượng hàng bán được trong tháng này
    $http.get('/api/thong-ke/monthly-sold-products')
        .then(function(response) {
            $scope.monthlySoldProducts = response.data;
        })
        .catch(function(error) {
            console.error('Lỗi khi lấy hàng bán được tháng này:', error);
        });

    // Lấy danh sách sản phẩm sắp hết hàng
    $http.get('/api/thong-ke/low-stock-products?threshold=10')
        .then(function(response) {
            $scope.lowStockProducts = response.data;
        })
        .catch(function(error) {
            console.error('Lỗi khi lấy sản phẩm sắp hết hàng:', error);
        });

    $scope.bestSellingProducts = [];
    // Lấy dữ liệu sản phẩm bán chạy từ API
    $http.get('/api/thong-ke/best-selling-products?limit=5')
        .then(function(response) {
            $scope.bestSellingProducts = response.data;
        })
        .catch(function(error) {
            console.error('Lỗi khi lấy dữ liệu sản phẩm bán chạy:', error);
        });

    $scope.labels = [];
    $scope.hoaDonData = [];
    $scope.sanPhamData = [];

    // Lấy dữ liệu thống kê từ API
    $http.get('/api/thong-ke/monthly-chart-data')
        .then(function(response) {
            const data = response.data;

            // Xử lý dữ liệu để hiển thị trên biểu đồ
            data.forEach(item => {
                $scope.labels.push(item.date); // Ngày
                $scope.hoaDonData.push(item.hoaDonCount || 0); // Số hóa đơn (gán 0 nếu null)
                $scope.sanPhamData.push(item.sanPhamCount || 0); // Số sản phẩm (gán 0 nếu null)
            });

            // Tạo biểu đồ sau khi có dữ liệu
            createChart();
        })
        .catch(function(error) {
            console.error('Lỗi khi lấy dữ liệu biểu đồ:', error);
        });

    // Hàm tạo biểu đồ
    function createChart() {
        const ctx = document.getElementById('thongKeChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar', // Loại biểu đồ
            data: {
                labels: $scope.labels, // Các ngày
                datasets: [
                    {
                        label: 'Hóa đơn',
                        data: $scope.hoaDonData,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)', // Màu cột Hóa đơn
                        borderColor: 'rgba(54, 162, 235, 1)', // Viền cột Hóa đơn
                        borderWidth: 1
                    },
                    {
                        label: 'Sản phẩm',
                        data: $scope.sanPhamData,
                        backgroundColor: 'rgba(255, 99, 132, 0.6)', // Màu cột Sản phẩm
                        borderColor: 'rgba(255, 99, 132, 1)', // Viền cột Sản phẩm
                        borderWidth: 1
                    }
                ]
            },
            options: {
                responsive: true,
                scales: {
                    x: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Ngày'
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Số Lượng'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: true,
                        position: 'bottom'
                    },
                    title: {
                        display: true,
                        text: 'Biểu Đồ Thống Kê Hóa Đơn Và Sản Phẩm Tháng Này'
                    }
                }
            }
        });
    }


    // Lấy dữ liệu trạng thái hóa đơn từ API
    $http.get('/api/thong-ke/order-status')
        .then(function(response) {
            const data = response.data;

            // Tạo biểu đồ
            createChartStatus(data.labels, data.data);
        })
        .catch(function(error) {
            console.error('Lỗi khi lấy dữ liệu trạng thái hóa đơn:', error);
        });

    function createChartStatus(labels, data) {
        const ctx = document.getElementById('orderStatusChart').getContext('2d');
        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: labels, // Các nhãn trạng thái
                datasets: [{
                    data: data, // Số lượng cho từng trạng thái
                    backgroundColor: [
                        '#FF6384', // Chờ xác nhận
                        '#36A2EB', // Xác nhận
                        '#FFCE56', // Chờ vận chuyển
                        '#4BC0C0', // Vận chuyển
                        '#9966FF', // Đã thanh toán
                        '#FF9F40', // Thành công
                        '#FF4444'  // Đã hủy
                    ]
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'right'
                    },
                    title: {
                        display: true,
                        text: 'Trạng Thái Đơn Hàng Tháng Này'
                    }
                }
            }
        });
    }

});

// Chuyển sang dạng VNĐ
app.filter('vndCurrency', function() {
    return function(amount) {
        if (!amount) return '0 VNĐ';
        return parseFloat(amount).toLocaleString('vi-VN') + ' VNĐ';
    };
});

