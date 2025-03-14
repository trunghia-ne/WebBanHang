<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/14/2024
  Time: 7:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="icon" href="./img/logo-fold.png" sizes="180x180">
    <title>Đăng nhập</title>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
            integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
            crossorigin="anonymous"
            referrerpolicy="no-referrer"
    />
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link
            href="https://fonts.googleapis.com/css2?family=Nunito+Sans:ital,opsz,wght@0,6..12,200..1000;1,6..12,200..1000&family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
            rel="stylesheet"
    />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/login.css?v=1.3">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">
</head>
<body>
<div class="wrapper">
    <!-- Hiển thị thông báo lỗi nếu có -->
    <c:if test="${not empty errorMessageReset}">
        <p id="responseMessage1" style="color: red;">
                ${errorMessageReset}
        </p>
    </c:if>

    <!-- Hiển thị thông báo thành công nếu có -->
    <c:if test="${not empty successMessageReset}">
        <p id="responseMessage2" style="color: green;">
                ${successMessageReset}
        </p>
    </c:if>
    <form id="login-form" action="login" method="post"
          style="${showOtpForm || showNewPasswordForm ? 'display: none;' : 'display: block;'}">
        <%--    <form id="login-form" action="login" method="post">--%>
        <div class="form-header">
            <a href="index.html" class="logo"
            ><img src="./img/logo-login.png" alt=""
            /></a>
            <p>ĐĂNG NHẬP</p>
        </div>
        <div class="form-main">
            <div class="form-group">
                <p>TÀI KHOẢN</p>
                <div class="form-field">
                    <input
                            type="text"
                            name="username"
                            class="form-input"
                            id="cus-username"
                            placeholder=" "
                    />
                    <label for="cus-username" class="form-label">Tài khoản</label>
                </div>
            </div>

            <div class="form-group">
                <p>MẬT KHẨU</p>
                <div class="form-field">
                    <input
                            type="password"
                            name="password"
                            class="form-input"
                            id="cus-password"
                            placeholder=" "
                    />
                    <label for="cus-password" class="form-label">Mật khẩu</label>
                </div>
            </div>
            <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null) {
            %>
            <div class="error-message" style="color: red; text-align: center; margin: 10px 0;">
                <%= errorMessage %>
            </div>
            <%
                }
            %>

            <div class="form-footer">
                <label>
                    <div class="option_field">
                        <a href="#" class="forgot-pw"
                        >Quên mật khẩu?</a
                        >
                    </div>
                </label>
                <button type="submit" class="btn">Đăng nhập</button>
            </div>

            <div style="width: 100%; background-color: #eee; height: 1px; border-radius: 4px; margin-top: 20px;" ></div>

            <div class="login-by-platform">
                <p>ĐĂNG NHẬP BẰNG ỨNG DỤNG KHÁC </p>
                <div class="platfrom-container">
                    <div class="platform" id="login-facebook">
                        <img src="<%= request.getContextPath() %>/assets/img/imagesWeb/facebook.png" alt="Facebook Login">
                        <span>Facebook</span>
                    </div>

                    <div class="platform" id="login-google">
                        <a class="google-link" href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&response_type=code&client_id=625158935097-jbd22lba0t05tm01j6m2bc4n801ncnfj.apps.googleusercontent.com&redirect_uri=http://localhost:8080/WebBongDen_war/login-google&access_type=offline&approval_prompt=force">
                            <img src="<%= request.getContextPath() %>/assets/img/imagesWeb/google.png" alt="Facebook Login">
                            <span>Google</span>
                        </a>
                    </div>
                </div>
            </div>

            <div style="width: 100%; background-color: #eee; height: 1px; border-radius: 4px; margin-top: 20px;" ></div>
            <div class="form-footer">
                <label>
                    <div class="option_field">
                        <a href="#" class="forgot-pw"
                        >Quên mật khẩu?</a
                        >
                    </div>
                </label>
                <button type="submit" class="btn">Đăng nhập</button>
            </div>
            <div class="login_signup">
                Bạn chưa có tài khoản?
                <a href="${pageContext.request.contextPath}/register" id="signup">Đăng ký ngay!</a>
            </div>
        </div>
    </form>
    <!-- Form quên mật khẩu (nhập email) -->
    <div id="forgot-password-form" class="forgot-container"
         style="${showOtpForm || showNewPasswordForm ? 'display: none;' : 'display: none;'}">
        <p>KHÔI PHỤC MẬT KHẨU</p>
        <form action="reset-password" id="reset-password-form" method="post">
            <div class="input-group">
                <label for="reset-email">Nhập email của bạn</label>
                <input
                        type="email"
                        id="reset-email"
                        name="reset-email"
                        required
                        placeholder="example@gmail.com"
                />
            </div>
            <div class="group-btn">
                <button type="submit">Gửi Yêu Cầu Đặt Lại Mật Khẩu</button>
                <p>
                    Bạn đã nhớ mật khẩu?
                    <span id="back-to-login" style="color: #4288e5">Quay lại trang đăng nhập</span>
                </p>
            </div>
        </form>
    </div>

    <!-- Form nhập OTP -->
    <div id="otp-form" class="otp-container"
         style="${showOtpForm && !showNewPasswordForm ? 'display: block;' : 'display: none;'}">
        <form action="verify-otp" id="verify-otp-form" method="post">
            <div class="input-group">
                <label for="otp">Nhập OTP</label>
                <input
                        type="text"
                        id="otp"
                        name="otp"
                        required
                        placeholder="Nhập OTP"
                />
            </div>
            <div class="group-btn">
                <button type="submit">Xác Nhận OTP</button>
                <p>
                    <span id="back-to-email" style="color: #4288e5">Quay lại nhập email</span>
                </p>
            </div>
        </form>
    </div>

    <!-- Form nhập mật khẩu mới -->
    <div id="new-password-form" class="new-password-container"
         style="${showNewPasswordForm ? 'display: block;' : 'display: none;'}">
        <form action="update-password" id="update-password-form" method="post">
            <div class="input-group">
                <label for="new-password">Mật khẩu mới</label>
                <input
                        type="password"
                        id="new-password"
                        name="new-password"
                        required
                        placeholder="Nhập mật khẩu mới"
                />
            </div>
            <div class="input-group">
                <label for="confirm-password">Xác nhận mật khẩu mới</label>
                <input
                        type="password"
                        id="confirm-password"
                        name="confirm-password"
                        required
                        placeholder="Xác nhận mật khẩu mới"
                />
            </div>
            <div class="group-btn">
                <button type="submit">Cập Nhật Mật Khẩu</button>
                <p>
                    <span id="back-to-otp" style="color: #4288e5">Quay lại nhập OTP</span>
                </p>
            </div>
        </form>
    </div>
