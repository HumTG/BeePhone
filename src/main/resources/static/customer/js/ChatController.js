app.controller('ChatController', function ($scope, $http, $window) {

    // Kết nối WebSocket
    var socket = new SockJS('/chat'); // Kết nối tới WebSocket
    var stompClient = Stomp.over(socket); // Sử dụng STOMP protocol

    // Lắng nghe tin nhắn từ server
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (messageOutput) {
            var message = JSON.parse(messageOutput.body);
            $scope.messages.push(message); // Thêm tin nhắn vào danh sách
            $scope.$apply(); // Cập nhật view
        });
    });

    // Khởi tạo danh sách tin nhắn
    $scope.messages = [];

    // Lấy thông tin người dùng từ localStorage
    const savedUser = JSON.parse(localStorage.getItem("user"));

    // Gửi tin nhắn
    $scope.sendMessage = function () {
        if ($scope.messageContent.trim() === "") {
            alert("Bạn cần nhập nội dung tin nhắn.");
            return;
        }

        var message = {
            senderId: savedUser.id, // ID của người gửi
            senderType: savedUser.ho_ten, // Tên khách hàng
            content: $scope.messageContent, // Nội dung tin nhắn
            timestamp: new Date().toISOString() // Thời gian gửi tin nhắn
        };

        stompClient.send("/app/sendMessage", {}, JSON.stringify(message)); // Gửi tin nhắn lên server
        $scope.messageContent = ""; // Làm trống ô nhập tin nhắn
    };

});
