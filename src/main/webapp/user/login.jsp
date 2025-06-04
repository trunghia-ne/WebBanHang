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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css?v=2" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
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
            <a href="/home" class="logo"
            ><img src="./assets/img/logo-login.png" alt="Logo"></a>
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
                <span class="error-message" id="error-username"></span>
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
                <span class="error-message" id="error-password"></span>
            </div>
            <p id="error-message" style="color: red; display: none;"></p>

            <!-- CAPTCHA -->
            <div class="form-group">
                <div class="g-recaptcha" data-sitekey="6LehGvYqAAAAAGyyRncgehEOw351OwCJK4fAoLL0" data-callback="onCaptchaSuccess"></div>
                <span class="error-message" id="error-captcha"></span>
            </div>


            <div class="form-footer">
                <label>
                    <div class="option_field">
                        <a href="#" class="forgot-pw"
                        >Quên mật khẩu?</a
                        >
                    </div>
                </label>
                <button type="submit" id="login-btn" class="btn">
                    <span class="btn-text">Đăng nhập</span>
                    <span class="spinner" style="display:none;"></span>
                </button>
            </div>

            <div style="width: 100%; background-color: #eee; height: 1px; border-radius: 4px; margin-top: 35px;" ></div>
            <div class="login-by-platform">
                <p>ĐĂNG NHẬP BẰNG ỨNG DỤNG KHÁC </p>
                <div class="platfrom-container">
                    <div class="platform" id="login-facebook">
                        <img src="<%= request.getContextPath() %>/assets/img/imagesWeb/facebook.png" alt="Facebook Login">
                        <span>Facebook</span>
                    </div>

                    <div class="platform" id="login-google">
                        <a class="google-link" href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&response_type=code&client_id=625158935097-jbd22lba0t05tm01j6m2bc4n801ncnfj.apps.googleusercontent.com&redirect_uri=https://webbongden.khacthienit.click/WebBongDen_war/login-google&access_type=offline&approval_prompt=force">
                            <img src="<%= request.getContextPath() %>/assets/img/imagesWeb/google.png" alt="Facebook Login">
                            <span>Google</span>
                        </a>
                    </div>
                </div>
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
<script src="${pageContext.request.contextPath}/assets/Js/login.js?v=${System.currentTimeMillis()}"></script>
</html>
