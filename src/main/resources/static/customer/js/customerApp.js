var app = angular.module('customerApp', ['ngRoute']);

app.config(function ($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "/customer/views/home.html",
            controller: "HomeController"
        })
        .when("/san-pham", {
            templateUrl: "/customer/views/san-pham.html",
            controller: "SanPhamController"
        })
        .when("/san-pham/detail/:idSP", {
            templateUrl: "/customer/views/san-pham-detail.html",
            controller: "SanPhamDetailController"
        })
        .when("/cart", {
            templateUrl: "/customer/views/cart.html",
            controller: "SanPhamController"
        })
        .when("/thanh-toan", {
            templateUrl: "/customer/views/thanh-toan.html",
            controller: "SanPhamController"
        })
        .when("/don-hang", {
            templateUrl: "/customer/views/don-hang.html",
            controller: "DonHangController"
        })
        .when("/chat", {
            templateUrl: "/customer/views/chat.html",
            controller: "ChatController"
        })
        .when("/thong-tin", {
            templateUrl: "/customer/views/thong-tin.html",
            controller: "ThongTinController"
        })
        .when("/doi-mat-khau", {
            templateUrl: "/customer/views/doi-mat-khau.html",
            controller: "ThongTinController"
        })
        .when("/dia-chi", {
            templateUrl: "/customer/views/dia-chi.html",
            controller: "ThongTinController"
        })
        .when("/tra-cuu-don-hang", {
            templateUrl: "/customer/views/tra-cuu-don-hang.html",
            controller: "TraCuuController"
        })
        .otherwise({
            redirectTo: '/'
        });

});


app.controller('HomeController', function ($scope, $http, $window) {
    // Kiểm tra trạng thái thông báo
    const loginSuccess = localStorage.getItem("loginSuccess");
    if (loginSuccess === "true") {
        // Hiển thị thông báo
        toastr.success('Đăng nhập thành công!', 'Success');

        // Xóa trạng thái để không hiển thị lại khi tải lại trang
        localStorage.removeItem("loginSuccess");
    }
    // Kiểm tra trạng thái đăng nhập khi khởi chạy controller
    const savedUser = localStorage.getItem("user");
    if (savedUser) {
        $scope.isLoggedIn = true;
        $scope.user = JSON.parse(savedUser);
    } else {
        $scope.isLoggedIn = false;
        $scope.user = null;
    }

    // Hàm để thiết lập trạng thái đăng nhập
    $scope.setLoggedIn = function (user) {
        $scope.isLoggedIn = true;
        $scope.user = user;

        // Lưu vào localStorage
        localStorage.setItem("user", JSON.stringify(user));
    };

    // Hàm để xử lý đăng xuất
    $scope.logout = function () {
        $scope.isLoggedIn = false;
        $scope.user = null;

        // Xóa thông tin khỏi localStorage
        localStorage.removeItem("user");
        localStorage.removeItem("cart");
        // Xóa trạng thái để không hiển thị lại khi tải lại trang
        localStorage.removeItem("loginSuccess");
        // Điều hướng về trang chủ hoặc trang đăng nhập
        // toastr.success('Đăng xuất thành công!', 'Success');
        // Hiển thị thông báo toastr
        toastr.success('Đăng xuất thành công', 'Success', {
            timeOut: 200,  // Hiển thị toastr trong 1 giây (1000ms)
            onHidden: function() {
                // Đợi 2 giây rồi reload trang
                setTimeout(function() {
                    window.location.reload(); // Tải lại trang sau khi 2 giây
                }, 100); // 2000ms = 2 giây
            }
        });
    };

    // Các sản phẩm bán chạy
});


// Tạo filter để định dạng giá tiền Việt Nam Đồng
app.filter('currencyVND', function () {
    return function (input) {
        if (!input) return '';
        // Chuyển số thành chuỗi với định dạng 1.000.000
        return parseFloat(input).toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
    };
});
