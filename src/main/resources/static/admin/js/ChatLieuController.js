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
    // $scope.formData.trang_thai = Number($scope.formData.trang_thai);
    $scope.formData.id = null;
    $http.post("http://localhost:8080/rest/add-chat-lieu",$scope.formData).then(function (respone) {
        $scope.getData();
        $scope.toaTest(event);
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
            $scope.toaTest(event);
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
    console.log($scope.formData);
}

//clear
$scope.clearDataForm = function (){
    $scope.formData.id = null;
    $scope.formData.ma_chat_lieu = "";
    $scope.formData.ten = "";
    $scope.formData.trang_thai = "1"
}

//Update
    $scope.updateCL = function (event){
        event.preventDefault();

        if($scope.formData.id == null){
            alert("Hãy chọn 1 chất liệu")
            return;
        }
        $http.post("http://localhost:8080/rest/add-chat-lieu",$scope.formData).then(function (respone) {
            $scope.getData();
            $scope.toaTest(event);
            toastr.warning('Sửa thành công', 'OK');
            console.log("Thành công",respone)
            $scope.clearDataForm();
        })  .catch(function (error) {
            // Xử lý khi gặp lỗi
            console.error("Đã có lỗi xảy ra", error);
            alert("Có lỗi xảy ra khi gửi dữ liệu. Vui lòng thử lại.");
        });
    }


//Toa test

    $scope.toaTest = function (event) {
        event.preventDefault();

        toastr.options = {
            "positionClass": "toast-top-right",
            "timeOut": "5000",
            "closeButton": false,
            "progressBar": true,
            "preventDuplicates": true,
            "toastClass": "custom-toast", // Áp dụng lớp tùy chỉnh
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut",
            "onclick": null,
        };

    };

})