package com.example.webbongden.controller.UserController;

import com.example.webbongden.services.AccountServices;
import com.example.webbongden.services.AccountServices.RegistrationResult;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "VerifyOtpRegisterServlet", value = "/VerifyOtpRegisterServlet")
public class VerifyOtpRegisterServlet extends HttpServlet {

    private AccountServices accountServices;
    private ObjectMapper objectMapper;

    // --- BẮT ĐẦU THAY ĐỔI: Thêm các hằng số để dễ quản lý ---
    private static final int MAX_OTP_ATTEMPTS = 5;
    private static final long OTP_ATTEMPT_WINDOW_MS = 60 * 1000; // 1 phút
    // --- KẾT THÚC THAY ĐỔI ---

    @Override
    public void init() throws ServletException {
        super.init();
        accountServices = new AccountServices();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/register");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        Map<String, Object> jsonResponse = new HashMap<>();

        if (session == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Phiên làm việc không hợp lệ hoặc đã hết hạn. Vui lòng thử đăng ký lại.");
            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();
            return;
        }

        // --- BẮT ĐẦU THAY ĐỔI: Logic kiểm tra khóa tài khoản ---
        Long lockoutTime = (Long) session.getAttribute("otp_lockout_time");
        if (lockoutTime != null && System.currentTimeMillis() < lockoutTime) {
            long remainingSeconds = (lockoutTime - System.currentTimeMillis()) / 1000;
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Bạn đã nhập sai quá nhiều lần. Vui lòng thử lại sau " + remainingSeconds + " giây.");
            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();
            return;
        }
        // Sau khi hết thời gian khóa, xóa thuộc tính để cho phép thử lại
        if (lockoutTime != null && System.currentTimeMillis() >= lockoutTime) {
            session.removeAttribute("otp_lockout_time");
        }
        // --- KẾT THÚC THAY ĐỔI ---


        String userSubmittedOtp = request.getParameter("otp");

        if (userSubmittedOtp == null || userSubmittedOtp.trim().isEmpty()) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Vui lòng nhập mã OTP.");
            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();
            return;
        }

        try {
            RegistrationResult registrationResult = accountServices.finalizeOtpRegistration(userSubmittedOtp.trim(), session);

            if (registrationResult.isSuccess()) {
                // --- BẮT ĐẦU THAY ĐỔI: Xóa các thuộc tính theo dõi khi thành công ---
                session.removeAttribute("otp_failure_count");
                session.removeAttribute("otp_first_failure_time");
                session.removeAttribute("otp_lockout_time");
                // --- KẾT THÚC THAY ĐỔI ---

                jsonResponse.put("success", true);
                jsonResponse.put("message", registrationResult.getMessage());
                jsonResponse.put("redirectUrl", request.getContextPath() + "/login");
            } else {
                // --- BẮT ĐẦU THAY ĐỔI: Logic xử lý khi nhập sai OTP ---
                handleOtpFailure(session, jsonResponse, registrationResult.getMessage());
                // --- KẾT THÚC THAY ĐỔI ---
            }
            out.print(objectMapper.writeValueAsString(jsonResponse));

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Đã xảy ra lỗi không mong muốn trong quá trình xác thực OTP.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(objectMapper.writeValueAsString(jsonResponse));
        } finally {
            if (out != null) {
                out.flush();
            }
        }
    }

    /**
     * Xử lý logic khi người dùng nhập sai OTP, bao gồm đếm số lần sai và khóa tạm thời.
     *
     * @param session      HttpSession của người dùng.
     * @param jsonResponse Map để xây dựng phản hồi JSON.
     * @param errorMessage Thông báo lỗi gốc từ service.
     */
    private void handleOtpFailure(HttpSession session, Map<String, Object> jsonResponse, String errorMessage) {
        Integer failureCount = (Integer) session.getAttribute("otp_failure_count");
        Long firstFailureTime = (Long) session.getAttribute("otp_first_failure_time");

        long currentTime = System.currentTimeMillis();

        if (failureCount == null || (firstFailureTime != null && (currentTime - firstFailureTime > OTP_ATTEMPT_WINDOW_MS))) {
            // Đây là lần sai đầu tiên HOẶC lần sai trước đã quá 1 phút -> reset
            failureCount = 1;
            firstFailureTime = currentTime;
        } else {
            // Tăng số lần sai trong khoảng thời gian 1 phút
            failureCount++;
        }

        session.setAttribute("otp_failure_count", failureCount);
        session.setAttribute("otp_first_failure_time", firstFailureTime);

        if (failureCount >= MAX_OTP_ATTEMPTS) {
            // Đạt đến ngưỡng sai tối đa, khóa tài khoản trong 1 phút
            long lockoutEndTime = currentTime + OTP_ATTEMPT_WINDOW_MS;
            session.setAttribute("otp_lockout_time", lockoutEndTime);
            // Xóa bộ đếm vì đã chuyển sang trạng thái khóa
            session.removeAttribute("otp_failure_count");
            session.removeAttribute("otp_first_failure_time");

            jsonResponse.put("success", false);
            jsonResponse.put("message", "Bạn đã nhập sai OTP " + MAX_OTP_ATTEMPTS + " lần. Vui lòng thử lại sau 1 phút.");
        } else {
            // Vẫn chưa đạt ngưỡng, chỉ thông báo lỗi
            jsonResponse.put("success", false);
            int remainingAttempts = MAX_OTP_ATTEMPTS - failureCount;
            jsonResponse.put("message", errorMessage + " Bạn còn " + remainingAttempts + " lần thử.");
        }
    }
}