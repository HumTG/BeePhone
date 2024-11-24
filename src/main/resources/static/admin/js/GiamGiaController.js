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

    $scope.giamGia = {
        ten: '',
        gia_tri: '',
        ngay_bat_dau: '',
        ngay_ket_thuc: ''
    };

    $scope.isDateError = false;
    $scope.isGiaTriError = false;

    // Hàm kiểm tra ngày khi submit form
    $scope.checkNgay = function() {
        if ($scope.giamGia.ngay_bat_dau && $scope.giamGia.ngay_ket_thuc) {
            const ngayBatDau = new Date($scope.giamGia.ngay_bat_dau);
            const ngayKetThuc = new Date($scope.giamGia.ngay_ket_thuc);

            // Kiểm tra nếu ngày bắt đầu lớn hơn ngày kết thúc
            if (ngayBatDau > ngayKetThuc) {
                $scope.isDateError = true;
                // Hiển thị thông báo toast
                $scope.showToast("Ngày bắt đầu không được lớn hơn ngày kết thúc.");
            } else {
                $scope.isDateError = false;
            }
        }
    };

    // Hàm kiểm tra giá trị giảm
    $scope.checkGiaTri = function() {
        if ($scope.giamGia.gia_tri > 100) {
            $scope.isGiaTriError = true;
            // Hiển thị thông báo toast
            $scope.showToast("Giá trị giảm không được lớn hơn 100.");
        } else {
            $scope.isGiaTriError = false;
        }
    };

    // Hàm hiển thị thông báo toast
    $scope.showToast = function(message) {
        // Bạn có thể sử dụng thư viện toast như toastr hoặc ngToast
        // Ví dụ sử dụng toastr:
        toastr.error(message);
    };


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

        // Kiểm tra các ngày khi submit form
        $scope.checkNgay();
        $scope.checkGiaTri();

        // Kiểm tra nếu chưa chọn bất kỳ biến thể sản phẩm nào
        const selectedVariantIds = $scope.selectedVariants
            .filter(variant => variant.selected)
            .map(variant => variant.id);

        if (selectedVariantIds.length === 0) {
            toastr.error("Vui lòng chọn ít nhất một biến thể sản phẩm!");
            return; // Dừng thực hiện nếu không có biến thể nào được chọn
        }

        if ($scope.giamGiaForm.$valid && !$scope.isDateError && !$scope.isGiaTriError) {
            // Logic lưu thông tin
            $http.post('/rest/giam-gia', $scope.giamGia)
                .then(function (response) {
                    const discount = response.data;
                    // Cập nhật các biến thể đã chọn với id_giam_gia
                    $http.put(`/rest/giam-gia/${discount.id}/apply-to-variants`, selectedVariantIds)
                        .then(function () {
                            // Chuyển hướng đến trang quản lý đợt giảm giá
                            window.location.href = "http://localhost:8080/admin/dot-giam-gia";
                        })
                        .catch(function (error) {
                            console.error("Lỗi khi cập nhật biến thể sản phẩm:", error);
                            toastr.error("Cập nhật biến thể sản phẩm thất bại!");
                        });
                })
                .catch(function (error) {
                    console.error("Lỗi khi thêm đợt giảm giá:", error);
                    toastr.error("Thêm đợt giảm giá thất bại!");
                });
            console.log("Dữ liệu hợp lệ và đã được lưu!");
        } else {
            console.log("Dữ liệu không hợp lệ, không thể lưu.");
        }

    };


    // Load danh sách sản phẩm khi trang được tải
        $scope.loadProducts($scope.currentSanPhamPage);

    // Hàm để chuyển trang
    $scope.changeSanPhamPage = function(page) {
        if (page >= 0 && page < $scope.totalSanPhamPages) {
            $scope.loadProducts(page);
        }
    };


    // Detail

    $scope.viewDiscountDetail = function(discountId) {
        $http.get(`/rest/giam-gia/${discountId}/detail`)
            .then(function(response) {
                $scope.discountDetail = response.data.discount;  // Thông tin đợt giảm giá
                $scope.appliedVariants = response.data.variants; // Danh sách biến thể áp dụng

                // Hiển thị modal chi tiết đợt giảm giá
                var discountDetailModal = new bootstrap.Modal(document.getElementById('discountDetailModal'));
                discountDetailModal.show();
            })
            .catch(function(error) {
                console.error("Lỗi khi tải chi tiết đợt giảm giá:", error);
                toastr.error("Không thể tải thông tin chi tiết đợt giảm giá.");
            });
    };


    // filter

    // Khởi tạo bộ lọc
    $scope.filter = {
        maKhuyenMai: '',
        giaTriGiam: '',
        tenKhuyenMai: '',
        trangThai: '',
        tuNgay: '',
        denNgay: ''
    };
    // Hàm lấy dữ liệu với bộ lọc
    $scope.getData = function(page) {
        let params = {
            page: page,
            maKhuyenMai: $scope.filter.maKhuyenMai,
            giaTriGiam: $scope.filter.giaTriGiam,
            tenKhuyenMai: $scope.filter.tenKhuyenMai,
            trangThai: $scope.filter.trangThai,
            tuNgay: $scope.filter.tuNgay,
            denNgay: $scope.filter.denNgay
        };
        $http.get(host, { params: params })
            .then(function(response) {
                $scope.listGG = response.data.content;
                $scope.totalPages = response.data.totalPages;
            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
    };

    // Áp dụng bộ lọc
    $scope.applyFilters = function() {
        $scope.getData(0); // Bắt đầu từ trang đầu tiên khi áp dụng bộ lọc
    };

    // Xóa bộ lọc
    $scope.clearFilters = function() {
        $scope.filter = {
            maKhuyenMai: '',
            giaTriGiam: '',
            tenKhuyenMai: '',
            trangThai: '',
            tuNgay: '',
            denNgay: ''
        };
        $scope.getData(0); // Lấy lại toàn bộ dữ liệu
    };

    // Đổi trang
    $scope.changePage = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getData($scope.currentPage);
        }
    };

    // Gọi dữ liệu ban đầu
    $scope.getData($scope.currentPage);








})