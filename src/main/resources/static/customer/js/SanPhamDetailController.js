app.controller('SanPhamDetailController', function($scope,$routeParams, $http,$window) {
    $scope.idSP = $routeParams.idSP;
    $scope.selectedColor = null;
    $scope.selectedSize = null;
    $scope.selectedVariant = null;

    // Hàm để lấy sản phẩm theo id
    $scope.getData = function() {
        $http.get('http://localhost:8080/rest/san-pham/' +$scope.idSP)
            .then(function(response) {
                $scope.selectedProduct = response.data;
                console.log( $scope.thongTinSP);
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

});