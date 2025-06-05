package com.example.webbongden.controller.AdminController.VoucherPage; import com.example.webbongden.dao.VoucherDao;
import com.example.webbongden.dao.model.Voucher;
import com.example.webbongden.utils.LogUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

@WebServlet(name = "EditVoucherController", value = "/edit-voucher")
public class EditVoucherController extends HttpServlet { 

@Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain;charset=UTF-8");

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String code = req.getParameter("code").trim();
            String discountType = req.getParameter("discountType");
            BigDecimal discountValue = new BigDecimal(req.getParameter("discountValue"));
            Date startDate = Date.valueOf(req.getParameter("startDate"));
            Date endDate = Date.valueOf(req.getParameter("endDate"));
            BigDecimal minOrderValue = new BigDecimal(req.getParameter("minOrderValue"));
            int usageLimit = Integer.parseInt(req.getParameter("usageLimit"));
            String status = req.getParameter("status");

            VoucherDao voucherDao = new VoucherDao();

            // 1. Kiểm tra mã voucher đã tồn tại (ngoại trừ voucher đang sửa)
            Voucher existingVoucher = voucherDao.findByCode(code);
            if (existingVoucher != null && existingVoucher.getId() != id) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Mã giảm giá này đã tồn tại. Vui lòng chọn mã khác.");
                return;
            }

            // 2. Kiểm tra ngày bắt đầu < ngày kết thúc
            if (startDate.after(endDate) || startDate.equals(endDate)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Ngày bắt đầu không được sau ngày kết thúc.");
                return;
            }

            // 3. Kiểm tra giá trị giảm giá > 0
            if (discountValue.compareTo(BigDecimal.ZERO) <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Giá trị giảm giá phải lớn hơn 0.");
                return;
            }

            // 4. Nếu là phần trăm, không vượt quá 100%
            if ("percent".equalsIgnoreCase(discountType) && discountValue.compareTo(new BigDecimal("100")) > 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Giá trị phần trăm không được vượt quá 100%.");
                return;
            }

            // 5. Kiểm tra số lần sử dụng > 0
            if (usageLimit <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Số lần sử dụng phải lớn hơn 0.");
                return;
            }

            Voucher voucher = new Voucher();
            voucher.setId(id);
            voucher.setCode(code);
            voucher.setDiscountType(discountType);
            voucher.setDiscountValue(discountValue);
            voucher.setStartDate(startDate);
            voucher.setEndDate(endDate);
            voucher.setMinOrderValue(minOrderValue);
            voucher.setUsageLimit(usageLimit);
            voucher.setStatus(status);

            voucherDao.update(voucher);
            req.setAttribute("actionStatus", "success");
            req.setAttribute("voucherId", voucher.getId());
            LogUtils.logUpdateVoucher(req, existingVoucher, voucher);

            resp.setContentType("application/json");
            resp.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Lỗi khi cập nhật: " + e.getMessage() + "\"}");
        }
    }

}