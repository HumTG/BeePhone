var app = angular.module('GiamGiaApp', []);
var host = "http://localhost:8080/rest/giam-gia";

app.controller('GiamGiaController',function ($scope,$http){
    console.log("đây là giảm giá controller")
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


    ///cập nhật trạng thái khi đã hết hạn
    // $scope.capNhatTrangThai = function (){
    //    $http.put("http://localhost:8080/rest/cap-nhat-trang-thai-giam-gia")
    //        .then(function (res){
    //            console.log(res)
    //        })
    //        .catch(function(error) {
    //          console.error("Lỗi :"+error)
    //        });
    // }
    // $scope.capNhatTrangThai();

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

    ///add dữ liệu
    $scope.addGiamGia = function (){
        var giam_gia = {
            id : null,
            ma_giam_gia : $scope.newGiamGia.ma_giam_gia,
            ten : $scope.newGiamGia.ten,
            gia_tri : Number($scope.newGiamGia.gia_tri),
            ngay_bat_dau : new Date($scope.newGiamGia.ngay_bat_dau),
            ngay_ket_thuc : new Date($scope.newGiamGia.ngay_ket_thuc),
            trang_thai : 1
        }
        $http.post('http://localhost:8080/rest/giam-gia', giam_gia)
            .then(function (respone){
                console.log(giam_gia)
                var modalElement = document.getElementById('addGiamGiaModal');
                var addModal = bootstrap.Modal.getInstance(modalElement);
                addModal.hide(); // đóng modal
                $scope.getData();
                $scope.clear();
                toastr.success('Thành công', 'OK')
            }) .catch(function(error) {
            console.error('Lỗi :', error);
            toastr.error('Có lỗi xảy ra!', 'Error', {
                closeButton: true,
                progressBar: true,
                timeOut: 3000
            });
        });
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
                $scope.getData();
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

})