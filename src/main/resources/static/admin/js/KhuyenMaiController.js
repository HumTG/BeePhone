var app = angular.module('KhuyenMaiApp', []);
var host = "http://localhost:8080/rest/khuyen-mai";

app.controller('KhuyenMaiController',function ($scope,$http){
    console.log("Đây là khuyến mãi Ctrl")
    $scope.currentPage = 0;
    $scope.newKM = {
        gia_tri: 0,
        gia_tri_toi_thieu: 0,
        ngay_bat_dau: "",
        ngay_ket_thuc: "",
        trang_thai : 1

    }

    $scope.capNhatTrangThai = function (){
        $http.put("http://localhost:8080/rest/khuyen-mai/update-trang-thai-auto")
            .then(function (res){
                console.log(res)
            })
            .catch(function(error) {
                console.error("Lỗi :"+error)
            });
    }
    $scope.capNhatTrangThai();


    // Hàm để lấy dữ liệu từ API
    $scope.getData = function(page) {
        $http.get(host, { params: { page: page } })
            .then(function(response) {
                $scope.listKM = response.data.content; // 'content' là nơi chứa danh sách
                $scope.totalPages = response.data.totalPages; // Số trang tổng cộng
            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
    };

    $scope.getData($scope.currentPage);

    ///đổi page
    $scope.changePage = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getData($scope.currentPage);
        }
    };
//open modal add
    $scope.openModalAdd = function (){
        var addModal = new bootstrap.Modal(document.getElementById('addKhuyenMaiModal')); // Sử dụng Bootstrap Modal
        addModal.show(); // Hiển thị modal
    }

    $scope.clear = function (){
            $scope.newKM.gia_tri = 0,
            $scope.newKM.gia_tri_toi_thieu = 0,
            $scope.newKM.ngay_bat_dau = "",
            $scope.newKM.ngay_ket_thuc = "",
            $scope.newKM.trang_thai = 1
    }

///add
    $scope.addKM = function (){
        var khuyen_mai = {
            gia_tri: Number($scope.newKM.gia_tri),
            gia_tri_toi_thieu: Number($scope.newKM.gia_tri_toi_thieu),
            ngay_bat_dau: new Date($scope.newKM.ngay_bat_dau),
            ngay_ket_thuc: new Date($scope.newKM.ngay_ket_thuc),
            trang_thai : $scope.newKM.trang_thai
        }

        $http.post('http://localhost:8080/rest/khuyen-mai', khuyen_mai)
            .then(function (respone){
                console.log(khuyen_mai)

                var modalElement = document.getElementById('addKhuyenMaiModal');
                var addModal = bootstrap.Modal.getInstance(modalElement);
                addModal.hide(); // đóng modal
                $scope.getData(0)
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

    $scope.openModalUpdate = function (km){
        $scope.updateKM = angular.copy(km);
        console.log($scope.updateKM)

        if ($scope.updateKM.ngay_bat_dau) {
            $scope.updateKM.ngay_bat_dau = new Date($scope.updateKM.ngay_bat_dau);
        }
        if ($scope.updateKM.ngay_ket_thuc) {
            $scope.updateKM.ngay_ket_thuc = new Date($scope.updateKM.ngay_ket_thuc);
        }

        var addModal = new bootstrap.Modal(document.getElementById('updateKMModal')); // Sử dụng Bootstrap Modal
        addModal.show();
    }

    $scope.updateGiamGia = function (){
        var update_khuyen_mai = {
            id : $scope.updateKM.id,
            ma_khuyen_mai : $scope.updateKM.ma_khuyen_mai,
            gia_tri : Number($scope.updateKM.gia_tri),
            gia_tri_toi_thieu : Number($scope.updateKM.gia_tri_toi_thieu),
            ngay_bat_dau : new Date($scope.updateKM.ngay_bat_dau),
            ngay_ket_thuc : new Date($scope.updateKM.ngay_ket_thuc),
            ngay_tao : new Date($scope.updateKM.ngay_tao),
            trang_thai : $scope.updateKM.trang_thai
        }

        $http.put('http://localhost:8080/rest/khuyen-mai', update_khuyen_mai)
            .then(function (respone){
                console.log(respone)

                var modalElement = document.getElementById('updateKMModal');
                var Modal = bootstrap.Modal.getInstance(modalElement);
                Modal.hide(); // đóng modal

                $scope.getData($scope.currentPage)
                toastr.success('Sửa khuyến mại thành công', 'OK')
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