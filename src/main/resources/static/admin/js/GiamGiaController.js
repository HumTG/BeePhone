var app = angular.module('GiamGiaApp', []);
var host = "http://localhost:8080/rest/giam-gia";

app.controller('GiamGiaController',function ($scope,$http){
    console.log("đây là giảm giá controller")
    $scope.currentPage = 0;

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

})