var app = angular.module('GiamGiaApp', []);
var host = "http://localhost:8080/rest/giam-gia";

app.controller('GiamGiaController',function ($scope,$http){
    $scope.currentPage = 0;
    $scope.newGiamGia = {
        id : null,
        ma_giam_gia : "GG100",
        ten : "",
        gia_tri : 0,
        ngay_bat_dau : "",
        ngay_ket_thuc : "",
        trang_thai : 1
    }

    // Hàm để lấy dữ liệu từ API
    $scope.getData = function(page) {
        $http.get(host, { params: { page: page } })
            .then(function(response) {
                $scope.listGG = response.data.content; // 'content' là nơi chứa danh sách sản phẩm
                $scope.totalPages = response.data.totalPages; // Số trang tổng cộng
            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
    };

    $scope.getData($scope.currentPage)

    ///đổi page
    $scope.changePage = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getData($scope.currentPage);
        }
    };

    //Open modal
    $scope.openModalAdd = function (){
        var addModal = new bootstrap.Modal(document.getElementById('addGiamGiaModal')); // Sử dụng Bootstrap Modal
        addModal.show(); // Hiển thị modal
    }

    //CLEAR data
    $scope.clear = function (){
        $scope.newGiamGia.id = null,
        $scope.newGiamGia.ma_giam_gia = "",
        $scope.newGiamGia.ten = "",
        $scope.newGiamGia.gia_tri = 0,
        $scope.newGiamGia.ngay_bat_dau = "",
        $scope.newGiamGia.ngay_ket_thuc = ""
    }

    //Open modal update
    $scope.openModalUpdate = function (gg){
        $scope.updateGG = angular.copy(gg);

        if ($scope.updateGG.ngay_bat_dau) {
            $scope.updateGG.ngay_bat_dau = new Date($scope.updateGG.ngay_bat_dau);
        }
        if ($scope.updateGG.ngay_ket_thuc) {
            $scope.updateGG.ngay_ket_thuc = new Date($scope.updateGG.ngay_ket_thuc);
        }

        var addModal = new bootstrap.Modal(document.getElementById('updateGiamGiaModal')); // Sử dụng Bootstrap Modal
        addModal.show();
    }

    $scope.updateGiamGia = function (){
        var update_giam_gia = {
            id : $scope.updateGG.id,
            ma_giam_gia : $scope.updateGG.ma_giam_gia,
            ten : $scope.updateGG.ten,
            gia_tri : Number($scope.updateGG.gia_tri),
            ngay_bat_dau : new Date($scope.updateGG.ngay_bat_dau),
            ngay_ket_thuc : new Date($scope.updateGG.ngay_ket_thuc),
            trang_thai : $scope.updateGG.trang_thai
        }

        $http.put('http://localhost:8080/rest/giam-gia', update_giam_gia)
            .then(function (respone){
                console.log(respone)
                var modalElement = document.getElementById('updateGiamGiaModal');
                var Modal = bootstrap.Modal.getInstance(modalElement);
                Modal.hide(); // đóng modal
                $scope.getData($scope.currentPage)
                toastr.success('Sửa giảm giá thành công', 'OK')
            }) .catch(function(error) {
            console.error('Lỗi :', error);
            toastr.error('Có lỗi xảy ra!', 'Error', {
                closeButton: true,
                progressBar: true,
                timeOut: 3000
            });
        });
    }

    $scope.giamGia = {};
    $scope.products = [];
    $scope.selectedVariants = [];
    $scope.selectedProductId = null; // Biến để lưu ID sản phẩm được chọn
    $scope.currentSanPhamPage = 0;
    $scope.sanPhamPageSize = 5; // Số sản phẩm trên mỗi trang
    $scope.totalSanPhamPages = 0;


    // Tải danh sách sản phẩm
    $scope.loadProducts = function (page) {

        $http.get('/rest/san-pham/list', { params: { page: page, size: $scope.sanPhamPageSize } })
            .then(function(response) {
                $scope.products = response.data.content || response.data; // Lấy danh sách sản phẩm từ API
                $scope.totalSanPhamPages = response.data.totalPages; // Cập nhật số trang tổng cộng
                $scope.currentSanPhamPage = response.data.number; // Cập nhật trang hiện tại
            })
            .catch(function(error) {
                console.error("Lỗi khi tải danh sách sản phẩm:", error);
                toastr.error("Không thể tải danh sách sản phẩm.");
            });
    };

    // Tải biến thể sản phẩm khi chọn sản phẩm
    $scope.loadVariants = function (productId) {
        // Kiểm tra nếu sản phẩm đã được chọn, hủy chọn sản phẩm và ẩn bảng biến thể
        if ($scope.selectedProductId === productId) {
            $scope.selectedProductId = null;
            $scope.selectedVariants = [];
        } else {
            $scope.selectedProductId = productId;

            // Tải danh sách biến thể của sản phẩm đã chọn
            $http.get(`/rest/san-pham/${productId}/variants`)
                .then(function (response) {
                    $scope.selectedVariants = response.data;
                })
                .catch(function (error) {
                    console.error("Lỗi khi tải danh sách biến thể:", error);
                    toastr.error("Không thể tải danh sách biến thể.");
                });
        }
    };

    // Thêm đợt giảm giá và áp dụng cho biến thể
    $scope.addGiamGia = function () {
        $http.post('/rest/giam-gia', $scope.giamGia)
            .then(function (response) {
                const discount = response.data;
                // Cập nhật các biến thể đã chọn với id_giam_gia
                const selectedVariantIds = $scope.selectedVariants
                    .filter(variant => variant.selected)
                    .map(variant => variant.id);

                if (selectedVariantIds.length > 0) {
                    $http.put(`/rest/giam-gia/${discount.id}/apply-to-variants`, selectedVariantIds)
                        .then(function () {
                            // Lưu thông báo vào localStorage
                            localStorage.setItem('successMessage', 'Thêm đợt giảm giá thành công!');
                            // Chuyển hướng đến trang quản lý đợt giảm giá
                            window.location.href = "http://localhost:8080/admin/dot-giam-gia";
                        })
                        .catch(function (error) {
                            console.error("Lỗi khi cập nhật biến thể sản phẩm:", error);
                            toastr.error("Cập nhật biến thể sản phẩm thất bại!");
                        });
                } else {
                    // Lưu thông báo nếu không có biến thể nào được chọn
                    localStorage.setItem('successMessage', 'Thêm đợt giảm giá thành công!');
                    window.location.href = "http://localhost:8080/admin/dot-giam-gia";
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi thêm đợt giảm giá:", error);
                toastr.error("Thêm đợt giảm giá thất bại!");
            });
    };

    // Load danh sách sản phẩm khi trang được tải
        $scope.loadProducts($scope.currentSanPhamPage);

    // Hàm để chuyển trang
    $scope.changeSanPhamPage = function(page) {
        if (page >= 0 && page < $scope.totalSanPhamPages) {
            $scope.loadProducts(page);
        }
    };





})