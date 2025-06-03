package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.VoucherDao;
import com.example.webbongden.dao.model.Voucher;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ApplyVoucherController", value = "/ApplyVoucherController")
public class ApplyVoucherController extends HttpServlet {
    VoucherDao voucherDao = new VoucherDao();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Voucher> voucherList = voucherDao.getAll();

        // Chuyển danh sách voucher thành JSON
        String json = new Gson().toJson(voucherList);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("discountCode");

        BigDecimal subtotalBD;
        try {
            subtotalBD = new BigDecimal(request.getParameter("subtotal"));
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "Tổng tiền không hợp lệ, vui lòng thử lại.", null, null);
            return;
        }

        Voucher voucher = voucherDao.findValidVoucher(code, subtotalBD);

        if (voucher == null) {
            sendJsonResponse(response, false, "Mã giảm giá không hợp lệ hoặc đã hết hạn, vui lòng kiểm tra lại.", null, null);
            return;
        }

        BigDecimal discountAmount;
        if ("percent".equalsIgnoreCase(voucher.getDiscountType())) {
            discountAmount = subtotalBD.multiply(voucher.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
        } else {
            discountAmount = voucher.getDiscountValue();
        }

        if (discountAmount.compareTo(subtotalBD) > 0) {
            discountAmount = subtotalBD;
        }

        // Lưu voucher đã áp dụng vào session (nếu cần)
        request.getSession().setAttribute("appliedVoucher", voucher);

        String successMessage = String.format("Áp dụng mã giảm giá thành công! Bạn được giảm %,.0f VND.", discountAmount.doubleValue());

        sendJsonResponse(response, true, successMessage, discountAmount, voucher.getId());
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, BigDecimal discountAmount, Integer voucherId) throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("success", success);
        jsonMap.put("message", message);
        if (success) {
            jsonMap.put("discountAmount", discountAmount.doubleValue());
            jsonMap.put("voucherId", voucherId);
        }

        String json = new Gson().toJson(jsonMap);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
