<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/15/2024
  Time: 10:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="./img/logo-fold.png" sizes="180x180">
    <title>Đăng Kí</title>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css" />
<%--    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/register.css?v=2">--%>
<%--    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">--%>
    <style>
        :root {
            --color-black: #171C24;
            --color-white: #fff;
            --color-yellow: #ffe31a;
            --color-yellow-dark: #ffd900;
            --color-gray-light: #e9e9e9;
            --color-gray-dark: #ddd;
            --color-gray-mid: #555;
            --color-gray-darkest: #333;
        }

        /* Body và Wrapper */
        body {
            min-height: 100vh;
            font-weight: 400;
            font-size: 16px;
            line-height: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            background-image: url("https://images.unsplash.com/photo-1503455637927-730bce8583c0?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
            height: 100vh;
        }

        *,
        *:before,
        *:after {
            box-sizing: border-box;
        }
        * {
            margin: 0;
            padding: 0;
            font-family: "Poppins", sans-serif;
            font-size: 14px;
        }

        img,
        picture,
        svg,
        video {
            display: block;
            max-width: 100%;
        }
        input,
        select,
        textarea {
            background-color: transparent;
            outline: none;
        }
        button {
            cursor: pointer;
            background-color: transparent;
            outline: none;
            border: 0;
        }


        ul li {
            list-style: none;
        }

        hr {
            border: 0;
            height: 1px;
            background-color: #cfcfcf;
            color: #cfcfcf;
        }

        .container {
            max-width: 1240px;
            margin: 0 auto;
        }

        /* Input chung */
        select, input[type="text"], input[type="email"], input[type="password"] {
            width: 100%;
            padding: 8px;
            outline: none;
            border-radius: 4px;
            font-size: 14px;
            background-color: #fff;
        }

        /* Placeholder */
        input[type="text"]::placeholder {
            color: #9b9b9b;
        }

        a {
            text-decoration: none;
            color: #4288e5 !important;
        }

        a:hover {
            text-decoration: underline;
        }

        /* Form chung */
        .form-main {
            margin-top: 30px;
        }

        .form-input {
            height: 40px;
            padding: 0 20px;
            border-radius: 4px;
            transition: 0.25s ease;
            border: 1px solid #ccc;
            outline: none;
        }

        .form-input:focus {
            border: 1px solid #6a5af9;
            background-color: #fff !important;
            box-shadow: 0 0 8px rgba(106, 90, 249, 0.6); /* Hiệu ứng viền mờ */
        }

        /* Field và Label */
        .form-field {
            width: 100%;
            position: relative;
        }

        .form-label {
            position: absolute;
            top: 50%;
            left: 21px;
            transform: translateY(-50%);
            color: #999;
            pointer-events: none;
            user-select: none;
            font-size: 13px;
            transition: 0.25s ease;
        }

        .form-input:not(:placeholder-shown) + .form-label,
        .form-input:focus + .form-label {
            top: 0;
            left: 11px;
            padding: 0 10px;
            background-color: #fff;
            color: #6a5af9;
            font-size: 12px;
        }

        .wrapper {
            width: 420px;
            padding: 20px;
            background-color: #fff;
            border-radius: 4px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        /* Header và Main */
        .form-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .form-header .logo {
            display: flex;
            justify-content: center;
            align-items: center;
            text-decoration: none;
            color: black;
            font-weight: 600;
        }

        .form-header .logo p {
            margin-left: -15px;
            font-size: 24px;
        }

        .form-header p {
            font-weight: 600;
            font-size: 18px;
        }

        .form-group {
            margin-bottom: 35px;
        }

        .form-group p {
            margin-bottom: 10px;
            color: #333;
            font-weight: 600;
        }

        /* Footer */
        .form-footer {
            margin-top: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        button[type="submit"],
        [type="button"],
        .wrapper .form-footer button {
            padding: 10px 20px;
            background-color: #4288e5;
            color: white;
            border-radius: 4px;
        }

        /* Forgot Password */
        .forgot-pw {
            font-size: 15px;
            color: #6a5af9;
            text-decoration: none;
            cursor: pointer;
            margin-top: 10px;
        }

        .forgot-pw:hover {
            text-decoration: underline;
        }

        .forgot-container {
            width: 380px;
            margin-bottom: 10px;
            border: 1px solid #c0bfbf;
            border-radius: 8px;
            border: none;
        }

        .forgot-container>p {
            margin-bottom: 40px;
            font-size: 20px;
            text-align: center;
            font-weight: 600;
        }

        .login_signup {
            margin-top: 15px;
            text-align: center;
        }

        .group-btn {
            margin-top: 20px;
        }

        .group-btn p {
            margin-top: 15px;
            font-size: 14px;
            cursor: pointer;
        }

        .group-btn button {
            width: 100%;
        }

        .input-group label {
            margin-bottom: 5px;
            display: block;
        }

        /* Thiết kế hộp thông báo */
        .notification {
            position: fixed;
            top: 20px;
            left: -300px;
            width: 250px;
            padding: 15px;
            background-color: #28a745;
            color: #fff;
            font-size: 16px;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            transition: left 0.5s ease-in-out;
            z-index: 1000;
        }

        /* Hiển thị thông báo */
        .notification.show {
            left: 20px;
        }

        .error-message {
            color: #ff4d4f; /* Màu đỏ nổi bật */
            font-size: 12px;
            font-weight: 600;
            margin-top: 5px;
        }

        .password-hint {
            font-size: 12px;
            color: #888;
            margin-top: 5px;
        }
    </style>
</head>
<body>
<div class="wrapper">
    <form id="signup-form" action="${pageContext.request.contextPath}/register" method="post" novalidate>
        <div class="form-header">
            <a href="/WebBongDen_war/home" class="logo"
            ><img src="./assets/img/logo-login.png" alt="Logo"></a>
            <p>ĐĂNG KÍ</p>
        </div>
        <div class="form-main">
            <div class="form-group">
                <p>TÊN KHÁCH HÀNG</p>
                <div class="form-field">
                    <input type="text" class="form-input" id="cusname" name="cusname" placeholder=" " required />
                    <label for="cusname" class="form-label">Tên khách hàng</label>
                </div>
                <span class="error-message"></span>
            </div>

            <div class="form-group">
                <p>TÀI KHOẢN</p>
                <div class="form-field">
                    <input type="text" class="form-input" id="username" name="username" placeholder=" " required />
                    <label for="username" class="form-label">Tài khoản</label>
                </div>
                <span class="error-message"></span>
            </div>

            <div class="form-group">
                <p>EMAIL</p>
                <div class="form-field">
                    <input type="email" class="form-input" id="email" name="email" placeholder=" " required />
                    <label for="email" class="form-label">Email</label>
                </div>
                <span class="error-message"></span>
            </div>

            <div class="form-group">
                <p>MẬT KHẨU</p>
                <div class="form-field">
                    <input type="password" class="form-input" id="password" name="password" placeholder=" " required />
                    <label for="password" class="form-label">Mật khẩu</label>
                </div>
                <div id="password-hint" class="password-hint">Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ in hoa, số và ký tự đặc biệt.</div>
                <span class="error-message"></span>
            </div>

            <div class="form-group">
                <p>NHẬP LẠI MẬT KHẨU</p>
                <div class="form-field">
                    <input type="password" class="form-input" id="rePassword" name="rePassword" placeholder=" " required />
                    <label for="rePassword" class="form-label">Nhập lại mật khẩu</label>
                </div>
                <span class="error-message"></span>
            </div>
        </div>
        <div class="form-footer">
            <label>
                <input type="checkbox" id="terms-checkbox" required />
                Tôi đã đọc rõ điều khoản
            </label>
            <button type="submit" class="btn">Đăng kí</button>
        </div>
    </form>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> <!-- Thêm jQuery -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const form = document.getElementById("signup-form");
        const inputs = form.querySelectorAll(".form-input");

        function validateInput(input) {
            const value = input.value.trim();
            const formGroup = input.closest(".form-group");
            const errorMessage = formGroup.querySelector(".error-message");

            if (value === "") {
                input.style.borderColor = "red";
                errorMessage.textContent = "Trường này không được để trống.";
                return false;
            }

            if (input.type === "email") {
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(value)) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Email không hợp lệ.";
                    return false;
                }
            }

            if (input.type === "password") {
                if (value.length < 8) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Mật khẩu phải có ít nhất 8 ký tự.";
                    return false;
                }
                if (!/[A-Z]/.test(value)) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Mật khẩu phải có ít nhất 1 chữ cái viết hoa.";
                    return false;
                }
                if (!/[a-z]/.test(value)) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Mật khẩu phải có ít nhất 1 chữ cái viết thường.";
                    return false;
                }
                if (!/\d/.test(value)) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Mật khẩu phải có ít nhất 1 chữ số.";
                    return false;
                }
                if (!/[!@#$%^&*(),.?":{}|<>]/.test(value)) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Mật khẩu phải có ít nhất 1 ký tự đặc biệt.";
                    return false;
                }
            }

            input.style.borderColor = "green";
            errorMessage.textContent = "";
            return true;
        }

        inputs.forEach(input => {
            input.addEventListener("blur", function () {
                validateInput(input);
            });

            input.addEventListener("input", function () {
                validateInput(input);
            });
        });

        form.addEventListener("submit", function (e) {
            e.preventDefault()
            let isValid = true;

            inputs.forEach(input => {
                if (!validateInput(input)) {
                    isValid = false;
                }
            });

            // Kiểm tra nhập lại mật khẩu
            const password = document.getElementById("password").value.trim();
            const rePassword = document.getElementById("rePassword").value.trim();
            const rePasswordError = document.getElementById("rePassword").closest(".form-group").querySelector(".error-message");

            if (password !== rePassword) {
                isValid = false;
                rePasswordError.textContent = "Mật khẩu nhập lại không khớp.";
                document.getElementById("rePassword").style.borderColor = "red";
            } else {
                rePasswordError.textContent = "";
                document.getElementById("rePassword").style.borderColor = "green";
            }

            // Nếu có lỗi, ngăn form submit đến Servlet
            if (!isValid) {
                e.preventDefault();
                return;
            }

            // Gửi dữ liệu đến Servlet
            const formData = new FormData(form);
            const jsonData = {};
            formData.forEach((value, key) => {
                jsonData[key] = value;
            });

            fetch(form.action, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(jsonData),
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error("Đã xảy ra lỗi khi đăng ký. Vui lòng thử lại!");
                    }
                    return response.json();
                })
                .then((data) => {
                    if (data.success) {
                        toastr.success("Đăng ký thành công!");
                        setTimeout(() => {
                            window.location.href = "${pageContext.request.contextPath}/login";
                        }, 2000);
                    } else {
                        toastr.error(data.message || "Đăng ký thất bại. Vui lòng thử lại!");
                    }
                })
                .catch((error) => {
                    console.error("Error:", error);
                    toastr.error("Lỗi khi kết nối đến server. Vui lòng thử lại!");
                });
        });
    });


    <!-- Ngăn copy pass -->
        document.addEventListener("DOMContentLoaded", function () {
        const passwordInput = document.getElementById("password");
        const rePasswordInput = document.getElementById("rePassword");

        function preventCopyPaste(input) {
        input.addEventListener("paste", function (e) {
        e.preventDefault();
    });
        input.addEventListener("copy", function (e) {
        e.preventDefault();
    });
        input.addEventListener("cut", function (e) {
        e.preventDefault();
    });
        input.addEventListener("contextmenu", function (e) {
        e.preventDefault();
    });
    }

        preventCopyPaste(passwordInput);
        preventCopyPaste(rePasswordInput);
    });

</script>
</body>
</html>

