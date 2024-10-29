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
        if ($scope.textNull() == 0||$scope.validate() == 0){
            return;
        }

        $scope.formData.id = null;
        $http.post("http://localhost:8080/rest/add-mau-sac",$scope.formData).then(function (respone) {
            $scope.getData();
            toastr.success('Thêm thành công', 'OK');
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
    $scope.fillData = function (event,ms){
        event.preventDefault();
        ms.trang_thai = String(ms.trang_thai);
        $scope.formData = angular.copy(ms);
        window.scrollTo({
            top: 0,
            behavior : "smooth"
        });
    }


//clear
    $scope.clearDataForm = function (){
        $scope.formData.id = null;
        $scope.formData.ma_mau_sac = "";
        $scope.formData.ten = "";
        $scope.formData.trang_thai = "1"
        document.getElementById("errTen").innerText = ""
        document.getElementById("errMa").innerText = ""
    }

//update
    $scope.updateMS = function (event){
        event.preventDefault();

        if($scope.formData.id == null){
            alert("Hãy chọn 1 màu sắc")
            return;
        }
        if ($scope.textNull() == 0){
            return;
        }

        $http.post("http://localhost:8080/rest/add-mau-sac",$scope.formData).then(function (respone) {
            $scope.getData();
            toastr.success('Sửa thành công', 'OK');
            $scope.clearDataForm();
        })  .catch(function (error) {
            // Xử lý khi gặp lỗi
            console.error("Đã có lỗi xảy ra", error);
            alert("Có lỗi xảy ra khi gửi dữ liệu. Vui lòng thử lại.");
        });
    }

//check trung ma js
$scope.validate = function (){
    let isDuplicate = false;

    $scope.listMS.forEach(function(item) {
        if(item.ma_mau_sac === $scope.formData.ma_mau_sac){
            isDuplicate = true;
            toastr.error("Đã trùng mã màu sắc","Lỗi")
        }
    });

    return isDuplicate ? 0 : 1;
}

$scope.textNull = function (){
  let err = false;
  var ma = $scope.formData.ma_mau_sac
  var  ten = $scope.formData.ten
  if(ma.trim().length == 0){
      document.getElementById("errMa").innerText = "Không để trống mã màu"
      err = true
  }
    if(ten.trim().length == 0){
        document.getElementById("errTen").innerText = "Không để trống tên màu"
        err = true
    }

    return err ? 0 : 1;
}

})

