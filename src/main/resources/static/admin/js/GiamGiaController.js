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
    $scope.capNhatTrangThai = function (){
       $http.put("http://localhost:8080/rest/cap-nhat-trang-thai-giam-gia")
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


})