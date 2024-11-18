var app = angular.module('KichCoApp', []);

app.controller('KichCoController', function ($scope, $http) {
    console.log("controller kích cỡ ok")

    let url = "http://localhost:8080/rest/kich-co/list";

    $scope.formData = {
        id: null,
        ma_kich_co: "",
        ten: "",
        trang_thai: "1"
    }

    $scope.getData = function () {
        $http.get(url).then(function(response) {
            if (response.status === 200) {
                $scope.listKT = response.data;
                console.log(response.data);
            } else {
                console.error("Error: ", response.status);
            }
        }).catch(function(error) {
            console.error("Error occurred: ", error);
        });
    }
    $scope.getData();

    // Thêm dữ liệu
    $scope.addKT = function (event) {
        event.preventDefault();
        if ($scope.textNull() == 0 || $scope.checkMa() == 0) {
            return;
        }

        $scope.formData.id = null;
        $http.post("http://localhost:8080/rest/kich-co/create", $scope.formData).then(function (response) {
            $scope.getData();
            toastr.success('Thêm thành công', 'OK');
            console.log("Thành công", response)
            $scope.clearDataForm();
        }).catch(function (error) {
            console.error("Đã có lỗi xảy ra", error);
            alert("Có lỗi xảy ra khi gửi dữ liệu. Vui lòng thử lại.");
        });
    }

    // Xóa dữ liệu
    $scope.deleteKT = function (event, id) {
        event.preventDefault();
        console.log(id);

        $http.delete("http://localhost:8080/rest/kich-co/delete/" + id).then(function(response) {
            toastr.warning('Xóa thành công', 'OK');
            console.log("xóa thành công")
            $scope.getData();
        }).catch(function(error) {
            console.error("Error occurred: ", error);
        });
    }

    // Điền dữ liệu
    $scope.fillData = function (event, kt) {
        event.preventDefault();
        kt.trang_thai = String(kt.trang_thai);
        $scope.formData = angular.copy(kt);
        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    }

    // Xóa form
    $scope.clearDataForm = function () {
        $scope.formData.id = null;
        $scope.formData.ma_kich_co = "";
        $scope.formData.ten = "";
        $scope.formData.trang_thai = "1"
        document.getElementById("errTen").innerText = ""
        document.getElementById("errMa").innerText = ""
    }

    // Cập nhật
    $scope.updateKT = function (event) {
        event.preventDefault();

        if ($scope.formData.id == null) {
            alert("Hãy chọn 1 kích cỡ")
            return;
        }
        if ($scope.textNull() == 0) {
            return;
        }
        $http.put("http://localhost:8080/rest/kich-co/update/" + $scope.formData.id, $scope.formData)
            .then(function (response) {
                $scope.getData();
                toastr.success('Sửa thành công', 'OK');
                console.log("Thành công", response)
                $scope.clearDataForm();
            }).catch(function (error) {
            console.error("Đã có lỗi xảy ra", error);
            alert("Có lỗi xảy ra khi gửi dữ liệu. Vui lòng thử lại.");
        });
    }

    // Kiểm tra mã trùng
    $scope.checkMa = function () {
        let isDuplicate = false;

        $scope.listKT.forEach(function(item) {
            if (item.ma_kich_co === $scope.formData.ma_kich_co) {
                isDuplicate = true;
                toastr.error("Đã trùng mã kích cỡ", "Lỗi")
            }
        });

        return isDuplicate ? 0 : 1;
    }

    // Kiểm tra rỗng
    $scope.textNull = function () {
        let err = false;
        var ma = $scope.formData.ma_kich_co
        var ten = $scope.formData.ten
        if (ma.trim().length == 0) {
            document.getElementById("errMa").innerText = "Không để trống mã kích cỡ"
            err = true
        }
        if (ten.trim().length == 0) {
            document.getElementById("errTen").innerText = "Không để trống tên kích cỡ"
            err = true
        }

        return err ? 0 : 1;
    }
});