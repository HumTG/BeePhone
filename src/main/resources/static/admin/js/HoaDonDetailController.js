app.controller('DetailHoaDonController', function($scope, $http,$filter) {
    // Lấy ID từ URL
    var url = window.location.href;
    var id = url.substring(url.lastIndexOf('/') + 1); // Giả sử ID nằm ở cuối URL

    // Gọi API để lấy chi tiết hóa đơn
    $http.get('/rest/hoa-don/' + id + '/detail')
        .then(function(response) {
            // Xử lý dữ liệu từ API
            $scope.hoaDon = response.data.hoaDon;
            $scope.chiTietHoaDonList = response.data.chiTietHoaDonList;


            // Lấy ngay_tao từ dữ liệu và tính ngày dự kiến nhận
            let ngayTao = new Date($scope.hoaDon.ngay_tao);
            let ngayDuKienNhan = new Date(ngayTao);
            ngayDuKienNhan.setDate(ngayDuKienNhan.getDate() + 5);
            // Định dạng ngày dự kiến nhận
            $scope.hoaDon.ngayDuKienNhan = $filter('date')(ngayDuKienNhan, 'dd/MM/yyyy');

            // Tiền giảm
            $scope.tienVorcher = $scope.hoaDon.khuyenMai.gia_tri * $scope.hoaDon.thanh_tien


        })
        .catch(function(error) {
            // Xử lý lỗi
            console.error("Không thể lấy dữ liệu hóa đơn:", error);
            $scope.errorMessage = "Không tìm thấy hóa đơn với ID: " + id;
        });

    // Hàm chuyển đổi trạng thái
    $scope.getTrangThai = function(trangThai) {
        switch (trangThai) {
            case 1: return "Chờ xác nhận";
            case 2: return "Đã xác nhận";
            case 3: return "Chờ vận chuyển";
            case 4: return "Vận chuyển";
            case 5: return "Thanh toán";
            case 6: return "Hoàn thành";
            case 7: return "Hủy";
            default: return "Không xác định";
        }
    };

    // Hàm xóa hóa đơn chi tiết
    $scope.deleteHDCT = function(hdct) {
        // Kiểm tra nếu hdct tồn tại trước khi thực hiện xóa
        if (!hdct || !hdct.id) {
            toastr.error("Chi tiết hóa đơn không tồn tại hoặc đã bị xóa", "Lỗi");
            return;
        }

        // Hiển thị thông báo xác nhận trước khi xóa
        if (confirm("Bạn có chắc chắn muốn xóa chi tiết hóa đơn này?")) {
            console.log("Đang xóa chi tiết hóa đơn:", hdct);

            $http.delete("http://localhost:8080/rest/hoa-don-chi-tiet/" + hdct.id)
                .then(function(response) {
                    if (response.status === 200) {
                        toastr.success('Xóa thành công', 'OK');
                        // Gọi lại API để cập nhật danh sách sau khi xóa thành công
                        $http.get('/rest/hoa-don/' + id + '/detail')
                            .then(function(response) {
                                $scope.chiTietHoaDonList = response.data.chiTietHoaDonList;
                            })
                            .catch(function(error) {
                                console.error("Không thể cập nhật danh sách chi tiết hóa đơn:", error);
                            });
                    } else {
                        console.error("Error: ", response.status);
                        toastr.error("Đã xảy ra lỗi khi xóa", "Lỗi");
                    }
                })
                .catch(function(error) {
                    console.error("Error occurred: ", error);
                    toastr.error("Không thể xóa chi tiết hóa đơn", "Lỗi");
                });
        }
    };



// Hàm tính tổng tiền cho cột "Tổng tiền" trong bảng
    $scope.getTongTienHang = function() {
        // Kiểm tra nếu chiTietHoaDonList là mảng, nếu không thì trả về 0
        if (!Array.isArray($scope.chiTietHoaDonList)) {
            return 0;
        }

        // Tính tổng tiền cho cột "Tổng tiền"
        return $scope.chiTietHoaDonList.reduce(function(total, item) {
            // Lấy số lượng cho từng item
            let soLuong = item.chiTiet ? item.chiTiet.so_luong : 0;

            // Tính giá sau giảm (hoặc giữ nguyên giá gốc nếu không có giảm giá)
            let donGia = item.chiTiet ? item.chiTiet.don_gia : 0;
            let giamGia = item.chiTiet && item.chiTiet.chi_tiet_san_pham.giamGia ? item.chiTiet.chi_tiet_san_pham.giamGia.gia_tri : 0;
            let finalPrice = donGia * (1 - (giamGia / 100));

            // Tính tổng tiền của item này: Số lượng * Giá sau giảm (hoặc giá gốc nếu không giảm)
            let tongTienItem = soLuong * finalPrice;

            // Cộng tổng tiền của item vào tổng tiền chung
            return total + tongTienItem;
        }, 0);
    };



    // Hàm chuyển sang trang trước
    $scope.previousPage = function(page) {
        if (page >= 0) {
            $scope.openProductsModel(page);
        }
    };

    // Hàm chuyển sang trang tiếp theo
    $scope.nextPage = function(page) {
        if (page < $scope.totalPages) {
            $scope.openProductsModel(page);
        }
    };

    // Khởi tạo biến danh sách sản phẩm và phân trang
    $scope.products = [];
    $scope.page = 0;  // Trang hiện tại
    $scope.size = 5; // Số sản phẩm trên mỗi trang
    $scope.totalPages = 0; // Tổng số trang

    // Khởi tạo biến modal một lần
    var productModal = new bootstrap.Modal(document.getElementById('productModal'));

    // Hàm gọi API để lấy danh sách sản phẩm với phân trang
    $scope.openProductsModel = function(page) {
        $scope.page = page || 0;
        $http.get(`http://localhost:8080/rest/chi-tiet-san-pham/list?page=${$scope.page}&size=${$scope.size}`)
            .then(function(response) {
                // Lưu dữ liệu sản phẩm và thông tin phân trang
                $scope.products = response.data.content;
                $scope.totalPages = response.data.totalPages;
                console.log("Dữ liệu sản phẩm:", response.data);

                // Chỉ hiển thị modal khi lần đầu tiên hoặc khi chưa được hiển thị
                if (!productModal._isShown) {
                    productModal.show();
                }
            })
            .catch(function(error) {
                console.error("Không thể lấy dữ liệu sản phẩm:", error);
            });
    };


    // Chọn sản phẩm
    $scope.selectProduct = function(product) {
        console.log("Sản phẩm được chọn:", product);

        $http({
            method: 'POST',
            url : 'http://localhost:8080/rest/hoa-don-chi-tiet/add',
            params: {
                idHD : id,
                idCTSP : product.id
            }
        }).then(function(response) {
            $http.get('/rest/hoa-don/' + id + '/detail')
                .then(function(response) {
                    $scope.chiTietHoaDonList = response.data.chiTietHoaDonList;
                })
                .catch(function(error) {
                    console.error("Không thể cập nhật danh sách chi tiết hóa đơn:", error);
                });
            toastr.success('Thêm sản phẩm thành công', 'Success');
        }).catch(function(error) {
            console.error('Error fetching data:', error);
        });


        // Thực hiện các hành động khác khi sản phẩm được chọn
        var model = bootstrap.Modal.getInstance(document.getElementById('productModal'));
        model.hide(); // Đóng modal sau khi chọn sản phẩm
    };







});

