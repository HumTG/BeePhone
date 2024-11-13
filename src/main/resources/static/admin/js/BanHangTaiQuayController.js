var app = angular.module('BanHangTaiQuayApp', []);
var url = "http://localhost:8080/rest/hoa-don"

app.controller('BanHangTaiQuayCtrl',function ($scope,$http,$timeout){
    $scope.viTriHoaDon = 0;
    $scope.hoa_don = {};
    $scope.currentPage = 0;  /// trang của chi tiết sản phẩm
    $scope.pageKhachHang = 0; /// trang của khách hàng

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
                  // console.log($scope.listHDCT);
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
        console.log("Số lương thêm : " + ctsp.soLuongThem);
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

    $scope.checkDiaChi = function (){
        // console.log($scope.selectedKhachHang);
    }

    /// xác nhận đơn tại quầy
    $scope.xacNhanHoaDon = function (){
        let loaiHD =  $scope.hoaDon_DB.loai_hoa_don;
        $http({
            method: 'PUT',
            url : 'http://localhost:8080/rest/hoa-don/xac-nhan-don',
            params: {
                idHD : $scope.hoa_don.id,
                loaiHD : loaiHD
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



    // $scope.a = function (){
    //     sessionStorage.setItem('toastrMessage', 'LOAD thành công');
    //     window.location.reload();
    // }
    // angular.element(document).ready(function() {
    //     var message = sessionStorage.getItem('toastrMessage');
    //     if (message) {
    //         toastr.success(message, 'OK');
    //         sessionStorage.removeItem('toastrMessage');
    //     }
    // });
});