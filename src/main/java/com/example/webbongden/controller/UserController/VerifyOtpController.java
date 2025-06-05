package com.example.webbongden.controller.UserController;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@WebServlet(name = "VerifyOtpController", value = "/verify-otp")
public class VerifyOtpController extends HttpServlet {

    private static final int MAX_OTP_ATTEMPTS = 5;
    private static final long BLOCK_DURATION_MS = TimeUnit.MINUTES.toMillis(1); // 1 phút

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Kiểm tra xem người dùng có đang bị khóa hay không
        Long blockTimestamp = (Long) session.getAttribute("otpBlockTime");
        if (blockTimestamp != null) {
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - blockTimestamp;

            if (timeElapsed < BLOCK_DURATION_MS) {
                long remainingTime = BLOCK_DURATION_MS - timeElapsed;
                long remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(remainingTime);
                sendJsonResponse(response, false, "Bạn đã nhập sai quá nhiều lần. Vui lòng thử lại sau " + remainingSeconds + " giây.");
                return;
            } else {
                session.removeAttribute("otpAttemptCount");
                session.removeAttribute("otpBlockTime");
            }
        }

        String userOtp = request.getParameter("otp");
        String storedOtp = (String) session.getAttribute("otp");

        if (userOtp != null && userOtp.equals(storedOtp)) {
            // Xác thực thành công
            session.removeAttribute("otpAttemptCount");
            session.removeAttribute("otpBlockTime");
            sendJsonResponse(response, true, "OTP hợp lệ. Vui lòng nhập mật khẩu mới.");
        } else {
            // Xác thực thất bại
            Integer otpAttemptCount = (Integer) session.getAttribute("otpAttemptCount");
            if (otpAttemptCount == null) {
                otpAttemptCount = 0;
            }
            otpAttemptCount++;
            session.setAttribute("otpAttemptCount", otpAttemptCount);

            if (otpAttemptCount >= MAX_OTP_ATTEMPTS) {
                session.setAttribute("otpBlockTime", System.currentTimeMillis());
                sendJsonResponse(response, false, "Bạn đã nhập sai OTP 5 lần. Tài khoản bị tạm khóa trong 1 phút.");
            } else {
                int remainingAttempts = MAX_OTP_ATTEMPTS - otpAttemptCount;
                sendJsonResponse(response, false, "OTP không hợp lệ. Bạn còn " + remainingAttempts + " lần thử.");
            }
        }
    }

    /**
     * Helper method để gửi phản hồi dạng JSON
     */
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) throws IOException {
        PrintWriter out = response.getWriter();
        // Tạo chuỗi JSON thủ công
        String json = "{\"success\": " + success + ", \"message\": \"" + message + "\"}";
        out.print(json);
        out.flush();
    }
}