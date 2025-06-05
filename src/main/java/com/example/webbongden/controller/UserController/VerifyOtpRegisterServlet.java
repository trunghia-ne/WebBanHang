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

        String userSubmittedOtp = request.getParameter("otp");

        HttpSession session = request.getSession(false);

        Map<String, Object> jsonResponse = new HashMap<>();

        if (session == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Phiên làm việc không hợp lệ hoặc đã hết hạn. Vui lòng thử đăng ký lại.");
            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();
            return;
        }

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
                jsonResponse.put("success", true);
                jsonResponse.put("message", registrationResult.getMessage());
                jsonResponse.put("redirectUrl", request.getContextPath() + "/login");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", registrationResult.getMessage());
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
}