<%-- register.jsp --%>
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
    <style>
        :root {
            --color-black: #17C24;
            --color-white: #fff;
            --color-yellow: #ffe31a;
            --color-yellow-dark: #ffd900;
            --color-gray-light: #e9e9e9;
            --color-gray-dark: #ddd;
            --color-gray-mid: #555;
            --color-gray-darkest: #333;
        }

        body {
            min-height: 100vh;
            font-weight: 400;
            font-size: 16px;
            line-height: 1.5; /* Cải thiện khả năng đọc */
            display: flex;
            justify-content: center;
            align-items: center;
            background-image: url("https://mixivivu.com/section-background.png");
            background-size: cover;
            background-position: center;
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

        hr { /* Style chung cho hr, sẽ được ghi đè bởi style cụ thể hơn cho OTP section */
            border: 0;
            height: 1px;
            background-color: #cfcfcf;
            color: #cfcfcf; /* Thuộc tính color không có tác dụng với hr theo cách này */
            margin: 20px 0;
        }

        .container {
            max-width: 1240px;
            margin: 0 auto;
        }

        /* Input chung - Áp dụng cho tất cả input text, email, password, number */
        /* Thuộc tính background-color: #fff; ở đây sẽ đảm bảo input có nền trắng */
        select, input[type="text"], input[type="email"], input[type="password"], input[type="number"] {
            width: 100%;
            padding: 8px 15px; /* Điều chỉnh padding cho cân đối hơn */
            outline: none;
            border-radius: 4px;
            font-size: 14px;
            background-color: var(--color-white); /* Nền trắng cho input */
            border: 1px solid var(--color-gray-dark); /* Viền mặc định cho input */
            color: var(--color-black); /* Màu chữ trong input */
        }

        /* Placeholder */
        input[type="text"]::placeholder { /* Và các type input khác nếu cần */
            color: #9b9b9b;
        }

        a {
            text-decoration: none;
            color: #4288e5 !important; /* Hạn chế dùng !important */
        }

        a:hover {
            text-decoration: underline;
        }

        /* Form chung */
        .form-main {
            margin-top: 30px;
        }

        .form-input { /* Class này được dùng cho các input trong form đăng ký và OTP */
            height: 44px; /* Đồng nhất chiều cao với select, input[type=...] ở trên nếu muốn */
            padding: 0 15px; /* Đồng nhất padding */
            border-radius: 5px; /* Đồng nhất bo góc */
            transition: border-color 0.25s ease, box-shadow 0.25s ease;
            border: 1px solid var(--color-gray-dark); /* Kế thừa từ trên hoặc định nghĩa lại */
            background-color: var(--color-white); /* Đảm bảo có nền */
            outline: none;
        }

        .form-input:focus {
            border-color: #6a5af9;
            background-color: var(--color-white) !important; /* !important có thể không cần thiết nếu specificity đúng */
            box-shadow: 0 0 0 3px rgba(106, 90, 249, 0.15);
        }

        /* Field và Label */
        .form-field {
            width: 100%;
            position: relative;
        }

        .form-label { /* Style label ban đầu */
            position: absolute;
            top: 50%;
            left: 16px; /* Tương ứng với padding của .form-input */
            transform: translateY(-50%);
            color: var(--color-gray-mid);
            pointer-events: none;
            user-select: none;
            font-size: 14px;
            background-color: var(--color-white); /* Nền cho label khi nổi lên */
            transition: top 0.2s ease, left 0.2s ease, font-size 0.2s ease, color 0.2s ease, padding 0.2s ease;
        }

        /* Hiệu ứng label nổi lên - ĐỊNH NGHĨA MỘT LẦN VÀ CHÍNH XÁC */
        .form-input:not(:placeholder-shown) + .form-label,
        .form-input:focus + .form-label {
            top: -8px; /* Điều chỉnh giá trị này để label nằm đẹp trên viền input */
            left: 12px;
            font-size: 11px;
            color: #6a5af9;
            padding: 0 4px; /* Padding để tạo khoảng trống cho nền của label */
            z-index: 1; /* Đảm bảo label nằm trên viền của input */
        }

        .wrapper {
            width: 420px;
            padding: 25px 30px;
            background-color: var(--color-white);
            border-radius: 8px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
        }

        /* Header và Main của form đăng ký */
        .form-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
        }

        .form-header .logo {
            display: flex;
            align-items: center;
            text-decoration: none;
            color: var(--color-black);
            font-weight: 600;
        }
        .form-header .logo img{
            margin-right: 10px;
        }
        .form-header .logo p {
            margin-left: -15px; /* Giữ lại nếu đây là cố ý cho layout logo của bạn */
            font-size: 24px;
        }

        .form-header > p { /* Tiêu đề "ĐĂNG KÍ" */
            font-weight: 600;
            font-size: 20px;
            color: var(--color-black);
        }

        .form-group > p {
            margin-bottom: 8px;
            color: var(--color-gray-darkest);
            font-weight: bold;
            font-size: 13px;
        }

        /* Footer của form đăng ký */
        .form-footer {
            margin-top: 25px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .form-footer label {
            font-size: 13px;
            color: var(--color-gray-darkest);
            display: flex;
            align-items: center;
        }
        .form-footer input[type="checkbox"] {
            margin-right: 8px;
            /* Xem xét việc style custom checkbox nếu muốn đồng bộ hơn */
        }

        /* Nút bấm chung ở footer form đăng ký */
        .wrapper .form-footer button#btnRegister { /* Áp dụng class .btn nếu có */
            padding: 10px 25px;
            background-color: #4288e5;
            color: var(--color-white);
            border-radius: 5px;
            font-weight: 500;
            font-size: 15px;
            transition: background-color 0.25s ease, box-shadow 0.25s ease;
        }
        .wrapper .form-footer button#btnRegister:hover {
            background-color: #3578ce;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }


        .error-message { /* Style chung cho tin nhắn lỗi */
            color: #ff4d4f;
            font-size: 12px;
            font-weight: 500;
            margin-top: 6px;
            display: block;
            min-height: 1.2em; /* Giữ không gian, tránh nhảy layout */
        }

        .password-hint {
            font-size: 12px;
            color: var(--color-gray-mid);
            margin-top: 6px;
        }

        /* === PHẦN CSS ĐÃ SỬA CHO OTP SECTION === */
        /* Loại bỏ định nghĩa #otp-section cũ, chỉ giữ lại phiên bản đã sửa này */
        #otp-section {
            margin-top: 35px;
            padding-top: 20px;
            padding-bottom: 25px;
            background-color: transparent; /* NỀN TRONG SUỐT */
            border: none;                  /* KHÔNG VIỀN */
            box-shadow: none;              /* KHÔNG BÓNG ĐỔ */
            text-align: center;
        }

        #otp-section hr { /* Đường kẻ phân cách trong OTP section */
            border: 0;
            height: 1px;
            background-color: var(--color-gray-light);
            margin: 20px auto 25px;
            width: 80%;
        }

        .otp-section-title { /* Tiêu đề "XÁC THỰC OTP" (dùng class cho <p> hoặc <h3>) */
            font-size: 20px;
            font-weight: 600;
            color: var(--color-black);
            margin-bottom: 15px;
        }

        #otp-message { /* Tin nhắn "OTP đã được gửi..." */
            font-size: 13px;
            color: var(--color-gray-mid);
            margin-bottom: 25px;
            line-height: 1.5;
        }

        #otp-section .form-group { /* Group chứa input OTP */
            margin-bottom: 20px; /* Khoảng cách dưới ô nhập OTP */
            text-align: left; /* Giữ label và input căn trái */
        }

        /* Input OTP - sử dụng class .form-input đã có và id #otp để style cụ thể hơn */
        #otp-section input#otp.form-input {
            height: 48px; /* Cao hơn cho dễ thao tác */
            font-size: 20px; /* Số OTP to rõ */
            text-align: center; /* Căn giữa số trong ô */
            letter-spacing: 4px; /* Khoảng cách giữa các chữ số */
            /* Nền và viền sẽ được kế thừa từ .form-input chung */
            /* Nếu .form-input không có background-color: var(--color-white); thì cần thêm ở đây */
        }

        /* Label cho input OTP - Hiệu ứng nổi lên đã được xử lý bởi rule chung */
        /* #otp-section input#otp.form-input + .form-label { */
        /* Bỏ trống hoặc điều chỉnh nếu rule chung không hoạt động như ý với input này */
        /* } */

        /* Tin nhắn lỗi cho OTP */
        #otp-section span#otp-error-message.error-message {
            text-align: left; /* Đảm bảo căn trái nếu .form-group căn trái */
        }

        /* Nút "Xác nhận OTP" */
        #otp-section button#btnVerifyOtp.btn-primary-otp { /* Thêm class .btn-primary-otp vào nút */
            width: 100%;
            padding: 12px 20px;
            font-size: 15px;
            font-weight: 500;
            background-color: var(--color-yellow);
            color: var(--color-black);
            border: none;
            border-radius: 5px;
            transition: background-color 0.25s ease, box-shadow 0.25s ease;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        #otp-section button#btnVerifyOtp.btn-primary-otp:hover {
            background-color: var(--color-yellow-dark);
            box-shadow: 0 4px 8px rgba(0,0,0,0.15);
        }

        #otp-section button#btnVerifyOtp.btn-primary-otp:disabled {
            background-color: var(--color-gray-light);
            color: var(--color-gray-mid);
            cursor: not-allowed;
            box-shadow: none;
        }

        /* Nút "Gửi lại OTP" (nếu có) */
        #otp-section button#btnResendOtp.btn-secondary-otp { /* Thêm class .btn-secondary-otp vào nút */
            width: 100%;
            padding: 10px 20px;
            font-size: 14px;
            font-weight: 500;
            background-color: transparent;
            color: var(--color-gray-darkest);
            border: 1px solid var(--color-gray-dark);
            border-radius: 5px;
            transition: background-color 0.25s ease, color 0.25s ease, border-color 0.25s ease;
            margin-top: 12px;
        }

        #otp-section button#btnResendOtp.btn-secondary-otp:hover {
            background-color: var(--color-gray-light);
            border-color: var(--color-gray-mid);
            color: var(--color-black);
        }

        /* === CLASS TIỆN ÍCH ĐỂ ẨN/HIỆN === */
        .hidden {
            display: none !important; /* Ưu tiên display: none để ẩn hoàn toàn */
        }

        /* Đoạn CSS #otp-section.hidden với opacity và transform đã bị loại bỏ để tránh xung đột với .hidden {display:none;} */
        /* Nếu bạn muốn hiệu ứng transition, cần thay đổi cách ẩn/hiện trong JavaScript và CSS cho .hidden */

    </style>
