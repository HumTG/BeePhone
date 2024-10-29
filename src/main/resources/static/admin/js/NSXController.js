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
        if ($scope.textNull() == 0||$scope.checkTrungMa() == 0){
            return;
        }

        $http.post("http://localhost:8080/rest/add-nha-san-xuat",$scope.formData).then(function (respone) {
            $scope.getData();
            toastr.success('Thêm thành công', 'OK');
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
                toastr.success('Xóa thành công', 'OK');
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
        document.getElementById("errMa").innerText = ""
        document.getElementById("errTen").innerText = ""
    }

//Update
    $scope.updateNSX = function (event){
        event.preventDefault();

        if ($scope.formData.id == null){
                alert("Hãy chọn 1 nhà sản xuất")
                return;
        }
        if ($scope.textNull() == 0){
            return;
        }
        $http.post("http://localhost:8080/rest/add-nha-san-xuat",$scope.formData).then(function (respone) {
            $scope.getData();
            console.log("Sửa thành công",respone)
            toastr.success('Sửa thành công', 'OK');
            $scope.clearDataForm();
        })  .catch(function (error) {
            // Xử lý khi gặp lỗi
            console.error("Đã có lỗi xảy ra", error);
            alert("Có lỗi xảy ra khi gửi dữ liệu. Vui lòng thử lại.");
        });
    }

    //check trung ma js
    $scope.checkTrungMa = function (){
        let isDuplicate = false;

        $scope.listNSX.forEach(function(item) {
            if(item.ma_nha_san_xuat === $scope.formData.ma_nha_san_xuat){
                isDuplicate = true;
                toastr.error("Đã trùng mã nhà sản xuất","Lỗi")
            }
        });

        return isDuplicate ? 0 : 1;
    }

    $scope.textNull = function (){
        let err = false;
        var ma = $scope.formData.ma_nha_san_xuat
        var  ten = $scope.formData.ten
        if(ma.trim().length == 0){
            document.getElementById("errMa").innerText = "Không để trống mã NSX"
            err = true
        }
        if(ten.trim().length == 0){
            document.getElementById("errTen").innerText = "Không để trống tên NSX"
            err = true
        }

        return err ? 0 : 1;
    }

});