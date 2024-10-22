

// show giao diện tìm kiếm
function searchPopup() {
    // Mở hộp thoại tìm kiếm khi nhấn nút tìm kiếm
    $('#header-nav').on('click', '.search-button', function(e) {
        e.preventDefault();
        $('.search-popup').toggleClass('is-visible');
    });

    // Đóng hộp thoại tìm kiếm khi nhấn nút đóng
    $('#header-nav').on('click', '.btn-close-search', function(e) {
        e.preventDefault();
        $('.search-popup').toggleClass('is-visible');
    });

    // Mở hộp thoại tìm kiếm từ các phần tử có class `search-popup-trigger`
    $(".search-popup-trigger").on("click", function(e) {
        e.preventDefault();
        $(".search-popup").addClass("is-visible");
        // Tự động focus vào phần tử tìm kiếm sau 350ms
        setTimeout(function() {
            $(".search-popup").find("#search-popup").focus();
        }, 350);
    });

    // Đóng hộp thoại tìm kiếm khi click vào các phần tử liên quan hoặc ngoài popup
    $(".search-popup").on("click", function(e) {
        if ($(e.target).is(".search-popup-close") ||
            $(e.target).is(".search-popup-close svg") ||
            $(e.target).is(".search-popup-close path") ||
            $(e.target).is(".search-popup")) {
            e.preventDefault();
            $(this).removeClass("is-visible");
        }
    });

    // Đóng hộp thoại tìm kiếm khi nhấn phím ESC
    $(document).keyup(function(e) {
        if (e.which === 27) { // Phím ESC có mã là 27
            $(".search-popup").removeClass("is-visible");
        }
    });
};

