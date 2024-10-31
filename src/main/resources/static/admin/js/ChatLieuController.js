var app = angular.module('ChatLieuApp', []);

app.controller('ChatLieuController',function ($scope,$http){
console.log("controler chat lieu ok")

    let url = "http://localhost:8080/rest/chat-lieu";

$scope.formData = {
    id : null,
    ma_chat_lieu : "",
    ten : "",
    trang_thai : "1"
}

$scope.getData = function (){
    $http.get(url).then(function(response) {
        if (response.status === 200) {  // Kiểm tra mã trạng thái 200
            $scope.listCL = response.data;
            console.log(response.data);
        } else {
            console.error("Error: ", response.status);
        }
    }).catch(function(error) {
        console.error("Error occurred: ", error);
    });
}
    $scope.getData();

// thêm dl

$scope.addCL = function (event){
    event.preventDefault();
    if ($scope.textNull() == 0||$scope.checkMa() == 0){
        return;
    }

    $scope.formData.id = null;
    $http.post("http://localhost:8080/rest/add-chat-lieu",$scope.formData).then(function (respone) {
        $scope.getData();
        toastr.success('Thêm thành công', 'OK');
        console.log("Thành công",respone)
        $scope.clearDataForm();
})  .catch(function (error) {
        // Xử lý khi gặp lỗi
        console.error("Đã có lỗi xảy ra", error);
        alert("Có lỗi xảy ra khi gửi dữ liệu. Vui lòng thử lại.");
    });
}


//xóa dl
$scope.deleteCL = function (event,id){
    event.preventDefault();
    console.log(id);

    $http.get("http://localhost:8080/rest/delete-chat-lieu/" + id).then(function(response) {
        if (response.status === 200) {
            // console.log(response);
            toastr.warning('Xóa thành công', 'OK');
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
$scope.fillData = function (event,cl){
    event.preventDefault();
    cl.trang_thai = String(cl.trang_thai);
    $scope.formData = angular.copy(cl);
    window.scrollTo({
        top: 0,
        behavior : "smooth"
    });
}

//clear
$scope.clearDataForm = function (){
    $scope.formData.id = null;
    $scope.formData.ma_chat_lieu = "";
    $scope.formData.ten = "";
    $scope.formData.trang_thai = "1"
    document.getElementById("errTen").innerText = ""
    document.getElementById("errMa").innerText = ""
}

//Update
    $scope.updateCL = function (event){
        event.preventDefault();

        if($scope.formData.id == null){
            alert("Hãy chọn 1 chất liệu")
            return;
        }
        if ($scope.textNull() == 0){
            return;
        }
        $http.post("http://localhost:8080/rest/add-chat-lieu",$scope.formData).then(function (respone) {
            $scope.getData();
            toastr.success('Sửa thành công', 'OK');
            console.log("Thành công",respone)
            $scope.clearDataForm();
        })  .catch(function (error) {
            // Xử lý khi gặp lỗi
            console.error("Đã có lỗi xảy ra", error);
            alert("Có lỗi xảy ra khi gửi dữ liệu. Vui lòng thử lại.");
        });
    }

///validate
    $scope.checkMa = function (){
        let isDuplicate = false;

        $scope.listCL.forEach(function(item) {
            if(item.ma_chat_lieu === $scope.formData.ma_chat_lieu){
                isDuplicate = true;
                toastr.error("Đã trùng mã chất liệu","Lỗi")
            }
        });

        return isDuplicate ? 0 : 1;
    }

    $scope.textNull = function (){
        let err = false;
        var ma = $scope.formData.ma_chat_lieu
        var  ten = $scope.formData.ten
        if(ma.trim().length == 0){
            document.getElementById("errMa").innerText = "Không để trống mã chất liệu"
            err = true
        }
        if(ten.trim().length == 0){
            document.getElementById("errTen").innerText = "Không để trống tên chất liệu"
            err = true
        }

        return err ? 0 : 1;
    }

})