var app = angular.module('BanHangTaiQuayApp', []);
var url = "http://localhost:8080/rest/hoa-don"

app.controller('BanHangTaiQuayCtrl',function ($scope,$http,$timeout){
    $scope.viTriHoaDon = 0;
    $scope.hoa_don = {};
    $scope.currentPage = 0;  /// trang của chi tiết sản phẩm

    ///lấy hóa đơn
    $scope.getAllHoaDon = function (){
        $http.get(url).then(function(response) {
            if (response.status === 200) {
                $scope.listHD = response.data;
                console.log( $scope.listHD);
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
            $scope.getHDCT($scope.hoa_don.id);
        }
        console.log($scope.hoa_don)
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


    $scope.themSPvaoHDCT = function (ctsp){
        console.log(ctsp);
        console.log("Số lương thêm : " + ctsp.soLuongThem);
    }


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