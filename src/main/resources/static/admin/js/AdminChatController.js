var app = angular.module('ChatApp', []);


app.controller('AdminChatController', function ($scope, $http, $window) {
    var socket = new SockJS('/chat'); // Kết nối tới WebSocket
    var stompClient = Stomp.over(socket); // Sử dụng STOMP protocol

    // Lắng nghe tin nhắn từ khách hàng
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (messageOutput) {
            var message = JSON.parse(messageOutput.body);
            $scope.messages.push(message); // Thêm tin nhắn vào danh sách
            updateUniqueSenders(); // Cập nhật lại danh sách người gửi duy nhất
            $scope.$apply(); // Cập nhật view
        });
    });

    // Khởi tạo danh sách tin nhắn
    $scope.messages = [];

    // Danh sách người gửi duy nhất
    $scope.uniqueSenders = [];

    // Danh sách tin nhắn đã chọn theo người gửi
    $scope.selectedMessages = [];

    // Cập nhật danh sách người gửi duy nhất
    function updateUniqueSenders() {
        $scope.uniqueSenders = [];
        var seen = new Set();
        $scope.messages.forEach(function(message) {
            if (!seen.has(message.senderId)) {
                seen.add(message.senderId);
                $scope.uniqueSenders.push({
                    id: message.senderId,
                    senderType: message.senderType
                });
            }
        });
    }

    // Chọn người gửi và hiển thị tin nhắn của họ (bao gồm phản hồi)
    $scope.selectSender = function (senderId) {
        // Lọc tin nhắn chỉ của người gửi này và phản hồi từ quản trị viên
        $scope.selectedMessages = $scope.messages.filter(function (message) {
            return message.senderId === senderId || message.senderId === 1; // 1 là ID của quản trị viên
        });
    };

    // Gửi tin nhắn trả lời
    $scope.sendResponse = function () {
        if ($scope.responseContent.trim() === "") {
            alert("Bạn cần nhập nội dung tin nhắn.");
            return;
        }

        var responseMessage = {
            senderId: 1, // ID của nhân viên quản trị
            senderType: "Nhân viên hỗ trợ", // Loại người gửi là "nhan_vien"
            content: $scope.responseContent, // Nội dung tin nhắn trả lời
            timestamp: new Date().toISOString() // Thời gian gửi tin nhắn
        };

        stompClient.send("/app/sendMessage", {}, JSON.stringify(responseMessage)); // Gửi tin nhắn trả lời lên server
        $scope.responseContent = ""; // Làm trống ô nhập tin nhắn
    };
});


