document.addEventListener("DOMContentLoaded", () => {
    const notiList = document.querySelector('.noti-list');
    const badge = document.querySelector('.badge');
    const notiDropdown = document.getElementById('notiDropdown');

    // ✅ Kết nối WebSocket
    const socket = new WebSocket("ws://localhost:8080/WebBongDen_war/admin/socket-notification");

    socket.onopen = () => {
        console.log("✅ WebSocket connected");
    };

    socket.onmessage = function(event) {
        const data = JSON.parse(event.data);
        appendNotification(data.message, data.link);
    };

    // ✅ Hàm hiển thị 1 thông báo
    function appendNotification(msg,link) {
        const now = new Date();
        const time = now.toLocaleTimeString("vi-VN", { hour: '2-digit', minute: '2-digit' });

        const newNoti = `
        <a href="${link}" class="noti-link" target="_target">
            <div class="noti-item">
                <div class="noti-dot"></div>
                <div class="noti-content">
                    <p class="noti-title">Hệ thống • <span>${time}</span></p>
                    <p class="noti-text">${msg}</p>
                </div>
            </div>
        </a>
    `;
        notiList.insertAdjacentHTML("afterbegin", newNoti);
    }

    // ✅ Gọi API lấy danh sách thông báo từ DB
    fetch("/WebBongDen_war/admin/load-notifications")
        .then(res => res.json())
        .then(data => {
            if (data.length > 0) badge.style.display = "inline-block";

            data.reverse().forEach(noti => {
                appendNotification(noti.message, noti.link);
            });
        });

    // ✅ Toggle dropdown
    document.getElementById('notiBtn').addEventListener('click', () => {
        notiDropdown.classList.toggle('show');
        badge.style.display = 'none';
    });
});
