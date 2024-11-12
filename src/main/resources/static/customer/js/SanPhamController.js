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

    $scope.selectedColor = null;
    $scope.selectedSize = null;
    $scope.selectedVariant = null;

    // Hàm mở modal
    $scope.openModal = function(product) {
        $scope.selectedProduct = product;
        $scope.quantity = 1;
        $('#productModal').modal('show'); // Sử dụng jQuery để hiển thị modal
    };

    // Hàm lấy danh sách màu sắc duy nhất
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

    // Chọn màu sắc
    $scope.selectColor = function(color) {
        $scope.selectedColor = color;
        $scope.selectedSize = null;
        $scope.selectedVariant = null;
    };

    // Chọn kích cỡ và cập nhật sản phẩm chi tiết
    $scope.selectSize = function(size, variant) {
        $scope.selectedSize = size;
        $scope.selectedVariant = variant;
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


    // Khởi tạo giỏ hàng từ LocalStorage nếu có dữ liệu
    $scope.cart = JSON.parse(localStorage.getItem('cart')) || [];

    // Hàm lưu giỏ hàng vào LocalStorage
    $scope.saveCart = function() {
        localStorage.setItem('cart', JSON.stringify($scope.cart));
    };
    // Hàm đếm số lượng sản phẩm trong giỏ hàng
    $scope.getCartItemCount = function() {
        return $scope.cart.length; // Đếm số mục trong giỏ hàng
    };


    // Hàm thêm sản phẩm vào giỏ hàng
    $scope.addToCart = function(selectedProduct) {
        if ($scope.selectedVariant) {
            // Kiểm tra xem sản phẩm đã có trong giỏ chưa
            const existingProduct = $scope.cart.find(item =>
                item.product.id === selectedProduct.id && item.variant.id === $scope.selectedVariant.id
            );

            if (existingProduct) {
                // Nếu sản phẩm đã tồn tại, tăng số lượng
                existingProduct.quantity += $scope.quantity;
            } else {
                // Nếu sản phẩm chưa tồn tại, thêm vào giỏ
                $scope.cart.push({
                    product: selectedProduct,
                    variant: $scope.selectedVariant,
                    quantity: $scope.quantity
                });
            }

            // Lưu giỏ hàng vào LocalStorage
            $scope.saveCart();

            toastr.success('Sản phẩm đã được thêm vào giỏ hàng');
            // Cập nhật giao diện ngay lập tức
            $timeout(function() {
                $scope.getCartItemCount();
            }, 0); // Đặt thời gian là 0 để kích hoạt ngay
        } else {
            toastr.error('Vui lòng chọn màu sắc và kích cỡ');
        }
    };

// Hàm cập nhật giỏ hàng khi số lượng thay đổi
    $scope.updateCart = function() {
        $scope.cart = $scope.cart.filter(item => item.quantity > 0);
        $scope.saveCart(); // Cập nhật lại LocalStorage sau khi thay đổi
    };

// Xóa sản phẩm khỏi giỏ hàng
    $scope.removeFromCart = function(index) {
        $scope.cart.splice(index, 1);
        $scope.saveCart(); // Cập nhật lại LocalStorage sau khi xóa
        toastr.info('Sản phẩm đã được xóa khỏi giỏ hàng');
    };

// Hàm tính tổng tiền từng sản phẩm trong giỏ
    $scope.calculateTotalPrice = function(item) {
        let price = item.variant.gia_ban;
        if (item.variant.giamGia) {
            price = price * (1 - item.variant.giamGia.gia_tri / 100);
        }
        return price * item.quantity;
    };

// Hàm tính tổng giá trị đơn hàng
    $scope.calculateCartTotal = function() {
        return $scope.cart.reduce((total, item) => total + $scope.calculateTotalPrice(item), 0);
    };




});
