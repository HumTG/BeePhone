app.controller('DetailHoaDonController', function($scope, $http,$filter) {
    // Lấy ID từ URL
    var url = window.location.href;
    var id = url.substring(url.lastIndexOf('/') + 1); // Giả sử ID nằm ở cuối URL

    $scope.currentStatus = 1 ; // Trạng thái mặc định là "Chờ xác nhận"

    // Gọi API để lấy chi tiết hóa đơn
    $http.get('/rest/hoa-don/' + id + '/detail')
        .then(function(response) {
            // Xử lý dữ liệu từ API
            $scope.hoaDon = response.data.hoaDon;
            $scope.chiTietHoaDonList = response.data.chiTietHoaDonList ;
            $scope.currentStatus = response.data.hoaDon.trang_thai;
            console.log($scope.currentStatus)


            // Lấy ngay_tao từ dữ liệu và tính ngày dự kiến nhận
            let ngayTao = new Date($scope.hoaDon.ngay_tao);
            let ngayDuKienNhan = new Date(ngayTao);
            ngayDuKienNhan.setDate(ngayDuKienNhan.getDate() + 5);
            // Định dạng ngày dự kiến nhận
            $scope.hoaDon.ngayDuKienNhan = $filter('date')(ngayDuKienNhan, 'dd/MM/yyyy');

            // Phí ship
            if ($scope.hoaDon.khuyenMai == null){
                $scope.tienVorcher = 0 ;
            }
            else{
                // Tiền giảm
                $scope.tienVorcher =  ($scope.hoaDon.khuyenMai.gia_tri * $scope.hoaDon.thanh_tien) / 100 ;
            }


            // Phí ship
            if ($scope.hoaDon.phi_ship == null){
                return $scope.phiShip = 0;
            }
            else{
                return $scope.phiShip = $scope.hoaDon.phi_ship;
            }


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

    // Thay đổi thông tin hóa đơn
    $scope.openModal = function() {
        var myModal = new bootstrap.Modal(document.getElementById('changeInfoModal'));
        myModal.show();
    };

    // Xác nhận thay đổi thông tin hóa đơn
    $scope.submitForm = function() {
        var hoaDonId = id ;

        var updatedData = {
            ten_nguoi_nhan: $scope.hoaDon.ten_nguoi_nhan,
            sdt_nguoi_nhan: $scope.hoaDon.sdt_nguoi_nhan,
            dia_chi_nguoi_nhan: $scope.hoaDon.dia_chi_nguoi_nhan,
            mo_ta: $scope.hoaDon.mo_ta
        };

        // Make the PUT request
        $http.put('/rest/hoa-don/update/info-hoa-don/' + hoaDonId, updatedData)
            .then(function(response) {
                toastr.success('Thay đổi thông tin thành công !', 'Success');
                // Đóng modal sau khi xác nhận thành công
                $('#changeInfoModal').modal('hide');
            }, function(error) {
                console.error("Error occurred: ", error);
                toastr.error('Xóa sản phẩm thành thất bại !', 'Error');
            });
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

            $http.delete("http://localhost:8080/rest/hoa-don-chi-tiet/delete/" + hdct.id)
                .then(function(response) {
                    if (response.status === 200) {
                        toastr.success('Xóa sản phẩm thành công !', 'Success');
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



    // Cập nhật lại số lượng trong hóa đơn chi tiết
    $scope.capNhatSoLuong = function() {
        $scope.chiTietHoaDonList.forEach(function(item) {
            let url = '/rest/hoa-don-chi-tiet/update-so-luong/' + item.chiTiet.id;
            let params = { soLuong: item.chiTiet.so_luong };

            $http.put(url, null, { params: params })
                .then(function(response) {
                })
                .catch(function(error) {
                    console.error("Lỗi khi cập nhật số lượng:", error);
                });
        });
    };

    // Cập nhật thành tiền , tiền sau giảm giá , trạng thái + 1
    $scope.capNhatHoaDon = function() {
        // Lấy giá trị `thanhTien` và xử lý `tienVorcher` nếu là undefined hoặc null
        let thanhTien = $scope.getTongTienHang();
        let tienVorcher = $scope.tienVorcher || 0;  // Nếu tienVorcher là null hoặc undefined, gán giá trị mặc định là 0
        let phiShip = $scope.phiShip || 0 ;

        let tienSauGiamGia = thanhTien - tienVorcher + phiShip ;

        // Đường dẫn API với tham số id của hóa đơn
        let url = '/rest/hoa-don/update/' + id;

        // Gọi API `PUT` để cập nhật hóa đơn với `thanhTien` và `tienSauGiamGia`
        $http.put(url, null, {
            params: {
                thanhTien: thanhTien,
                tienSauGiamGia: tienSauGiamGia,
            }
        })
            .then(function(response) {
                // Cập nhật lại thông tin hóa đơn trên giao diện nếu cần
                $scope.hoaDon = response.data;
                $scope.currentStatus = $scope.hoaDon.trang_thai;
                console.log($scope.currentStatus);
            })
            .catch(function(error) {
                console.error("Lỗi khi cập nhật hóa đơn:", error);
            });
    };

    // Quay lại hóa đơn ,Cập nhật thành tiền , tiền sau giảm giá , trạng thái - 1
    $scope.cannelHoaDon = function() {
        // Lấy giá trị `thanhTien` và xử lý `tienVorcher` nếu là undefined hoặc null
        let thanhTien = $scope.getTongTienHang();
        let tienVorcher = $scope.tienVorcher || 0;  // Nếu tienVorcher là null hoặc undefined, gán giá trị mặc định là 0

        let tienSauGiamGia = thanhTien - tienVorcher;

        // Đường dẫn API với tham số id của hóa đơn
        let url = '/rest/hoa-don/update-cannel/' + id;

        // Gọi API `PUT` để cập nhật hóa đơn với `thanhTien` và `tienSauGiamGia`
        $http.put(url, null, {
            params: {
                thanhTien: thanhTien,
                tienSauGiamGia: tienSauGiamGia,
            }
        })
            .then(function(response) {
                // Cập nhật lại thông tin hóa đơn trên giao diện nếu cần
                $scope.hoaDon = response.data;
                $scope.currentStatus = $scope.hoaDon.trang_thai;
                console.log($scope.currentStatus);
                toastr.success('Quay lại trạng thái thành công!', 'Success');
            })
            .catch(function(error) {
                console.error("Lỗi khi cập nhật hóa đơn:", error);
            });
    };


    // Hàm mở modal để nhập mô tả
    $scope.createLichSuHoaDon = function() {
        // Mở modal để nhập mô tả
        $('#confirmModal').modal('show');
    };

    // Hàm xử lý xác nhận khi người dùng nhấn nút "Xác nhận" trong modal
    $scope.confirmCreateLichSuHoaDon = function() {
        let nguoiTaoHoaDon = "Tên Người Tạo"; // Thay bằng tên người tạo thực tế
        let moTa = $scope.moTa; // Lấy mô tả từ modal
        let trangThai = $scope.currentStatus  ; // Giữ nguyên trạng thái
        if ($scope.currentStatus == 1){
            trangThai = $scope.currentStatus + 1 ;
        }

        // Gọi API để tạo lịch sử hóa đơn
        let url = '/api/lich-su-hoa-don/create';
        $http.post(url, null, {
            params: {
                hoaDonId: $scope.hoaDon.id,
                nguoiTaoHoaDon: nguoiTaoHoaDon,
                moTa: moTa,
                trangThai: trangThai
            }
        })
            .then(function(response) {
                toastr.success('Xác nhận thành công!', 'Success');

                // Đóng modal sau khi xác nhận thành công
                $('#confirmModal').modal('hide');

                // // Cập nhật lại trạng thái hóa đơn trên giao diện nếu cần
                // $scope.currentStatus = trangThai ;

                // Nếu trạng thái là "Đã xác nhận" thì in hóa đơn
                if ($scope.currentStatus == 2) {
                    $scope.printInvoice();
                }
            })
            .catch(function(error) {
                console.error("Lỗi khi tạo lịch sử hóa đơn:", error);
                toastr.error('Có lỗi khi tạo lịch sử hóa đơn!');
            });
    };

    // Hủy hóa đơn
    $scope.huyHoaDon = function() {
        let url = '/rest/hoa-don/cancel/' + id;

        $http.put(url)
            .then(function(response) {
                toastr.success('Hủy hóa đơn thành công!', 'Success');
                // Cập nhật trạng thái hóa đơn trên giao diện
                $scope.hoaDon.trang_thai = 7;
                console.log("Hóa đơn đã được hủy:", $scope.hoaDon);
                $scope.currentStatus = $scope.hoaDon.trang_thai;
            })
            .catch(function(error) {
                console.error("Lỗi khi hủy hóa đơn:", error);
                toastr.error('Có lỗi xảy ra khi hủy hóa đơn!', 'Error');
            });
    };



    // In hóa đơn
    $scope.printInvoice = function() {
        let invoiceContent = document.getElementById("invoice").innerHTML;
        let printWindow = window.open("", "_blank");
        printWindow.document.open();
        printWindow.document.write(`
        <html>
            <head>
                <title>Hóa Đơn</title>
                <style>
                    /* Copy CSS styles from your main stylesheet here */
                    .invoice-container {
                        font-family: Arial, sans-serif;
                        width: 600px;
                        margin: auto;
                        padding: 20px;
                        border: 1px solid #ddd;
                    }

                    .invoice-container h2, .invoice-container h3 {
                        text-align: center;
                    }

                    .invoice-container table {
                        width: 100%;
                        border-collapse: collapse;
                    }

                    .invoice-container table, .invoice-container th, .invoice-container td {
                        border: 1px solid black;
                    }

                    .invoice-container th, .invoice-container td {
                        padding: 8px;
                        text-align: left;
                    }
                </style>
            </head>
            <body onload="window.print(); window.close();">
                ${invoiceContent}
            </body>
        </html>
    `);
        printWindow.document.close();
    };

    // Xác nhận trạng thái hóa đơn
    $scope.confirmBill = function(){
        $scope.capNhatHoaDon();
        $scope.capNhatSoLuong();
        $scope.createLichSuHoaDon();
    }

    // Quay lại trạng thái hóa đơn
    $scope.cannelBill = function(){
        $scope.cannelHoaDon();
        $scope.capNhatSoLuong();
        $scope.createLichSuHoaDon();
    }

    // Hủy hóa đơn
    $scope.cannelBill = function(){
        $scope.huyHoaDon();
        $scope.createLichSuHoaDon();
    }

    // Hàm mở modal và lấy dữ liệu lịch sử hóa đơn
    $scope.openLichSuModal = function() {
        // Gọi API để lấy dữ liệu lịch sử hóa đơn
        let url = '/api/lich-su-hoa-don/' + $scope.hoaDon.id;
        $http.get(url)
            .then(function(response) {
                $scope.lichSuHoaDonList = response.data; // Lưu dữ liệu lịch sử vào scope
                $('#lichSuModal').modal('show'); // Mở modal sau khi có dữ liệu
            })
            .catch(function(error) {
                console.error("Lỗi khi lấy lịch sử hóa đơn:", error);
                toastr.error('Có lỗi khi lấy lịch sử hóa đơn!');
            });
    };





});

