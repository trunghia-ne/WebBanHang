document.addEventListener("DOMContentLoaded", function () {
    const forgotPasswordLink = document.querySelector(".forgot-pw");
    const forgotPasswordForm = document.getElementById("forgot-password-form");
    const otpForm = document.getElementById("otp-form");
    const newPasswordForm = document.getElementById("new-password-form");
    const loginForm = document.getElementById("login-form");
    const backToLogin = document.getElementById("back-to-login");
    const backToEmail = document.getElementById("back-to-email");
    const backToOtp = document.getElementById("back-to-otp");
    const passwordInput = document.getElementById("cus-password");

    passwordInput.addEventListener("paste", function (e) {
        e.preventDefault();
    });

    passwordInput.addEventListener("copy", function (e) {
        e.preventDefault();
    });

    passwordInput.addEventListener("cut", function (e) {
        e.preventDefault();
    });

    passwordInput.addEventListener("contextmenu", function (e) {
        e.preventDefault();
    });

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

//Login ajax & Captcha
document.addEventListener("DOMContentLoaded", function () {
    let username = document.getElementById("cus-username");
    let password = document.getElementById("cus-password");
    let errorUsername = document.getElementById("error-username");
    let errorPassword = document.getElementById("error-password");
    let errorCaptcha = document.getElementById("error-captcha");

    function validateInput(input, errorElement) {
        if (input.value.trim() !== "") {
            errorElement.textContent = "";
            input.style.border = "1px solid green"; // Xanh khi hợp lệ
        } else {
            errorElement.textContent = `Vui lòng nhập ${input.getAttribute("placeholder").toLowerCase()}.`;
            input.style.border = "1px solid red"; // Đỏ khi trống
        }
    }

    username.addEventListener("input", function () {
        validateInput(username, errorUsername);
    });

    password.addEventListener("input", function () {
        validateInput(password, errorPassword);
    });

    // Lắng nghe sự kiện khi CAPTCHA được xác thực
    window.onCaptchaSuccess = function () {
        errorCaptcha.textContent = ""; // Xóa lỗi nếu CAPTCHA hợp lệ
    };

    document.getElementById("login-form").addEventListener("submit", function (event) {
        event.preventDefault(); // Chặn submit mặc định

        let isValid = true;

        if (!username.value.trim()) {
            errorUsername.textContent = "Vui lòng nhập tài khoản.";
            username.style.border = "1px solid red";
            isValid = false;
        }

        if (!password.value.trim()) {
            errorPassword.textContent = "Vui lòng nhập mật khẩu.";
            password.style.border = "1px solid red";
            isValid = false;
        }

        // Kiểm tra reCAPTCHA
        let captchaResponse = grecaptcha.getResponse();
        if (!captchaResponse) {
            errorCaptcha.textContent = "Vui lòng xác nhận CAPTCHA.";
            isValid = false;
        } else {
            errorCaptcha.textContent = "";
        }

        if (!isValid) return;

        // Lấy nút và các phần tử con
        const loginBtn = document.getElementById("login-btn");
        const btnText = loginBtn.querySelector(".btn-text");
        const spinner = loginBtn.querySelector(".spinner");

        // Hiện spinner, ẩn chữ đăng nhập
        btnText.style.display = "none";
        spinner.style.display = "inline-block";

        let formData = {
            username: username.value,
            password: password.value,
            captcha: captchaResponse
        };

        fetch("/WebBongDen_war/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(formData)
        })
            .then(response => {
                if (!response.ok) {
                    // Ghi log nội dung HTML lỗi để dễ debug
                    return response.text().then(text => {
                        console.error("Server error HTML:", text);
                        throw new Error("Server error " + response.status);
                    });
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    if (data.role === "admin") {
                        window.location.href = "/WebBongDen_war/admin?page=dashboard-management";
                    } else {
                        window.location.href = "/WebBongDen_war/home";
                    }
                } else {
                    document.getElementById("error-message").textContent = data.message;
                    document.getElementById("error-message").style.display = "block";
                    grecaptcha.reset(); // Reset CAPTCHA nếu lỗi
                }

                btnText.style.display = "inline";
                spinner.style.display = "none";
            })
            .catch(error => {
                console.error("Lỗi:", error.message);
            });

    });
});