var app = angular.module('BanHangTaiQuayApp', []);
var url = "http://localhost:8080/rest/hoa-don"

app.controller('BanHangTaiQuayCtrl',function ($scope,$http){
    $scope.viTriHoaDon = 0;
    $scope.hoa_don = {};
    $scope.currentPage = 0;  /// trang của chi tiết sản phẩm
    $scope.pageKhachHang = 0; /// trang của khách hàng
    $scope.switchGiaoHang = false;
    $scope.khachHang_new = {
        ho_ten : '',
        email : '',
        sdt : '',
        gioi_tinh : 0
    }

    ///lấy toàn bộ hóa đơn
    $scope.getAllHoaDon = function (){
        $http.get(url + "/ban-hang").then(function(response) {
            if (response.status === 200) {
                $scope.listHD = response.data;
            } else {
                console.error("Error: ", response.status);
            }
        }).catch(function(error) {
            console.error("Error occurred: ", error);
        });
    }
    $scope.getAllHoaDon();

    ///đổi hóa đơn
    $scope.changeHoaDon = function(index,a){
        $scope.viTriHoaDon = index;
        $scope.hoa_don = angular.copy(a);
        if ($scope.hoa_don.id != null ) {
            $scope.getHoaDonDB($scope.hoa_don.id)
            $scope.getHDCT($scope.hoa_don.id);
        }
    }

    /// lấy hóa đơn trong DB theo hóa đơn đã chọn
    $scope.getHoaDonDB = function (idHd){
        $http.get(url + "/" + idHd).then(function(response) {
            if (response.status === 200) {
                $scope.hoaDon_DB = response.data;
                console.log(response.data);
            } else {
                console.error("Error: ", response.status);
            }
        }).catch(function(error) {
            console.error("Error occurred: ", error);
        });
    }



    /// lấy hdct theo hóa đơn đã chọn
    $scope.getHDCT = function (idHD){
        $http.get("http://localhost:8080/rest/hoa-don-chi-tiet/dto/" + idHD).then(function(response) {
            if (response.status === 200) {
                $scope.listHDCT = response.data;
                   console.log($scope.listHDCT);
            } else {
                console.error("Error: ", response.status);
            }
        }).catch(function(error) {
            console.error("Error occurred: ", error);
        });
    }

    /// tạo hóa đơn mới
    $scope.createHD = function (){
         if ($scope.listHD.length >= 10){
             toastr.warning('Không thể tạo thêm hóa đơn', 'Cảnh báo');
             return;
         }
        $http.post(url).then(function (response) {
            $scope.getAllHoaDon();
            // $scope.changeHoaDon($scope.listHD.length,response.data);
            toastr.success('Thêm hóa đơn thành công', 'OK');

        })  .catch(function (error) {
            console.error("Đã có lỗi xảy ra", error);
            alert("Có lỗi xảy ra. Vui lòng thử lại.");
        });

    }

    /// mở modal chi tiết sp
    $scope.openModalCTSP = function (){
        if ($scope.hoa_don.id == null){
            toastr.warning('Chọn hóa đơn trước khi thêm', 'Cảnh báo');
            return;
        }
        var modal = new bootstrap.Modal(document.getElementById('chiTietSPModal'));
        modal.show();
    }

    ///lấy chi tiết sp dto từ api
    $scope.getCtspDTO = function(page) {
        $http.get("http://localhost:8080/rest/chi-tiet-san-pham/dto", { params: { page: page } })
            .then(function(response) {
                $scope.listCtspDTO = response.data.content; // 'content' là nơi chứa danh sách
                $scope.totalPages = response.data.totalPages; // Số trang tổng cộng
            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
    };

    /// load dữ liệu trang đầu
    $scope.getCtspDTO($scope.currentPage);

    ///đổi trang chi tiết sản phẩm
    $scope.changePageCTSP = function(pageCTSP) {
        if (pageCTSP >= 0 && pageCTSP < $scope.totalPages) {
            $scope.currentPage = pageCTSP;
            $scope.getCtspDTO($scope.currentPage);
        }
    };

/// thêm sản phẩm vào hóa đơn chi tiết
    $scope.themSPvaoHDCT = function (ctsp){
        // console.log("Số lương thêm : " + ctsp.soLuongThem);
        if(ctsp.soLuongThem <= 0){
            toastr.warning('Số lượng thêm phải lớn hơn 0', 'Cảnh báo');
            return;
        }
        if(ctsp.soLuongThem > ctsp.so_luong){
            toastr.warning('Số lượng thêm phải ít hơn hoặc bằng số lượng tồn', 'Cảnh báo');
            return;
        }

        $http({
            method: 'POST',
            url : 'http://localhost:8080/rest/hoa-don-chi-tiet',
            params: {
                idHD : $scope.hoa_don.id,
                idCTSP : ctsp.id,
                sl : ctsp.soLuongThem
            }
        }).then(function(response) {
            $scope.getHDCT($scope.hoa_don.id);
            $scope.getHoaDonDB($scope.hoa_don.id)
            $scope.changePageCTSP(0);
            $scope.searchTextCTSP = "";
            var modalElement = document.getElementById('chiTietSPModal');
            var Modal = bootstrap.Modal.getInstance(modalElement);
            Modal.hide(); // đóng modal
            toastr.success('Thêm thành công', 'OK');
        }).catch(function(error) {
            console.error('Error fetching data:', error);
        });

    }


///xóa sản phẩm khỏi hóa đơn chi tiết
    $scope.deleteHDCT= function (hdct){
        if ($scope.hoaDon_DB.khuyenMai != null){
            toastr.warning('Bỏ khuyến mại trước khi thực hiện', 'Warning');
            return;
        }
        $http.delete("http://localhost:8080/rest/hoa-don-chi-tiet/" + hdct.id).then(function(response) {
            if (response.status === 200) {
                $scope.getHDCT($scope.hoa_don.id);
                $scope.getHoaDonDB($scope.hoa_don.id)
                $scope.changePageCTSP(0);
                toastr.success('Xóa thành công', 'OK');

            } else {
                console.error("Error: ", status);
            }
        }).catch(function(error) {
            console.error("Error occurred: ", error);
        });
    }

/// mở modal thay đổi số lượng sp
    $scope.openModalSLSP = function (spct){
        if ($scope.hoaDon_DB.khuyenMai != null){
            toastr.warning('Bỏ khuyến mại trước khi thực hiện', 'Warning');
            return;
        }
        $scope.slCTSP = angular.copy(spct);
        $scope.slHienTai = angular.copy(spct.so_luong);
        console.log($scope.slHienTai);

        var modalSLSP = new bootstrap.Modal(document.getElementById('thayDoiSLSP'));
        modalSLSP.show();
    }

    ///thay đổi số lượng sp trong hóa đơn chi tiết
    $scope.thayDoiSlInHDCT = function (){
        if (!Number.isInteger($scope.slCTSP.so_luong) || $scope.slCTSP.so_luong <= 0 ){
            toastr.warning('Số lượng phải là số nguyên dương lớn hơn 0', 'OK');
            return;
        }
        if ($scope.slCTSP.so_luong > $scope.slCTSP.so_luong_ton_ctsp + $scope.slHienTai){
            toastr.warning('Vượt quá số lượng tồn', 'OK');
            return;
        }

        console.log($scope.slCTSP.id_chi_tiet_san_pham);

        $http({
            method: 'PUT',
            url : 'http://localhost:8080/rest/hoa-don-chi-tiet/thay-doi-sl-hdct-tai-quay',
            params: {
                idHD : $scope.hoa_don.id,
                idCTSP : $scope.slCTSP.id_chi_tiet_san_pham,
                slMoi : $scope.slCTSP.so_luong
            }
        }).then(function(response) {
            $scope.getHDCT($scope.hoa_don.id);
            $scope.getHoaDonDB($scope.hoa_don.id)
            $scope.changePageCTSP(0);

            var modalElement = document.getElementById('thayDoiSLSP');
            var Modal = bootstrap.Modal.getInstance(modalElement);
            Modal.hide(); // đóng modal
            toastr.success('Thay đổi số lượng thành công', 'OK');
        }).catch(function(error) {
            console.error('Error fetching data:', error);
        });
    }

    /// lấy danh sách khuyến mãi còn hạn
    $scope.getKhuyenMai = function (){
        $http.get("http://localhost:8080/rest/khuyen-mai/con-han").then(function(response) {
            if (response.status === 200) {
                $scope.listKhuyenMai = response.data;
                console.log($scope.listKhuyenMai);
            } else {
                console.error("Error: ", response.status);
            }
        }).catch(function(error) {
            console.error("Error occurred: ", error);
        });
    }
    $scope.getKhuyenMai();

    /// Mở modal khuyến mại
    $scope.openModalKhuyenMai = function (){
        if ($scope.hoa_don.id == null){
            toastr.warning('Chọn hóa đơn trước khi chọn khuyến mại', 'Cảnh báo');
            return;
        }
        if ($scope.listHDCT.length == 0){
            toastr.warning('Hãy chọn sản phẩm', 'Cảnh báo');
            return;
        }
        var modal = new bootstrap.Modal(document.getElementById('khuyenMaiModal'));
        modal.show();
    }

    /// ADD khuyến mại cho hóa đơn
    $scope.addKMchoHD = function (km){
        if ($scope.hoaDon_DB.thanh_tien < km.gia_tri_toi_thieu){
            toastr.warning('Hóa đơn không đủ điều kiện để áp dùng', 'Cảnh báo');
            return;
        }

        $http({
            method: 'PUT',
            url : 'http://localhost:8080/rest/hoa-don/update-khuyen-mai',
            params: {
                idKM : km.id,
                idHD : $scope.hoa_don.id
            }
        }).then(function(response) {
            $scope.getHoaDonDB($scope.hoa_don.id)

            var modalElement = document.getElementById('khuyenMaiModal');
            var Modal = bootstrap.Modal.getInstance(modalElement);
            Modal.hide(); // đóng modal
            toastr.success('Áp dụng thành công', 'OK');
        }).catch(function(error) {
            console.error('Error fetching data:', error);
        });
    }

    /// xóa khuyến mại khỏi hóa đơn
    $scope.xoaKMtrongHD = function (){
        $http({
            method: 'PUT',
            url : 'http://localhost:8080/rest/hoa-don/delete-khuyen-mai-hd',
            params: {
                idHD : $scope.hoa_don.id
            }
        }).then(function(response) {
            $scope.getHoaDonDB($scope.hoa_don.id)

            var modalElement = document.getElementById('khuyenMaiModal');
            var Modal = bootstrap.Modal.getInstance(modalElement);
            Modal.hide(); // đóng modal
            toastr.success('Xóa km thành công', 'OK');
        }).catch(function(error) {
            console.error('Error fetching data:', error);
        });
    }

    /// lấy danh sách khách hàng
    $scope.getKhachHang = function (viTriTrangKH){
        $http.get("http://localhost:8080/rest/khach-hang/ban-hang", { params: { page: viTriTrangKH } })
            .then(function(response) {
                $scope.listKH = response.data.content; // 'content' là nơi chứa danh sách
                $scope.tongTrangKH = response.data.totalPages; // Số trang tổng cộng
            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
    }

    /// load dữ liệu trang đầu của khách hàng
    $scope.getKhachHang($scope.pageKhachHang);

    /// mở modal khách hàng
    $scope.openModalKhachHang = function (){
        if ($scope.hoa_don.id == null){
            toastr.warning('Chọn hóa đơn trước khi chọn khách', 'Cảnh báo');
            return;
        }
        var modal = new bootstrap.Modal(document.getElementById('khachHangModal'));
        modal.show();
    }
    /// đổi trang khách hàng
    $scope.changePageKH = function(p) {
        if (p >= 0 && p < $scope.tongTrangKH) {
            $scope.pageKhachHang = p;
            $scope.getKhachHang($scope.pageKhachHang);
        }
    };

    /// ADD khách hàng cho hóa đơn
    $scope.addKhachHang = function (kh){
        $http({
            method: 'PUT',
            url : 'http://localhost:8080/rest/hoa-don/update-khach-hang-tai-quay',
            params: {
                idKH : kh.id,
                idHD : $scope.hoa_don.id
            }
        }).then(function(response) {
            $scope.getHoaDonDB($scope.hoa_don.id)

            var modalElement = document.getElementById('khachHangModal');
            var Modal = bootstrap.Modal.getInstance(modalElement);
            Modal.hide(); // đóng modal
            toastr.success('Add khách hàng thành công', 'OK');
        }).catch(function(error) {
            console.error('Error fetching data:', error);
        });
    }

    //mở modal thêm mới khách hàng
    $scope.modalThemMoiKhachHang = function (){
        var modal = new bootstrap.Modal(document.getElementById('themKhachHangModal'));
        modal.show();
    }

    /// thêm mới nhanh khách hàng tại quầy
    $scope.themKhachHangTaiQuay = function (){
        if ($scope.khachHang_new.ho_ten.trim().length == 0 || $scope.khachHang_new.sdt.trim().length == 0 || $scope.khachHang_new.email.trim().length == 0){
            toastr.warning('Không để trống dữ liệu', '');
            return;
        }
        const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!regexEmail.test($scope.khachHang_new.email)){
            toastr.warning('Sai định dạng Email', '');
            return;
        }
        const regexSDT = /^(0|\+84)([3|5|7|8|9])[0-9]{8}$/;
        if (!regexSDT.test($scope.khachHang_new.sdt)){
            toastr.warning('Số điện thoại phải có 10 số, bắt đầu bằng 0 và 3/5/7/8/9', '');
            return;
        }

        $http.post("http://localhost:8080/rest/khach-hang/add-khach-hang-tai-quay",$scope.khachHang_new).then(function (respone) {
            $scope.getKhachHang(0);
            toastr.success('Thêm khách hàng thành công', 'OK');

            var modalElement = document.getElementById('themKhachHangModal');
            var Modal = bootstrap.Modal.getInstance(modalElement);
            Modal.hide(); // đóng modal

            $scope.khachHang_new.ho_ten = '';
            $scope.khachHang_new.sdt = '';
            $scope.khachHang_new.email = '';

        })  .catch(function (error) {
            toastr.error("Có lỗi sảy ra khi thêm", 'lỗi');
        });
    }


    /// set khách lẻ cho hóa đơn
    $scope.setKhachLe = function (){
        $http({
            method: 'PUT',
            url : 'http://localhost:8080/rest/hoa-don/set-khach-le-tai-quay',
            params: {
                idHD : $scope.hoa_don.id
            }
        }).then(function(response) {
            $scope.getHoaDonDB($scope.hoa_don.id)

            var modalElement = document.getElementById('khachHangModal');
            var Modal = bootstrap.Modal.getInstance(modalElement);
            Modal.hide(); // đóng modal
            toastr.success('Set khách lẻ thành công', 'OK');
        }).catch(function(error) {
            console.error('Error fetching data:', error);
        });
    }

    // $scope.selectedKhachHang = null;
    //
    // $scope.checkDiaChi = function () {
    //     if ($scope.selectedKhachHang) {
    //         console.log($scope.selectedKhachHang.id);  // In ra ID của địa chỉ
    //         console.log($scope.selectedKhachHang.dia_chi_chi_tiet);  // In ra chi tiết địa chỉ
    //     } else {
    //         console.log("Chưa chọn địa chỉ.");
    //     }
    // };



    $scope.checkGiaoHang = function (){
        console.log($scope.switchGiaoHang);
    }

    $scope.openModalThanhToan = function (){
        if (!($scope.hoa_don.id)){
            toastr.warning('Chọn hóa đơn trước khi thanh toán', 'Cảnh báo');
        } else if ($scope.listHDCT.length == 0){
            toastr.warning('Không có sản phẩm trong hóa đơn', 'Cảnh báo');
        } else if ( $scope.switchGiaoHang &&  (!($scope.hoaDon_DB.khachHang) || $scope.hoaDon_DB.khachHang.id == 1)) {
            toastr.warning('Giao hàng phải chọn khách hàng', 'Cảnh báo');
        }
        else if ($scope.hoaDon_DB.phi_ship < 0 || !Number.isInteger($scope.hoaDon_DB.phi_ship) ) {
            toastr.warning('Sai phí ship', 'Cảnh báo');
        }
        else {
            var modal = new bootstrap.Modal(document.getElementById('xacNhanTTModal'));
            modal.show();
        }

    }

    /// xác nhận đơn tại quầy
    $scope.xacNhanHoaDon = function (){
        let loaiHD =  $scope.switchGiaoHang ? 1 : 0;
        console.log(loaiHD);
        var diaChi = $scope.getFullAddress();
        $scope.hoaDon_DB.dia_chi_nguoi_nhan = diaChi;
        $scope.hoaDon_DB.ten_nguoi_nhan = $scope.hoaDon_DB.khachHang.ho_ten;
        $scope.hoaDon_DB.email_nguoi_nhan = $scope.hoaDon_DB.khachHang.email;
        $scope.hoaDon_DB.sdt_nguoi_nhan = $scope.hoaDon_DB.khachHang.sdt;
        $http({
            method: 'PUT',
            url : 'http://localhost:8080/rest/hoa-don/xac-nhan-don',
            data : $scope.hoaDon_DB,
            params: {
                idHD : $scope.hoa_don.id,
                loaiHD : loaiHD
                // idDiaChi : $scope.selectedKhachHang
            }
        }).then(function(response) {
            toastr.success('Xác nhận đơn thành công', 'OK');
            $scope.printInvoice();
            setTimeout(function (){
                window.location.reload();
            },3000);

        }).catch(function(error) {
            console.error('Error fetching data:', error);
        });

    }

    /// In hóa đơn
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


    // Khởi tạo các biến lưu trữ danh sách tỉnh/thành phố và quận/huyện
    $scope.cities = [];
    $scope.districts = [];

    // Hàm để lấy danh sách tỉnh/thành phố từ API
    $scope.loadCities = function() {
        $http.get('https://provinces.open-api.vn/api/?depth=2')
            .then(function(response) {
                $scope.cities = response.data;
            })
            .catch(function(error) {
                console.error("Lỗi khi tải danh sách tỉnh/thành phố:", error);
            });
    };

    // Hàm để cập nhật danh sách quận/huyện dựa trên tỉnh/thành phố được chọn
    $scope.updateDistricts = function() {
        // console.log("Chọn thành phố :" + $scope.selectedCity.name)
        if ($scope.selectedCity) {
            $scope.districts = $scope.selectedCity.districts;
        } else {
            $scope.districts = [];
        }
    };

    // Gọi hàm loadCities để tải danh sách tỉnh/thành phố ngay khi controller được khởi tạo
    $scope.loadCities();

    // Hàm để lấy địa chỉ đầy đủ từ các trường thông tin địa chỉ
    $scope.getFullAddress = function() {
        let addressDetail = $scope.addressDetail || ""; // Địa chỉ chi tiết
        let city = $scope.selectedCity ? $scope.selectedCity.name : ""; // Tên tỉnh/thành phố
        let district = $scope.selectedDistrict ? $scope.selectedDistrict.name : ""; // Tên quận/huyện

        // Nối các phần địa chỉ lại với nhau
        return `${addressDetail}, ${district}, ${city}`;
    };
});