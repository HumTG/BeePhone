var app = angular.module('SanPhamApp', []);

app.controller('SanPhamController', function($scope, $http) {
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pageSize = 5; // Số phần tử mỗi trang

    // Khởi tạo bộ lọc tìm kiếm
    $scope.filter = {
        maHoacTenSanPham: '',
        trangThai: '',
        soLuongTon: 0
    };

    // Hàm lấy danh sách sản phẩm với phân trang và tìm kiếm
    $scope.getData = function (page) {
        const params = {
            page: page || 0,
            size: $scope.pageSize,
            maHoacTenSanPham: $scope.filter.maHoacTenSanPham,
            trangThai: $scope.filter.trangThai,
            soLuongTon: $scope.filter.soLuongTon
        };

        $http.get('http://localhost:8080/rest/san-pham/search', { params: params })
            .then(function (response) {
                $scope.sp = response.data.content;
                $scope.totalPages = response.data.totalPages;
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy danh sách sản phẩm:', error);
            });
    };

    // Chuyển trang
    $scope.changePage = function (page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getData(page);
        }
    };

    // Hàm tìm kiếm
    $scope.searchSanPham = function() {
        $scope.currentPage = 0;  // Đặt lại trang về 0 khi tìm kiếm mới
        $scope.getData($scope.currentPage);
    };

    // Hàm reset bộ lọc
    $scope.resetFilter = function() {
        $scope.filter = {
            maHoacTenSanPham: '',
            trangThai: '',
            soLuongTon: 0
        };
        $scope.currentPage = 0;
        $scope.getData($scope.currentPage);
    };

    // Gọi API để lấy dữ liệu trang đầu tiên
    $scope.getData($scope.currentPage);

    // Nhà sản xuất

    $scope.nhaSanXuatList = []; // Danh sách nhà sản xuất
    $scope.selectedNhaSanXuatId = null; // ID của nhà sản xuất đã chọn

    // Lấy danh sách nhà sản xuất từ API
    $scope.getNhaSanXuat = function() {
        $http.get('http://localhost:8080/rest/nha-san-xuat')
            .then(function(response) {
                $scope.nhaSanXuatList = response.data;
            })
            .catch(function(error) {
                console.error('Lỗi khi lấy danh sách nhà sản xuất:', error);
            });
    };

    // Gọi API khi khởi tạo controller
    $scope.getNhaSanXuat();

    // Mở modal thêm nhà sản xuất mới
    $scope.openAddNhaSanXuatModal = function() {
        console.log("Open modal to add new nhà sản xuất");
    };

    // Kiểm tra nhà sản xuất đã chọn
    $scope.printSelectedNhaSanXuatId = function() {
        console.log("ID của nhà sản xuất đã chọn:", $scope.selectedNhaSanXuatId);
    };

    // END nhà sản xuất

    // Nhà chất liệu
    $scope.chatLieuList = []; // Danh sách chất liệu
    $scope.selectedChatLieuId = null; // ID của chất liệu đã chọn

    // Lấy danh sách chất liệu từ API
    $scope.getChatLieu = function() {
        $http.get('http://localhost:8080/rest/chat-lieu')
            .then(function(response) {
                $scope.chatLieuList = response.data;
            })
            .catch(function(error) {
                console.error('Lỗi khi lấy danh sách chất liệu:', error);
            });
    };

    // Gọi API khi khởi tạo controller
    $scope.getChatLieu();

    // Mở modal thêm chất liệu mới
    $scope.openAddChatLieuModal = function() {
        console.log("Open modal to add new chất liệu");
    };

    // Kiểm tra chất liệu đã chọn
    $scope.printSelectedChatLieuId = function() {
        console.log("ID của chất liệu đã chọn:", $scope.selectedChatLieuId);
    };
    // End chất liệu



    // Màu Sắc

    $scope.dsMauSac = []; // Danh sách màu sắc từ API
    $scope.selectedMauDetails = []; // Danh sách các màu đã chọn

    // Hàm mở modal và lấy danh sách màu sắc từ API
    $scope.openMauSacModal = function() {
        $http.get('http://localhost:8080/rest/mau-sac')
            .then(function(response) {
                $scope.dsMauSac = response.data;
                var mauSacModal = new bootstrap.Modal(document.getElementById('mauSacModal'));
                mauSacModal.show();
            })
            .catch(function(error) {
                console.error('Lỗi khi lấy danh sách màu sắc:', error);
            });
    };

    // Hàm thêm/bỏ chọn màu
    $scope.toggleMau = function(mau) {
        const index = $scope.selectedMauDetails.findIndex(item => item.id === mau.id);
        if (index > -1) {
            // Nếu màu đã có trong danh sách selectedMauDetails, xóa nó đi
            $scope.selectedMauDetails.splice(index, 1);
        } else {
            // Nếu màu chưa có trong danh sách selectedMauDetails, thêm vào
            $scope.selectedMauDetails.push(mau);
        }
    };

    // Hàm xác nhận lựa chọn màu
    $scope.confirmMau = function() {
        var mauSacModal = bootstrap.Modal.getInstance(document.getElementById('mauSacModal'));
        mauSacModal.hide(); // Đóng modal
    };

    // Hàm xóa màu khỏi danh sách đã chọn
    $scope.removeSelectedMau = function(mauId) {
        $scope.selectedMauDetails = $scope.selectedMauDetails.filter(mau => mau.id !== mauId);
    };

    // END màu sắc

    // Kích cỡ

    $scope.sizeOptions = []; // Lưu danh sách các kích cỡ từ API
    $scope.selectedSizes = []; // Lưu các kích cỡ đã chọn

    // Lấy danh sách kích cỡ từ API
    $scope.getSizes = function() {
        $http.get('http://localhost:8080/rest/kich-co/list')
            .then(function(response) {
                $scope.sizeOptions = response.data;
            })
            .catch(function(error) {
                console.error('Lỗi khi lấy danh sách kích cỡ:', error);
            });
    };

    // Mở modal chọn kích cỡ
    $scope.openSizeModal = function() {
        $scope.getSizes(); // Tải kích cỡ trước khi mở modal
        var sizeModal = new bootstrap.Modal(document.getElementById('sizeModal'));
        sizeModal.show();
    };

    // Thêm hoặc loại bỏ kích cỡ khi click vào nút
    $scope.toggleSizeSelection = function(size) {
        const index = $scope.selectedSizes.findIndex(s => s.id === size.id);
        if (index === -1) {
            $scope.selectedSizes.push(size); // Thêm nếu chưa có
        } else {
            $scope.selectedSizes.splice(index, 1); // Bỏ nếu đã chọn
        }
    };

    // Đóng modal khi đã chọn xong
    $scope.addSelectedSizes = function() {
        var sizeModal = bootstrap.Modal.getInstance(document.getElementById('sizeModal'));
        sizeModal.hide();
    };

    // Xóa kích cỡ khỏi danh sách các kích cỡ đã chọn
    $scope.removeSize = function(size) {
        const index = $scope.selectedSizes.findIndex(s => s.id === size.id);
        if (index !== -1) {
            $scope.selectedSizes.splice(index, 1);
        }
    };


    // END kích cỡ

    // Hàm để bật/tắt màu nền của hàng khi chọn checkbox
    $scope.toggleRowHighlight = function(variant) {
        variant.selected = !variant.selected;
    };

    // Hàm chọn tất cả các checkbox và bật màu nền
    $scope.selectAll = function(event) {
        const selectAllCheckbox = event.target.checked;
        $scope.productVariants.forEach(variant => {
            variant.selected = selectAllCheckbox;
        });
    };

    // Tự động tạo biến thể cho bảng chi tiết sản phẩm
    $scope.productVariants = [];

    // Hàm để cập nhật danh sách các biến thể sản phẩm dựa trên kích cỡ và màu sắc đã chọn
    $scope.updateProductVariants = function() {
        $scope.productVariants = [];

        $scope.selectedSizes.forEach(size => {
            $scope.selectedMauDetails.forEach(color => {
                $scope.productVariants.push({
                    size: size.ten,              // Tên kích cỡ
                    color: color.ten,            // Tên màu
                    quantity: 1,               // Số lượng mặc định
                    price: '100,000 VND',        // Giá mặc định
                    tenSanPham: `${$scope.tenSanPham} [${size.ten} - ${color.ten}]` // Tên sản phẩm bao gồm kích cỡ và màu
                });
            });
        });
    };

    // Gọi hàm này khi thêm/xóa kích cỡ hoặc màu sắc
    $scope.$watch('selectedSizes', function(newVal, oldVal) {
        $scope.updateProductVariants();
    }, true);

    $scope.$watch('selectedMauDetails', function(newVal, oldVal) {
        $scope.updateProductVariants();
    }, true);

});