</div>
</body>
<script src="https://accounts.google.com/gsi/client" async defer></script>
<script src="${pageContext.request.contextPath}/assets/Js/login.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const forgotPasswordLink = document.querySelector(".forgot-pw");
        const forgotPasswordForm = document.getElementById("forgot-password-form");
        const otpForm = document.getElementById("otp-form");
        const newPasswordForm = document.getElementById("new-password-form");
        const loginForm = document.getElementById("login-form");
        const backToLogin = document.getElementById("back-to-login");
        const backToEmail = document.getElementById("back-to-email");
        const backToOtp = document.getElementById("back-to-otp");

        // Hiển thị form quên mật khẩu và ẩn form đăng nhập
        forgotPasswordLink.addEventListener("click", function (e) {
            e.preventDefault();
            loginForm.style.display = "none";
            forgotPasswordForm.style.display = "block";
            otpForm.style.display = "none";
            newPasswordForm.style.display = "none";
        });

        // Quay lại form đăng nhập từ form quên mật khẩu
        backToLogin.addEventListener("click", function () {
            forgotPasswordForm.style.display = "none";
            otpForm.style.display = "none";
            newPasswordForm.style.display = "none";
            loginForm.style.display = "block";
        });

        // Quay lại form nhập email từ form nhập OTP
        backToEmail.addEventListener("click", function () {
            otpForm.style.display = "none";
            newPasswordForm.style.display = "none";
            forgotPasswordForm.style.display = "block";
            loginForm.style.display = "none";
        });

        // Quay lại form nhập OTP từ form nhập mật khẩu mới
        backToOtp.addEventListener("click", function () {
            newPasswordForm.style.display = "none";
            otpForm.style.display = "block";
            forgotPasswordForm.style.display = "none";
            loginForm.style.display = "none";
        });
        //     ================code đăng nhập qua facebook ================================
        window.fbAsyncInit = function() {
            FB.init({
                appId      : '1158509715937438', // Thay YOUR_APP_ID bằng App ID của bạn
                cookie     : true,
                xfbml      : true,
                version    : 'v18.0'
            });
            FB.AppEvents.logPageView();
        };

        (function(d, s, id){
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) {return;}
            js = d.createElement(s); js.id = id;
            js.src = "https://connect.facebook.net/en_US/sdk.js";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));

        document.getElementById("login-facebook").addEventListener("click", function() {
            FB.login(function(response) {
                if (response.authResponse) {
                    // Chuyển hướng đến Servlet để xử lý đăng nhập
                    window.location.href = "login-facebook?access_token=" + response.authResponse.accessToken;
                }
            }, {scope: 'public_profile,email'});
        });
    });
</script>
</html>