</head>
<body>
<div class="wrapper">
    <%-- Form đăng ký ban đầu --%>
    <form id="signup-form" action="${pageContext.request.contextPath}/register" method="post" novalidate>
        <div class="form-header">
            <a href="${pageContext.request.contextPath}/home" class="logo">
                <img src="${pageContext.request.contextPath}/assets/img/logo-login.png" alt="Logo">
            </a>
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
                <div id="password-hint" class="password-hint">Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, số và ký tự đặc biệt.</div>
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
            <button type="submit" class="btn" id="btnRegister">Đăng kí</button>
        </div>
    </form>
        <%-- Phần nhập OTP - ban đầu ẩn đi --%>
        <div id="otp-section" class="hidden">
            <%-- Đổi <p> này thành h2 hoặc h3 để có ngữ nghĩa tốt hơn và thêm class --%>
            <h3 class="otp-section-title">XÁC THỰC OTP</h3>
            <div id="otp-message" style="margin-bottom:10px; font-weight: normal; line-height: 1.6;">Một mã OTP đã được gửi đến email [email_cua_nguoi_dung]. Vui lòng nhập mã đó vào ô bên dưới.</div>
            <form id="otp-form" action="${pageContext.request.contextPath}/VerifyOtpRegisterServlet" method="post">
                <div class="form-group">
                    <%-- Bỏ <p> này nếu tiêu đề field không cần thiết, hoặc style riêng --%>
                    <%-- <p>NHẬP MÃ OTP</p> --%>
                    <div class="form-field">
                        <input type="number" class="form-input" id="otp" name="otp" placeholder=" " required />
                        <label for="otp" class="form-label">Mã OTP (6 chữ số)</label>
                    </div>
                    <span class="error-message" id="otp-error-message"></span>
                </div>
                <button type="submit" class="btn btn-primary-otp" id="btnVerifyOtp">Xác nhận OTP</button>
                <button type="button" class="btn btn-secondary-otp" id="btnResendOtp" style="display: none; margin-top: 10px;">Gửi lại OTP</button>
            </form>
        </div>

