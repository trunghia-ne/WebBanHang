<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Liên hệ</title>
</head>
<style>
    body {
        display: flex;
        justify-content: center;
        align-items: center;
        background-image: url('<c:url value="/assets/img/background-login.png" />');
        background-size: cover;
        background-position: center;
        height: 100vh;
        margin: 0;
        font-family: Arial, sans-serif;
    }

    .notification {
        position: fixed;
        top: 20px;
        right: -300px; /* ban đầu ẩn ra ngoài bên phải */
        width: 250px;
        padding: 15px;
        background-color: #28a745; /* màu xanh lá thành công */
        color: #fff;
        font-size: 16px;
        border-radius: 5px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        transition: right 0.5s ease-in-out;
        z-index: 1000;
    }

    .notification.show {
        right: 20px; /* khi có class .show sẽ trượt vào */
    }

    .contact-wrapper {
        width: 450px;
        background-color: #fff;
        padding: 30px;
        border-radius: 6px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
    }

    .contact-wrapper h2 {
        text-align: center;
        margin-bottom: 20px;
        color: #333;
    }

    label {
        font-weight: 600;
        color: #333;
        display: block;
        margin-bottom: 5px;
    }

    input[type="text"], input[type="email"], textarea {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        margin-bottom: 15px;
        font-size: 14px;
        background-color: #fff;
    }

    textarea {
        resize: vertical;
    }

    button[type="submit"] {
        width: 100%;
        padding: 10px;
        background-color: #4288e5;
        color: white;
        font-size: 16px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    button[type="submit"]:hover {
        background-color: #306fd0;
    }

    #resultMessage {
        margin-top: 10px;
        font-weight: bold;
        text-align: center;
    }
</style>

<body>
<div class="contact-wrapper">
    <h2>Liên hệ với chúng tôi</h2>
    <form id="contactForm">
        <label>Họ tên:</label>
        <input type="text" name="name" required>

        <label>Email:</label>
        <input type="email" name="email" required>

        <label>Tiêu đề:</label>
        <input type="text" name="subject" required>

        <label>Nội dung:</label>
        <textarea name="message" rows="5" required></textarea>

        <button type="submit">Gửi</button>
    </form>

    <div class="notification" id="resultMessage"></div>
</div>

<script>
    const form = document.getElementById("contactForm");
    const submitButton = form.querySelector("button[type='submit']");
    const result = document.getElementById("resultMessage");

    form.addEventListener("submit", function(event) {
        event.preventDefault();
        submitButton.disabled = true;

        // Thêm class show để hiện thông báo
        result.textContent = "⏳ Đang gửi liên hệ...";
        result.className = "notification show";
        result.style.backgroundColor = "#4288e5";

        const formData = new FormData(form);

        fetch("ContactController", {
            method: "POST",
            body: formData
        })
            .then(response => response.text())
            .then(data => {
                result.textContent = data;
                if (data.includes("✅")) {
                    result.style.backgroundColor = "#28a745";
                    form.reset();
                } else {
                    result.style.backgroundColor = "#dc3545";
                }
            })
            .catch(error => {
                result.textContent = "❌ Có lỗi xảy ra khi gửi liên hệ!";
                result.style.backgroundColor = "#dc3545";
                console.error("Error:", error);
            })
            .finally(() => {
                submitButton.disabled = false;

                // Ẩn thông báo sau 4 giây
                setTimeout(() => {
                    result.classList.remove("show");
                }, 4000);
            });
    });

</script>


</body>
</html>
