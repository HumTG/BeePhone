app.controller('NavController', function($scope) {
    $scope.menuItems = [
        { name: 'Trang chủ', link: '#' },
        { name: 'Sản Phẩm', link: '#!san-pham' },
        { name: 'Giới thiệu', link: '#' },
        { name: 'Liên hệ', link: '#' },
        { name: 'Tuyển dụng', link: '#' }
    ];
});