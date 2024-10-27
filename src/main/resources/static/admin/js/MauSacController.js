var app = angular.module('MauSacApp',[]);
app.controller('MauSacController',function ($scope,$http){
    console.log("Đây là màu sắc controller")

$scope.formData = {
        id : null,
        ma_mau_sac : "",
        ten : "",
        trang_thai : "1"
}

let url = "http://localhost:8080/rest/mau-sac";
$scope.getData = function (){
        $http.get(url).then(function(response) {
            if (response.status === 200) {
                $scope.listMS = response.data;
                console.log(response.data);
            } else {
                console.error("Error: ", response.status);
            }
        }).catch(function(error) {
            console.error("Error occurred: ", error);
        });
    }
    $scope.getData();


//add dl
    $scope.addMS = function (event){
        event.preventDefault();
        $scope.formData.id = null;
        $http.post("http://localhost:8080/rest/add-mau-sac",$scope.formData).then(function (respone) {
            $scope.getData();
            alert("thêm thành công")
            $scope.clearDataForm();
        })  .catch(function (error) {
            console.error("Đã có lỗi xảy ra", error);
            alert("Có lỗi xảy ra khi gửi dữ liệu. Vui lòng thử lại.");
        });
    }

///delete mau sac
$scope.deleteMS = function (event,id){
        event.preventDefault();
        console.log(id);

        $http.delete("http://localhost:8080/rest/delete-mau-sac/" + id).then(function(response) {
            if (response.status === 200) {
                console.log("xóa thành công")
                $scope.getData();
            } else {
                console.error("Error: ", status);
            }
        }).catch(function(error) {
            console.error("Error occurred: ", error);
        });

    }

//fill
    $scope.fillData = function (event,ms){
        event.preventDefault();
        ms.trang_thai = String(ms.trang_thai);
        $scope.formData = angular.copy(ms);
        console.log($scope.formData);
    }


//clear
    $scope.clearDataForm = function (){
        $scope.formData.id = null;
        $scope.formData.ma_mau_sac = "";
        $scope.formData.ten = "";
        $scope.formData.trang_thai = "1"
    }

//update
    $scope.updateMS = function (event){
        event.preventDefault();

        if($scope.formData.id == null){
            alert("Hãy chọn 1 màu sắc")
            return;
        }
        $http.post("http://localhost:8080/rest/add-mau-sac",$scope.formData).then(function (respone) {
            $scope.getData();
           alert("Sửa thành công")
            $scope.clearDataForm();
        })  .catch(function (error) {
            // Xử lý khi gặp lỗi
            console.error("Đã có lỗi xảy ra", error);
            alert("Có lỗi xảy ra khi gửi dữ liệu. Vui lòng thử lại.");
        });
    }


})

