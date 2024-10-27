var app = angular.module('NSXApp', []);

app.controller('NSXController',function ($scope,$http){
    console.log("nha san xuat controller")
    let url = "http://localhost:8080/rest/nha-san-xuat";

    $scope.formData = {
        id : null,
        ma_nha_san_xuat : "",
        ten : "",
        trang_thai : "1"
    }

$scope.getData = function (){
    $http.get(url).then(function (res){
        if (res.status === 200) {
            $scope.listNSX = res.data;
            console.log(res.data);
        }
        else {
            console.error("Error: ", res.status);
        }
    }).catch(function(error) {
        console.error("Error occurred: ", error);
    });
}

//load table
$scope.getData();


// thêm dl
    $scope.addNSX = function (event){
        event.preventDefault();
        $scope.formData.id = null;
        $http.post("http://localhost:8080/rest/add-nha-san-xuat",$scope.formData).then(function (respone) {
            $scope.getData();
            console.log("Thành công",respone)
            alert("thêm thành công");
            $scope.clearDataForm();
        })  .catch(function (error) {
            console.error("Đã có lỗi xảy ra", error);
            alert("Có lỗi xảy ra khi gửi dữ liệu. Vui lòng thử lại.");
        });
        console.log($scope.formData)
    }

//xóa dl
    $scope.deleteNSX = function (event,id){
        event.preventDefault();
        console.log(id);

        $http.get("http://localhost:8080/rest/delete-nha-san-xuat/" + id).then(function(response) {
            if (response.status === 200) {
                console.log("xóa thành công")
                alert("Xóa thành công")
                $scope.getData();
            } else {
                console.error("Error: ", status);
            }
        }).catch(function(error) {
            console.error("Error occurred: ", error);
        });

    }

//fill
    $scope.fillNSX = function (event,nsx){
        event.preventDefault();
        nsx.trang_thai = String(nsx.trang_thai);
        $scope.formData = angular.copy(nsx);
        console.log($scope.formData);
    }

//Clear
    $scope.clearDataForm = function (){
        $scope.formData.id = null;
        $scope.formData.ma_nha_san_xuat = "";
        $scope.formData.ten = "";
        $scope.formData.trang_thai = "1"
    }

//Update
    $scope.updateNSX = function (event){
        event.preventDefault();

        if ($scope.formData.id == null){
                alert("Hãy chọn 1 nhà sản xuất")
                return;
        }
        $http.post("http://localhost:8080/rest/add-nha-san-xuat",$scope.formData).then(function (respone) {
            $scope.getData();
            console.log("Sửa thành công",respone)
            $scope.clearDataForm();
        })  .catch(function (error) {
            // Xử lý khi gặp lỗi
            console.error("Đã có lỗi xảy ra", error);
            alert("Có lỗi xảy ra khi gửi dữ liệu. Vui lòng thử lại.");
        });
    }

});