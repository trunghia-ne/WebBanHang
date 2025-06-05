package com.example.webbongden.controller.AdminController.VoucherPage;

import com.example.webbongden.dao.VoucherDao;
import com.example.webbongden.dao.model.Voucher;
import com.example.webbongden.utils.LogUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "AddVoucherController", value = "/add-voucher")
public class AddVoucherController extends HttpServlet {
    VoucherDao voucherDao = new VoucherDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Voucher> vouchers = voucherDao.getAll();
        req.setAttribute("vouchers", vouchers);
        req.getRequestDispatcher("admin?page=voucher").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=UTF-8"); // Thiết lập định dạng trả về text đơn giản
        try {
            String code = req.getParameter("code");
            String discountType = req.getParameter("discountType");
            String discountValueStr = req.getParameter("discountValue");
            String startDateStr = req.getParameter("startDate");
            String endDateStr = req.getParameter("endDate");
            String minOrderValueStr = req.getParameter("minOrderValue");
            String usageLimitStr = req.getParameter("usageLimit");

            // 1. Kiểm tra mã voucher đã tồn tại
            if (voucherDao.findByCode(code) != null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Mã giảm giá này đã tồn tại. Vui lòng chọn mã khác.");
                return;
            }

            // 2. Parse dữ liệu
            BigDecimal discountValue = new BigDecimal(discountValueStr);
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            BigDecimal minOrderValue = (minOrderValueStr == null || minOrderValueStr.isEmpty())
                    ? BigDecimal.ZERO : new BigDecimal(minOrderValueStr);
            int usageLimit = Integer.parseInt(usageLimitStr);

            // 3. Kiểm tra ngày bắt đầu và kết thúc
            if (startDate.after(endDate)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Ngày bắt đầu không được sau ngày kết thúc.");
                return;
            }

            // 4. Kiểm tra giá trị giảm giá > 0
            if (discountValue.compareTo(BigDecimal.ZERO) <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Giá trị giảm giá phải lớn hơn 0.");
                return;
            }

            // 5. Nếu là phần trăm, không được vượt quá 100%
            if ("percent".equalsIgnoreCase(discountType) &&
                    discountValue.compareTo(BigDecimal.valueOf(100)) > 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Giá trị phần trăm không được vượt quá 100%.");
                return;
            }

            // 6. Kiểm tra số lần sử dụng > 0
            if (usageLimit <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Số lần sử dụng phải lớn hơn 0.");
                return;
            }

            // 7. Tạo và lưu voucher
            Voucher voucher = new Voucher();
            voucher.setCode(code);
            voucher.setDiscountType(discountType);
            voucher.setDiscountValue(discountValue);
            voucher.setStartDate(startDate);
            voucher.setEndDate(endDate);
            voucher.setMinOrderValue(minOrderValue);
            voucher.setUsageLimit(usageLimit);
            voucher.setStatus("active");

            voucherDao.insert(voucher);
            req.setAttribute("actionStatus", "success");
            LogUtils.logAddVoucher(req, voucher);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
    }

}
