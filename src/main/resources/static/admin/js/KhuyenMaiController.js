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
            document.getElementById("errToiThieu").innerText = "";
            document.getElementById("errDate").innerText = "";
            document.getElementById("errGiaTri").innerText = "";
    }

///add
    $scope.addKM = function (){
        if ($scope.validateAddKM() == 0){
            return;
        }
        var khuyen_mai = {
            gia_tri: Number($scope.newKM.gia_tri),
            gia_tri_toi_thieu: Number($scope.newKM.gia_tri_toi_thieu),
            ngay_bat_dau: new Date($scope.newKM.ngay_bat_dau),
            ngay_ket_thuc: new Date($scope.newKM.ngay_ket_thuc),
            trang_thai : 1
        }

        $http.post('http://localhost:8080/rest/khuyen-mai', khuyen_mai)
            .then(function (respone){
                // console.log(khuyen_mai)

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
        if (isNaN($scope.updateKM.gia_tri) || $scope.updateKM.gia_tri <=0 || $scope.updateKM.gia_tri > 100){
            toastr.warning('Sai giá trị', 'OK')
            return;
        }
        if (isNaN($scope.updateKM.gia_tri_toi_thieu) || $scope.updateKM.gia_tri_toi_thieu <=0){
            toastr.warning('Sai giá trị tối thiểu', 'OK')
            return;
        }
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


    $scope.validateAddKM = function (){
        let err = false;

        if(isNaN($scope.newKM.gia_tri)){
            document.getElementById("errGiaTri").innerText = "Sai định dạng giá trị";
            err = true;
        }else if ($scope.newKM.gia_tri <= 0 || $scope.newKM.gia_tri > 100){
            document.getElementById("errGiaTri").innerText = "Giá trị phải nằm trong khoảng 1-100";
            err = true;
        }

        if(isNaN($scope.newKM.gia_tri_toi_thieu)){
            document.getElementById("errToiThieu").innerText = "Sai định dạng giá trị tối thiểu";
            err = true;
        }else if ($scope.newKM.gia_tri_toi_thieu <= 0 ){
            document.getElementById("errToiThieu").innerText = "Giá trị tối thiểu phải lớn hơn 0";
            err = true;
        }

        if (!($scope.newKM.ngay_bat_dau) || !($scope.newKM.ngay_ket_thuc) ){
            document.getElementById("errDate").innerText = "Không để trống ngày";
            err = true;
        }else if (new Date($scope.newKM.ngay_bat_dau).setHours(0, 0, 0, 0) < new Date().setHours(0, 0, 0, 0) ){
            document.getElementById("errDate").innerText = "Ngày bắt đầu phải hơn hoặc bằng ngày hiện tại";
            err = true;
        } else if (new Date($scope.newKM.ngay_bat_dau) >= new Date($scope.newKM.ngay_ket_thuc) ){
            document.getElementById("errDate").innerText = "Ngày kết thúc phải lớn hơn ngày bắt đầu";
            err = true;
        }

        return err ? 0 : 1;

    }

    ///filter khuyến mại
    $scope.filterKM = {
        ngay_bat_dau: null,
        ngay_ket_thuc: null,
        trang_thai : null
    }

    $scope.searchKhuyenMai = function (){
        var ngayBatDau = null;
        var ngayKetThuc = null;

        if ($scope.filterKM.ngay_bat_dau != null){
                  ngayBatDau = new Date($scope.filterKM.ngay_bat_dau).toISOString().split('T')[0];
        }
        if ($scope.filterKM.ngay_ket_thuc != null){
            ngayKetThuc = new Date($scope.filterKM.ngay_ket_thuc).toISOString().split('T')[0];
        }

        $http({
            method: 'GET',
            url : 'http://localhost:8080/rest/khuyen-mai/filters',
            params: {
                ngay_bat_dau : ngayBatDau,
                ngay_ket_thuc : ngayKetThuc,
                trang_thai : $scope.filterKM.trang_thai
            }
        }).then(function(response) {
            if (response.data.content.length == 0){
                toastr.warning('Không tìm thấy khuyến mại thỏa mãn', 'OK');
            }
            else{
                $scope.listKM = response.data.content;
                toastr.success('Lọc thành công', 'OK');
            }
        }).catch(function(error) {
            console.error('Error fetching data:', error);
        });

    }

    $scope.resetFilters = function (){
        $scope.filterKM.ngay_bat_dau = null;
        $scope.filterKM.ngay_ket_thuc = null;
        $scope.filterKM.trang_thai =null;
        $scope.getData($scope.currentPage);
    }

})