</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const signupForm = document.getElementById("signup-form");
        const otpSection = document.getElementById("otp-section");
        const otpForm = document.getElementById("otp-form");
        const inputs = signupForm.querySelectorAll(".form-input");
        const termsCheckbox = document.getElementById("terms-checkbox");

        function validateInput(input) {
            const value = input.value.trim();
            const formGroup = input.closest(".form-group");
            const errorMessage = formGroup.querySelector(".error-message");
            let isValid = true;

            if (value === "") {
                input.style.borderColor = "red";
                errorMessage.textContent = "Trường này không được để trống.";
                isValid = false;
            } else {
                // Xóa lỗi nếu người dùng bắt đầu nhập
                input.style.borderColor = "#ccc"; // Reset border
                errorMessage.textContent = "";
            }


            if (input.type === "email") {
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (value !== "" && !emailRegex.test(value)) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Email không hợp lệ.";
                    isValid = false;
                }
            }

            if (input.id === "password" && value !== "") { // Chỉ validate password nếu không rỗng
                if (value.length < 8) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Mật khẩu phải có ít nhất 8 ký tự.";
                    isValid = false;
                } else if (!/[A-Z]/.test(value)) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Mật khẩu phải có ít nhất 1 chữ cái viết hoa.";
                    isValid = false;
                } else if (!/[a-z]/.test(value)) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Mật khẩu phải có ít nhất 1 chữ cái viết thường.";
                    isValid = false;
                } else if (!/\d/.test(value)) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Mật khẩu phải có ít nhất 1 chữ số.";
                    isValid = false;
                } else if (!/[!@#$%^&*(),.?":{}|<>]/.test(value)) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Mật khẩu phải có ít nhất 1 ký tự đặc biệt.";
                    isValid = false;
                }
            }
            if (input.id === "rePassword" && value !== "") {
                const passwordValue = document.getElementById("password").value.trim();
                if (value !== passwordValue) {
                    input.style.borderColor = "red";
                    errorMessage.textContent = "Mật khẩu nhập lại không khớp.";
                    isValid = false;
                }
            }


            if (isValid && value !== "") {
                input.style.borderColor = "green";
                errorMessage.textContent = "";
            }
            return isValid;
        }

        inputs.forEach(input => {
            input.addEventListener("blur", function () {
                validateInput(this);
            });
            input.addEventListener("input", function () {
                // Xóa thông báo lỗi ngay khi người dùng nhập để UX tốt hơn
                const formGroup = this.closest(".form-group");
                const errorMessage = formGroup.querySelector(".error-message");
                this.style.borderColor = "#ccc"; // Reset border
                errorMessage.textContent = "";
                // Có thể gọi validateInput(this) ở đây nếu muốn validate real-time
            });
        });

        signupForm.addEventListener("submit", function (e) {
            e.preventDefault(); // Ngăn submit form truyền thống
            let isFormValid = true;

            inputs.forEach(input => {
                if (!validateInput(input)) {
                    isFormValid = false;
                }
            });

            // Kiểm tra checkbox điều khoản
            if (!termsCheckbox.checked) {
                toastr.error("Bạn phải đồng ý với điều khoản dịch vụ.");
                isFormValid = false;
            }

            if (!isFormValid) {
                toastr.error("Vui lòng kiểm tra lại các thông tin đã nhập.");
                return;
            }

            // Nếu form hợp lệ, gửi dữ liệu đến Servlet đăng ký
            const formData = new FormData(signupForm);
            const jsonData = {};
            formData.forEach((value, key) => {
                jsonData[key] = value;
            });

            // Vô hiệu hóa nút đăng ký để tránh click nhiều lần
            const btnRegister = document.getElementById('btnRegister');
            btnRegister.disabled = true;
            btnRegister.textContent = 'Đang xử lý...';


            fetch(signupForm.action, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(jsonData),
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        toastr.success(data.message || "Mã OTP đã được gửi. Vui lòng kiểm tra email.");
                        // Ẩn form đăng ký, hiển thị form OTP
                        signupForm.classList.add("hidden");
                        otpSection.classList.remove("hidden");
                        document.getElementById("otp-message").textContent = "Một mã OTP đã được gửi đến email " + jsonData.email + ". Vui lòng nhập mã đó vào ô bên dưới.";
                        // Focus vào ô nhập OTP
                        document.getElementById("otp").focus();
                    } else {
                        toastr.error(data.message || "Đăng ký thất bại. Vui lòng thử lại!");
                    }
                })
                .catch((error) => {
                    console.error("Error:", error);
                    toastr.error("Lỗi kết nối đến server. Vui lòng thử lại!");
                })
                .finally(() => {
                    // Kích hoạt lại nút đăng ký
                    btnRegister.disabled = false;
                    btnRegister.textContent = 'Đăng kí';
                });
        });


        otpForm.addEventListener("submit", function(e) {
            e.preventDefault();
            const otpInput = document.getElementById("otp");
            const otpValue = otpInput.value.trim();
            const otpErrorMessage = document.getElementById("otp-error-message");

            if (otpValue === "" || !/^\d{6}$/.test(otpValue)) {
                otpInput.style.borderColor = "red";
                otpErrorMessage.textContent = "Mã OTP phải là 6 chữ số.";
                toastr.error("Mã OTP không hợp lệ.");
                return;
            } else {
                otpInput.style.borderColor = "#ccc";
                otpErrorMessage.textContent = "";
            }

            const btnVerifyOtp = document.getElementById('btnVerifyOtp');
            btnVerifyOtp.disabled = true;
            btnVerifyOtp.textContent = 'Đang xác thực...';

            // Gửi OTP đến servlet xác thực
            const formData = new FormData(otpForm);
            // fetch(otpForm.action, { // action đã được set trong HTML
            //     method: "POST",
            //     body: formData // Gửi dưới dạng form-data vì servlet đang đọc bằng request.getParameter
            // })
            // Nếu muốn gửi JSON thì cần chỉnh servlet đọc JSON
            fetch(otpForm.action + "?otp=" + encodeURIComponent(otpValue) , { // Gửi OTP qua query parameter cho đơn giản
                method: "POST", // Giữ method POST theo servlet
                headers: {
                    "Accept": "application/json"
                }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        toastr.success(data.message || "Xác thực thành công! Đang chuyển hướng...");
                        setTimeout(() => {
                            window.location.href = data.redirectUrl || "${pageContext.request.contextPath}/login";
                        }, 2000);
                    } else {
                        toastr.error(data.message || "Xác thực OTP thất bại.");
                        otpInput.style.borderColor = "red";
                        otpErrorMessage.textContent = data.message || "Mã OTP không chính xác hoặc đã hết hạn.";
                    }
                })
                .catch(error => {
                    console.error("Error verifying OTP:", error);
                    toastr.error("Lỗi khi xác thực OTP. Vui lòng thử lại.");
                })
                .finally(() => {
                    btnVerifyOtp.disabled = false;
                    btnVerifyOtp.textContent = 'Xác nhận OTP';
                });
        });


        // Ngăn copy/paste mật khẩu (giữ nguyên)
        const passwordInput = document.getElementById("password");
        const rePasswordInput = document.getElementById("rePassword");
        function preventCopyPaste(input) {
            input.addEventListener("paste", e => e.preventDefault());
            input.addEventListener("copy", e => e.preventDefault());
            input.addEventListener("cut", e => e.preventDefault());
            input.addEventListener("contextmenu", e => e.preventDefault());
        }
        preventCopyPaste(passwordInput);
        preventCopyPaste(rePasswordInput);
    });
</script>
</body>
</html>