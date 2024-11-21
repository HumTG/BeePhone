app.controller('SanPhamDetailController', function($scope,$routeParams, $http,$window) {
    $scope.idSP = $routeParams.idSP;
    $scope.selectedColor = null;
    $scope.selectedSize = null;
    $scope.selectedVariant = null;
    $scope.quantity = 1;

    // Hàm để lấy sản phẩm theo id
    $scope.getData = function() {
        $http.get('http://localhost:8080/rest/san-pham/' +$scope.idSP)
            .then(function(response) {
                $scope.selectedProduct = response.data;
                console.log( $scope.selectedProduct);
            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
    };

    $scope.getData();

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
        // console.log($scope.cart)
    };

    // Hàm đếm số lượng sản phẩm trong giỏ hàng
    $scope.getCartItemCount = function() {
        return $scope.cart.length; // Đếm số mục trong giỏ hàng
    };

    // Hàm thêm sản phẩm vào giỏ hàng
    $scope.addToCart = function(selectedProduct) {
        // console.log($scope.selectedVariant);
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

});