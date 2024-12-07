var app = angular.module('NhanVienApp', []);
var host = "http://localhost:8080/rest/nhan-vien";

app.controller('NhanVienController', function($scope, $http ) {

    var successMessage = localStorage.getItem('successMessage');
    if (successMessage) {
        // Hiển thị toastr thông báo
        toastr.success(successMessage, 'Thành công', {
            closeButton: true,
            progressBar: true,
            timeOut: 3000
        });

        // Xóa thông báo sau khi hiển thị
        localStorage.removeItem('successMessage');
    }

    // Khởi tạo tham số phân trang
    $scope.currentPage = 0;

    // Hàm để lấy dữ liệu từ API
    $scope.getData = function(page) {
        $http.get(host, { params: { page: page } })
            .then(function(response) {
                // Lưu dữ liệu vào scope để hiển thị
                $scope.nv = response.data.content; // 'content' là nơi chứa danh sách sản phẩm
                $scope.totalPages = response.data.totalPages; // Số trang tổng cộng
            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
    };

    // Gọi hàm để lấy dữ liệu trang đầu tiên
    $scope.getData($scope.currentPage);

    // Hàm để chuyển trang
    $scope.changePage = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getData($scope.currentPage);
        }
    };


    // Hàm để mở modal và hiển thị thông tin chi tiết
    $scope.openDetail = function(id) {
        var hostNV = "http://localhost:8080/rest/nhan-vien/"+id;
        $http.get(hostNV)
            .then(function(response) {
                // Lưu dữ liệu vào scope để hiển thị
                $scope.nvct = response.data;

                // Chuyển đổi giới tính
                if ($scope.nvct.gioi_tinh === 0) {
                    $scope.nvct.gioi_tinh_display = 'Nam';
                } else {
                    $scope.nvct.gioi_tinh_display = 'Nữ';
                }

                // Kiểm tra và chuyển đổi 'ngay_sinh' thành kiểu Date
                if ($scope.nvct.ngay_sinh) {
                    // Chuyển đổi từ chuỗi 'yyyy-MM-dd' sang đối tượng Date
                    $scope.nvct.ngay_sinh = new Date($scope.nvct.ngay_sinh);
                }

                // $scope.showSuccessToast('Dữ liệu đã được lưu thành công!');
                console.log(id)

            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
        var detailModal = new bootstrap.Modal(document.getElementById('detailModal')); // Sử dụng Bootstrap Modal
        detailModal.show(); // Hiển thị modal
    };

    $scope.imageSrc = null; // Lưu ảnh đã chọn
    $scope.imageName = null; // Lưu tên ảnh đã chọn

    $scope.triggerFileInput = function () {
        document.getElementById('fileInput').click();
    };

    $scope.uploadFile = function (files) {
        if (files && files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $scope.$apply(function () {
                    $scope.imageSrc = e.target.result; // Hiển thị ảnh
                });
            };
            $scope.imageName = files[0].name ;
            reader.readAsDataURL(files[0]); // Đọc ảnh dưới dạng URL
            console.log('Tên file đã chọn:', files[0].name); // In ra tên file trong console
        }
    };

    // Thêm nhân viên
    $scope.confirmAddNhanVien = function() {
        var nhanVienData = {
            ho_ten: $scope.nhanVien.ho_ten,
            email: $scope.nhanVien.email,
            sdt: $scope.nhanVien.sdt,
            ngay_sinh: new Date($scope.nhanVien.ngay_sinh).toISOString().split('T')[0],
            gioi_tinh: $scope.nhanVien.gioi_tinh,
            dia_chi: $scope.nhanVien.dia_chi,
            trang_thai: $scope.nhanVien.trang_thai,
            hinh_anh: $scope.imageName
        };

        $http.post('http://localhost:8080/rest/nhan-vien', nhanVienData)
            .then(function(response) {
                localStorage.setItem('successMessage', 'Nhân viên đã được thêm thành công!');
                window.location.href = 'http://localhost:8080/admin/nhan-vien';
            })
            .catch(function(error) {
                console.error('L��i khi thêm nhân viên:', error);
                if (error.data) {
                    toastr.error(error.data, 'Lỗi', {
                        closeButton: true,
                        progressBar: true,
                        timeOut: 3000
                    });
                } else {
                    toastr.error('Có lỗi xảy ra!', 'Error', {
                        closeButton: true,
                        progressBar: true,
                        timeOut: 3000
                    });
                }
            });

        // Ẩn modal sau khi xác nhận
        var modalElement = document.getElementById('confirmModal');
        var modal = bootstrap.Modal.getInstance(modalElement);
        modal.hide();
    };

    // Update nhân viên
    $scope.openUpdateModal = function(nhanVien) {
        $scope.selectedNhanVien = angular.copy(nhanVien);

        // Chuyển đổi 'ngay_sinh' từ chuỗi sang đối tượng Date (nếu tồn tại)
        if ($scope.selectedNhanVien.ngay_sinh) {
            $scope.selectedNhanVien.ngay_sinh = new Date($scope.selectedNhanVien.ngay_sinh);
        }

        // Đảm bảo các giá trị giới tính và trạng thái được chuyển đúng kiểu
        $scope.selectedNhanVien.gioi_tinh = parseInt($scope.selectedNhanVien.gioi_tinh);
        $scope.selectedNhanVien.trang_thai = parseInt($scope.selectedNhanVien.trang_thai, 10);

        var updateModal = new bootstrap.Modal(document.getElementById('updateNhanVienModal'));
        updateModal.show(); // Hiển thị modal
    };



    $scope.updateNhanVien = function() {
        var nhanVienData = {
            ho_ten: $scope.selectedNhanVien.ho_ten,
            email: $scope.selectedNhanVien.email,
            sdt: $scope.selectedNhanVien.sdt,
            ngay_sinh: new Date($scope.selectedNhanVien.ngay_sinh).toISOString().split('T')[0],
            dia_chi: $scope.selectedNhanVien.dia_chi,
            gioi_tinh: $scope.selectedNhanVien.gioi_tinh,
            trang_thai: $scope.selectedNhanVien.trang_thai,
            hinh_anh: $scope.selectedNhanVien.hinh_anh
        };

        $http.put('http://localhost:8080/rest/nhan-vien/' + $scope.selectedNhanVien.id, nhanVienData)
            .then(function(response) {
                toastr.success('Cập nhật nhân viên thành công!', 'Thành công', {
                    closeButton: true,
                    progressBar: true,
                    timeOut: 3000
                });

                $scope.getData($scope.currentPage); // Tải lại dữ liệu sau khi cập nhật

                var modalElement = document.getElementById('updateNhanVienModal');
                var modal = bootstrap.Modal.getInstance(modalElement);
                modal.hide(); // Đóng modal sau khi cập nhật
            })
            .catch(function(error) {
                console.error('Lỗi khi cập nhật nhân viên:', error);
                toastr.error('Có lỗi xảy ra khi cập nhật khách hàng!', 'Lỗi', {
                    closeButton: true,
                    progressBar: true,
                    timeOut: 3000
                });
            });
    };

    // Search nhân viên
    $scope.searchNhanVien = function() {
        const params = {
            tenSdt: $scope.filter.ten_sdt || '',
            ngaySinhTu: $scope.filter.ngay_sinh_tu ? new Date($scope.filter.ngay_sinh_tu).toISOString().split('T')[0] : null,
            ngaySinhDen: $scope.filter.ngay_sinh_den ? new Date($scope.filter.ngay_sinh_den).toISOString().split('T')[0] : null,
            trangThai: $scope.filter.trang_thai || null,
            maxTuoi: $scope.filter.khoang_tuoi || null,
            page: $scope.currentPage || 0,
            size: 10
        };

        $http.get('http://localhost:8080/rest/nhan-vien/filter', { params: params })
            .then(function(response) {
                $scope.nv = response.data.content;
                $scope.totalPages = response.data.totalPages;
            })
            .catch(function(error) {
                console.error('Lỗi khi tìm kiếm nhân viên:', error);
                toastr.error('Có lỗi xảy ra khi tìm kiếm!', 'Lỗi', {
                    closeButton: true,
                    progressBar: true,
                    timeOut: 3000
                });
            });
    };

    // Hàm để reset bộ lọc
    $scope.resetFilter = function() {
        // Đặt lại các giá trị filter về mặc định
        $scope.filter = {
            ten_sdt: '',
            ngay_sinh_tu: null,
            ngay_sinh_den: null,
            trang_thai: '',
            khoang_tuoi: 60 // Giá trị mặc định là 60 tuổi
        };

        // Đặt lại trang hiện tại về trang đầu tiên
        $scope.currentPage = 0;

        // Lấy lại danh sách nhân viên với bộ lọc đã được reset
        $scope.getData($scope.currentPage);
    };







});
