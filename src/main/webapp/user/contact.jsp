<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Liên hệ</title>
</head>
<body>
<h2>Liên hệ với chúng tôi</h2>
<form id="contactForm">
    <label>Họ tên:</label><br>
    <input type="text" name="name" required><br><br>

    <label>Email:</label><br>
    <input type="email" name="email" required><br><br>

    <label>Tiêu đề:</label><br>
    <input type="text" name="subject" required><br><br>

    <label>Nội dung:</label><br>
    <textarea name="message" rows="5" required></textarea><br><br>

    <button type="submit">Gửi</button>
</form>

<div id="resultMessage" style="margin-top: 10px; font-weight: bold;"></div>
<script>
    const form = document.getElementById("contactForm");
    const submitButton = form.querySelector("button[type='submit']");
    const result = document.getElementById("resultMessage");

    form.addEventListener("submit", function(event) {
        event.preventDefault();

        // ✅ Disable nút gửi
        submitButton.disabled = true;
        result.textContent = "⏳ Đang gửi liên hệ...";
        result.style.color = "blue";

        const formData = new FormData(form);

        fetch("ContactController", {
            method: "POST",
            body: formData
        })
            .then(response => response.text())
            .then(data => {
                result.textContent = data;

                if (data.includes("✅")) {
                    result.style.color = "green";
                    form.reset(); // Reset form sau khi gửi thành công
                } else {
                    result.style.color = "red";
                }
            })
            .catch(error => {
                result.textContent = "❌ Có lỗi xảy ra khi gửi liên hệ!";
                result.style.color = "red";
                console.error("Error:", error);
            })
            .finally(() => {
                // ✅ Enable lại nút gửi sau khi xong
                submitButton.disabled = false;
            });
    });
</script>


</body>
</html>
