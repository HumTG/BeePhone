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
        $http.get('http://localhost:8080/rest/san-pham', { params: { page: page } })
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

    $scope.isModalOpen = false;
    $scope.selectedProduct = null;
    $scope.quantity = 1;

    $scope.selectedProduct = null;
    $scope.quantity = 1;

    // Hàm mở modal
    $scope.openModal = function(product) {
        $scope.selectedProduct = product;
        $scope.quantity = 1;
        $('#productModal').modal('show'); // Sử dụng jQuery để hiển thị modal
    };

    $scope.getUniqueColors = function() {
        const uniqueColors = [];
        const colorSet = new Set();

        $scope.selectedProduct.variants.forEach(variant => {
            if (!colorSet.has(variant.mauSac.ten)) {
                colorSet.add(variant.mauSac.ten);
                uniqueColors.push(variant);
            }
        });

        return uniqueColors;
    };



    // Hàm giảm số lượng
    $scope.decreaseQuantity = function() {
        if ($scope.quantity > 1) {
            $scope.quantity--;
        }
    };

    // Hàm tăng số lượng
    $scope.increaseQuantity = function() {
        $scope.quantity++;
    };

    // Hàm thêm vào giỏ hàng
    $scope.addToCart = function(product) {
        console.log("Thêm vào giỏ hàng:", product, "Số lượng:", $scope.quantity);
        $scope.closeModal(); // Đóng modal sau khi thêm vào giỏ hàng
    };


});
