$(document).ready(function () {
    const $notiList = $('.noti-list');
    const $badge = $('.badge');
    const $notiDropdown = $('#notiDropdown');

    // ✅ Kết nối WebSocket
    const socket = new WebSocket("ws://webbongden.khacthienit.click/admin/socket-notification");

    socket.onopen = function () {
        console.log("✅ WebSocket connected");
    };

    socket.onmessage = function (event) {
        const data = JSON.parse(event.data);
        appendNotification(data.message, data.link, data.createdAt);
    };

    //Hàm hiển thị 1 thông báo
    function appendNotification(msg, link, createdAt) {
        const timeFormatted = formatDateTime(createdAt);

        const newNoti = `
        <a href="${link}" class="noti-link" target="_target">
            <div class="noti-item">
                <div class="noti-dot"></div>
                <div class="noti-content">
                    <p class="noti-title">Hệ thống • <span>${timeFormatted}</span></p>
                    <p class="noti-text">${msg}</p>
                </div>
            </div>
        </a>
    `;
        $('.noti-list').prepend(newNoti);
    }

    // Gọi API lấy danh sách thông báo từ DB
    $.ajax({
        url: "/WebBongDen_war/admin/load-notifications",
        method: "GET",
        dataType: "json",
        success: function (data) {
            if (data.length > 0) $('.badge').css('display', 'inline-block');

            $.each(data.reverse(), function (i, noti) {
                appendNotification(noti.message, noti.link, noti.createdAt);
            });
        },
        error: function (xhr, status, error) {
            console.error("Lỗi khi tải thông báo:", error);
        }
    });

    // Toggle dropdown
    $('#notiBtn').on('click', function () {
        $notiDropdown.toggleClass('show');
        $badge.hide();
    });

    function formatDateTime(createdAt) {
        const date = new Date(createdAt);
        const time = date.toLocaleTimeString("vi-VN", {
            hour: '2-digit',
            minute: '2-digit'
        });

        const dateStr = date.toLocaleDateString("vi-VN"); // ví dụ: "26/05/2025"

        return `${time} • ${dateStr}`;
    }
});