app.controller('SanPhamController', function($scope, $http,$window) {
    // Khởi tạo tham số phân trang
    $scope.currentPage = 0;

    $scope.getData = function(page) {
        // Kiểm tra xem page có hợp lệ không (giới hạn từ 0 đến totalPages - 1)
        if (page < 0 || page >= $scope.totalPages) return; // Tránh việc chuyển đến trang không hợp lệ

        // Gọi API lấy danh sách sản phẩm với các tham số lọc
        $http.get('http://localhost:8080/rest/san-pham', {
            params: {
                page: page, // Trang hiện tại
                size: 12,    // Số sản phẩm trên mỗi trang
            }
        })
            .then(function(response) {
                $scope.sp = response.data.content; // 'content' chứa danh sách sản phẩm
                $scope.totalPages = response.data.totalPages; // Số trang tổng cộng
            })
            .catch(function(error) {
                console.error('Error fetching data:', error);
            });
    };


    // Gọi API lấy danh sách màu sắc
    $scope.getMauSacList = function() {
        $http.get('http://localhost:8080/rest/mau-sac')
            .then(function(response) {
                $scope.colors = response.data; // Lưu dữ liệu màu sắc vào scope
            })
            .catch(function(error) {
                console.error('Error fetching colors:', error);
            });
    };

    // Gọi API lấy danh sách kích cỡ
    $scope.getKichCoList = function() {
        $http.get('http://localhost:8080/rest/kich-co/list')
            .then(function(response) {
                $scope.sizes = response.data; // Lưu dữ liệu kích cỡ vào scope
            })
            .catch(function(error) {
                console.error('Error fetching sizes:', error);
            });
    };

    // Gọi các API này khi trang được tải
    $scope.getMauSacList();
    $scope.getKichCoList();

    $scope.filters = {
        color: '',
        size: '',
        minPrice: '',
        maxPrice: ''
    };

    // Hàm áp dụng bộ lọc
    $scope.applyFilters = function() {
        const params = {
            page: 0, // Luôn bắt đầu từ trang đầu tiên khi lọc
            size: 12, // Số sản phẩm trên mỗi trang
            color: $scope.filters.color,
            size: $scope.filters.size,
            minPrice: $scope.removeCurrencyFormat($scope.filters.minPrice), // Loại bỏ định dạng tiền tệ
            maxPrice: $scope.removeCurrencyFormat($scope.filters.maxPrice)  // Loại bỏ định dạng tiền tệ
        };

        // Gọi API với các tham số lọc
        $http.get('http://localhost:8080/rest/san-pham/filler', { params: params })
            .then(function(response) {
                $scope.sp = response.data.content; // Dữ liệu sản phẩm
                $scope.totalPages = response.data.totalPages; // Tổng số trang
            })
            .catch(function(error) {
                console.error('Error applying filters:', error);
            });
    };
    // Hàm định dạng số thành tiền tệ
    $scope.formatCurrency = function (field) {
        const value = $scope.filters[field];
        if (!value) {
            $scope.filters[field] = '';
            return;
        }

        // Loại bỏ các ký tự không phải số
        const numericValue = value.toString().replace(/[^\d]/g, '');

        // Định dạng số thành tiền tệ
        const formattedValue = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
            minimumFractionDigits: 0
        }).format(numericValue);

        // Cập nhật lại giá trị hiển thị trong ô input
        $scope.filters[field] = formattedValue;
    };

    $scope.removeCurrencyFormat = function(value) {
        if (!value) return null; // Trả về null nếu không có giá trị
        return Number(value.toString().replace(/[^\d]/g, ''));
    };

    $scope.resetFilters = function() {
        // Đặt lại các giá trị lọc về mặc định
        $scope.filters = {
            color: '',
            size: '',
            minPrice: '',
            maxPrice: ''
        };

        // Gọi hàm để lấy dữ liệu trang đầu tiên
        $scope.getData($scope.currentPage);

    };




    // Gọi hàm để lấy dữ liệu trang đầu tiên
    $scope.getData($scope.currentPage);

    // Hàm để chuyển trang
    $scope.changePage = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getData($scope.currentPage); // Gọi lại dữ liệu cho trang mới
        }
    };


    $scope.isModalOpen = false;
    $scope.selectedProduct = null;
    $scope.quantity = 1;

    $scope.selectedColor = null;
    $scope.selectedSize = null;
    $scope.selectedVariant = null;

    // Hàm mở modal
    $scope.openModal = function(product) {
        $scope.selectedProduct = product;
        $scope.quantity = 1;
        $('#productModal').modal('show'); // Sử dụng jQuery để hiển thị modal
    };

    // Hàm lấy danh sách màu sắc duy nhất
    $scope.getUniqueColors = function() {
        const uniqueColors = [];
        const colorSet = new Set();

        $scope.selectedProduct.variants.forEach(variant => {
            if (!colorSet.has(variant.mauSac.ten)) {
                colorSet.add(variant.mauSac.ten);
                uniqueColors.push(variant);
            }
        });

        return uniqueColors;
    };

    // Chọn màu sắc
    $scope.selectColor = function(color) {
        $scope.selectedColor = color;
        $scope.selectedSize = null;
        $scope.selectedVariant = null;
    };

    // Chọn kích cỡ và cập nhật sản phẩm chi tiết
    $scope.selectSize = function(size, variant) {
        $scope.selectedSize = size;
        $scope.selectedVariant = variant;
    };



    // Hàm giảm số lượng
    $scope.decreaseQuantity = function() {
        if ($scope.quantity > 1) {
            $scope.quantity--;
        }
    };

    // Hàm tăng số lượng
    $scope.increaseQuantity = function() {
        $scope.quantity++;
    };


    // Khởi tạo giỏ hàng từ LocalStorage nếu có dữ liệu
    $scope.cart = JSON.parse(localStorage.getItem('cart')) || [];

    // Hàm lưu giỏ hàng vào LocalStorage
    $scope.saveCart = function() {
        localStorage.setItem('cart', JSON.stringify($scope.cart));
        // console.log($scope.cart)
    };

    // Hàm đếm số lượng sản phẩm trong giỏ hàng
    $scope.getCartItemCount = function() {
        return $scope.cart.length; // Đếm số mục trong giỏ hàng
    };

    // Hàm thêm sản phẩm vào giỏ hàng
    $scope.addToCart = function(selectedProduct) {
        if ($scope.selectedVariant) {

            // Kiểm tra số lượng tồn
            if ($scope.selectedVariant.so_luong === 0) {
                toastr.error('Sản phẩm này đã hết hàng');
                return; // Dừng hàm nếu hết hàng
            }

            // Kiểm tra xem sản phẩm đã có trong giỏ chưa
            const existingProduct = $scope.cart.find(item =>
                item.product.id === selectedProduct.id && item.variant.id === $scope.selectedVariant.id
            );

            if (existingProduct) {
                // Kiểm tra tổng số lượng sau khi thêm
                const totalQuantity = existingProduct.quantity + $scope.quantity;
                if (totalQuantity > $scope.selectedVariant.so_luong) {
                    toastr.error('Không thể thêm sản phẩm vượt quá số lượng tồn kho');
                    return; // Dừng nếu vượt quá tồn kho
                }
                if ($scope.quantity <= 0) {
                    toastr.error('Số lượng phải lớn hơn 0');
                    return; // Dừng nếu số lượng <= 0
                }
                // Nếu sản phẩm đã tồn tại, tăng số lượng
                existingProduct.quantity += $scope.quantity;
            } else {
                // Kiểm tra số lượng thêm vào lần đầu
                if ($scope.quantity > $scope.selectedVariant.so_luong) {
                    toastr.error('Số lượng không được vượt quá số lượng tồn kho');
                    return; // Dừng nếu vượt quá tồn kho
                }
                if ($scope.quantity <= 0) {
                    toastr.error('Số lượng phải lớn hơn 0');
                    return; // Dừng nếu số lượng <= 0
                }
                // Nếu sản phẩm chưa tồn tại, thêm vào giỏ
                $scope.cart.push({
                    product: selectedProduct,
                    variant: $scope.selectedVariant,
                    quantity: $scope.quantity
                });
            }
            // Lưu giỏ hàng vào LocalStorage
            $scope.saveCart();
            // Hiển thị thông báo toastr thành công
            toastr.success('Sản phẩm đã được thêm vào giỏ hàng', '', {
                timeOut: 500,  // Hiển thị toastr trong 1 giây (1000ms)
                onHidden: function() {
                    // Đợi 0.5 giây (500ms) rồi reload trang
                    setTimeout(function() {
                        window.location.reload(); // Tải lại trang sau khi 0.5 giây
                    }, 200); // 500ms = 0.5 giây
                }
            });

            // Cập nhật giao diện ngay lập tức
            $timeout(function() {
                $scope.getCartItemCount();
            }, 0); // Đặt thời gian là 0 để kích hoạt ngay
        } else {
            toastr.error('Vui lòng chọn màu sắc và kích cỡ');
        }
    };

    // Hàm cập nhật giỏ hàng khi số lượng thay đổi
    $scope.updateCart = function() {
        $scope.cart.forEach(item => {
            // Kiểm tra nếu số lượng nhỏ hơn 1
            if (item.quantity < 1) {
                toastr.error('Số lượng phải lớn hơn hoặc bằng 1');
                item.quantity = 1; // Đặt lại số lượng tối thiểu
            }

            // Kiểm tra nếu số lượng vượt quá tồn kho
            if (item.quantity > item.variant.so_luong) {
                toastr.error('Số lượng yêu cầu vượt quá số lượng tồn kho');
                item.quantity = item.variant.so_luong; // Đặt lại số lượng tối đa
            }
        });

        // Loại bỏ các sản phẩm có số lượng bằng 0 (nếu cần)
        $scope.cart = $scope.cart.filter(item => item.quantity > 0);

        // Cập nhật LocalStorage sau khi thay đổi
        $scope.saveCart();

        // Có thể cập nhật giao diện hoặc tổng tiền nếu cần
        toastr.success('Cập nhật giỏ hàng thành công');
    };


    // Xóa sản phẩm khỏi giỏ hàng
    $scope.removeFromCart = function(index) {
        $scope.cart.splice(index, 1);
        $scope.saveCart(); // Cập nhật lại LocalStorage sau khi xóa

        // Cập nhật lại số lượng sản phẩm hiển thị trên icon giỏ hàng
        $scope.getCartItemCount(); // Gọi lại để cập nhật số sản phẩm

        // Hiển thị thông báo toastr
        toastr.info('Sản phẩm đã được xóa khỏi giỏ hàng', '', {
            timeOut: 500,  // Hiển thị toastr trong 1 giây (1000ms)
            onHidden: function() {
                // Đợi 2 giây rồi reload trang
                setTimeout(function() {
                    window.location.reload(); // Tải lại trang sau khi 2 giây
                }, 300); // 2000ms = 2 giây
            }
        });
    };



    // Hàm tính tổng tiền từng sản phẩm trong giỏ
    $scope.calculateTotalPrice = function(item) {
        // Kiểm tra item và item.variant có tồn tại hay không
        if (!item || !item.variant) {
            console.warn("Item hoặc variant không xác định:", item);
            return 0; // Trả về 0 nếu item hoặc item.variant không tồn tại
        }
        let price = item.variant.gia_ban;
        if (item.variant.giamGia) {
            price = price * (1 - item.variant.giamGia.gia_tri / 100);
        }
        return price * item.quantity;
    };

    // Hàm tính tổng giá trị đơn hàng
    $scope.calculateCartTotal = function() {
        return $scope.cart.reduce((total, item) => total + $scope.calculateTotalPrice(item), 0);
    };

    // Lấy ra khuyến mãi
    $scope.getVouchersByTotal = function () {
        const tongTienHoaDon = $scope.calculateCartTotal(); // Tính tổng giá trị hóa đơn
        $http({
            method: 'GET',
            url: '/api/vorcher/filter',
            params: { tongTienHoaDon: tongTienHoaDon }
        }).then(function (response) {
            $scope.vouchers = response.data; // Gán dữ liệu trả về vào danh sách vouchers
        }).catch(function (error) {
            console.error("Lỗi khi lấy danh sách khuyến mãi:", error);
        });
    };

    // Áp dụng voucher
    $scope.applyVoucher = function (voucher) {
        $scope.selectedVoucher = voucher; // Lưu voucher được chọn
        $scope.idSelectedVoucher = voucher.id; // Lưu id voucher được chọn
        const tongTienHoaDon = $scope.calculateCartTotal();

        // Tính giá trị giảm (giới hạn không giảm quá tổng tiền)
        $scope.discountValue = Math.min(
            tongTienHoaDon * (voucher.gia_tri / 100),
            tongTienHoaDon
        );
        toastr.success('Áp dụng voucher thành công !','success');

        // Tự động đóng modal
        const modalElement = document.getElementById('voucherModal');
        const modal = bootstrap.Modal.getInstance(modalElement);
        modal.hide();
    };



    // Lấy thông tin người dùng từ localStorage
    const savedUser = JSON.parse(localStorage.getItem("user"));
    if (savedUser) {
        // Tự động điền thông tin vào các ng-model
        $scope.name = savedUser.ho_ten;
        $scope.phone = savedUser.sdt;
        $scope.email = savedUser.email;
        // Lấy địa chỉ có trang_thai == 1
        const activeAddress = savedUser.diaChiKhachHang.find(address => address.trang_thai === 1);
        $scope.addressDetail = activeAddress ? activeAddress.dia_chi_chi_tiet : ""; // Nếu không tìm thấy, để trống
    }

    // Hàm xác nhận thanh toán đơn hàng
    $scope.confirmOrder = function() {
        if (document.getElementById('agreeTerms').checked) {

            // Validate thông tin người nhận
            if (!$scope.name || $scope.name.trim() === '') {
                toastr.error('Vui lòng nhập tên người nhận!');
                return;
            }
            if (!$scope.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test($scope.email)) {
                toastr.error('Vui lòng nhập email hợp lệ!');
                return;
            }
            if (!$scope.phone || !/^\d{10,11}$/.test($scope.phone)) {
                toastr.error('Vui lòng nhập số điện thoại hợp lệ (10-11 số)!');
                return;
            }
            if (!$scope.addressDetail || $scope.addressDetail === '') {
                toastr.error('Vui lòng nhập địa chỉ giao hàng!');
                return;
            }

            // Kiểm tra phương thức thanh toán
            if (!$scope.paymentMethod) {
                toastr.error('Vui lòng chọn phương thức thanh toán!');
                return;
            }

            let hoaDon = {
                tien_sau_giam_gia : $scope.calculateCartTotal() - $scope.discountValue ,
                thanh_tien : $scope.calculateCartTotal(),
                phuong_thuc_thanh_toan: $scope.paymentMethod, // 1: COD, 2: Bank Transfer
                loai_hoa_don: 2,
                dia_chi_nguoi_nhan : $scope.getFullAddress(),
                ten_nguoi_nhan : $scope.name ,
                email_nguoi_nhan : $scope.email ,
                sdt_nguoi_nhan : $scope.phone ,
                mo_ta: $scope.note,
                trang_thai: 1
            };

            // Kiểm tra savedUser và gán idKhachHang
            let idKhachHang = (savedUser && savedUser.id != null) ? savedUser.id : 1;
            let idKhuyenMai = $scope.idSelectedVoucher ? $scope.idSelectedVoucher : 1; // Kiểm tra nếu không có voucher thì gán null

            $http.post('/rest/hoa-don/add', hoaDon ,{
              params: { idKhachHang : idKhachHang , idKhuyenMai : idKhuyenMai } //  Thêm params `idKhachHang` từ localStorage
            }).then(function(response) {
                        if (response.data && response.data.id) {
                            let hoaDonId = response.data.id;
                            console.log("id hóa đơn" ,hoaDonId)
                            let promises = [];

                            // Bước 4: Tạo hóa đơn chi tiết cho từng sản phẩm trong giỏ hàng
                            $scope.cart.forEach(function(item) {
                                let hoaDonChiTiet = {
                                    hoaDon: { id: hoaDonId },
                                    chi_tiet_san_pham: { id: item.variant.id },
                                    so_luong: item.quantity,
                                    don_gia: item.variant.gia_ban
                                };
                                promises.push($http.post('/rest/hoa-don-chi-tiet/add-customer', hoaDonChiTiet,{
                                    params: { idHoaDon: hoaDonId }
                                }));
                            });

                            return Promise.all(promises);


                        } else {
                            throw new Error("Không thể tạo hóa đơn");
                        }
                    })
                    .then(function() {
                        toastr.success('Đặt hàng thành công!');
                        // Clear cart and redirect to another page if needed
                        $scope.cart = [];
                        $scope.saveCart();
                        // Chuyển hướng sang trang khác sau khi đặt hàng thành công
                        $window.location.href = 'http://localhost:8080/index#!/san-pham';
                        setTimeout(function() {
                            $window.location.reload();
                        }, 200); // Đợi 1 giây để chuyển hướng trước khi tải lại trang
                    })
                    .catch(function(error) {
                        console.error("Đã xảy ra lỗi:", error);
                        toastr.error("Đã xảy ra lỗi khi đặt hàng!");
                    });
        } else {
            toastr.error('Vui lòng đồng ý với các điều khoản chính sách giao hàng');
        }
    };


    // Khởi tạo các biến lưu trữ danh sách tỉnh/thành phố và quận/huyện
    $scope.cities = [];
    $scope.districts = [];

    // Hàm để lấy danh sách tỉnh/thành phố từ API
    $scope.loadCities = function() {
        $http.get('https://provinces.open-api.vn/api/?depth=2')
            .then(function(response) {
                $scope.cities = response.data;
            })
            .catch(function(error) {
                console.error("Lỗi khi tải danh sách tỉnh/thành phố:", error);
            });
    };

    // Hàm để cập nhật danh sách quận/huyện dựa trên tỉnh/thành phố được chọn
    $scope.updateDistricts = function() {
        console.log($scope.selectedCity)
        console.log($scope.selectedCity.name)
        if ($scope.selectedCity) {
            $scope.districts = $scope.selectedCity.districts;
        } else {
            $scope.districts = [];
        }
    };

    // Gọi hàm loadCities để tải danh sách tỉnh/thành phố ngay khi controller được khởi tạo
    $scope.loadCities();

    // Hàm để lấy địa chỉ đầy đủ từ các trường thông tin địa chỉ
    $scope.getFullAddress = function() {
        let addressDetail = $scope.addressDetail || ""; // Địa chỉ chi tiết
        let city = $scope.selectedCity ? $scope.selectedCity.name : ""; // Tên tỉnh/thành phố
        let district = $scope.selectedDistrict ? $scope.selectedDistrict.name : ""; // Tên quận/huyện

        // Nối các phần địa chỉ lại với nhau
        return `${addressDetail}, ${district}, ${city}`;
    };

    // Gọi API để lấy sản phẩm bán chạy ( sản phẩm bán chạy ở trang chủ )
    $http.get('http://localhost:8080/rest/san-pham/top-selling')
        .then(function(response) {
            $scope.products = response.data;
        }, function(error) {
            console.error('Có lỗi khi lấy dữ liệu:', error);
        });

    // Gọi API để lấy sản phẩm mới nhất ( sản phẩm new ở trang chủ )
    $http.get('http://localhost:8080/rest/san-pham/latest')
        .then(function(response) {
            $scope.productsNew = response.data;
        }, function(error) {
            console.error('Có lỗi khi lấy dữ liệu:', error);
        });







});

// Chuyển sang dạng VNĐ
app.filter('vndCurrency', function() {
    return function(amount) {
        if (!amount) return '0 VNĐ';
        return parseFloat(amount).toLocaleString('vi-VN') + ' VNĐ';
    };
});
