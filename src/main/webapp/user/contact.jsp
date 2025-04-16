<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Liên hệ</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/header-footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:ital,opsz,wght@0,6..12,200..1000;1,6..12,200..1000&family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">
</head>
<style>
    body {
        margin: 0;
        font-family: Arial, sans-serif;
        background-image: url('<c:url value="/assets/img/background-login.png" />');
        background-size: cover;
        background-position: center;
        height: 100vh;
        display: flex;
        flex-direction: column;
    }

    .main {
        flex: 1;
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 40px 0;
    }

    .notification {
        position: fixed;
        top: 150px;
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
        right: 20px;
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
        font-size: 18px;
    }

    .contact-wrapper label {
        font-weight: 600;
        color: #333;
        display: block;
        margin-bottom: 5px;
    }

    .contact-wrapper input[type="text"], input[type="email"], textarea {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        margin-bottom: 15px;
        font-size: 14px;
        background-color: #fff;
    }

    .contact-wrapper textarea {
        resize: vertical;
    }

    .contact-wrapper button[type="submit"] {
        width: 100%;
        padding: 10px;
        background-color: #ffe31a;
        color: #333;
        font-size: 16px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        transition: background-color 0.3s ease;
        font-weight: 600;
    }

    .contact-wrapper button[type="submit"]:hover {
        background-color:  #ffe31a;
    }

    #resultMessage {
        margin-top: 10px;
        font-weight: bold;
        text-align: center;
    }
</style>

<body>
<div class="wrapper">
    <jsp:include page="../reuse/header.jsp" />
    <div class="main">
        <div class="contact-wrapper">
            <h2>Liên hệ với chúng tôi</h2>
            <form id="contactForm">
                <label for="contactName">Họ tên:</label>
                <input type="text" id="contactName" name="name" required>

                <label for="contactEmail">Email:</label>
                <input type="email" id="contactEmail" name="email" required>

                <label for="contactSubject">Tiêu đề:</label>
                <input type="text" id="contactSubject" name="subject" required>

                <label for="contactMessage">Nội dung:</label>
                <textarea id="contactMessage" name="message" rows="5" required></textarea>

                <button type="submit">Gửi</button>
            </form>

            <div class="notification" id="resultMessage"></div>
        </div>
    </div>
</div>
<jsp:include page="../reuse/footer.jsp" />

<script>
    // Lắng nghe sự kiện submit trên form với ID là 'contactForm'
    const form = document.getElementById("contactForm");
    const submitButton = form.querySelector("button[type='submit']");
    const result = document.getElementById("resultMessage");

    form.addEventListener("submit", function(event) {
        event.preventDefault();
        submitButton.disabled = true;

        // Hiển thị thông báo gửi dữ liệu
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
