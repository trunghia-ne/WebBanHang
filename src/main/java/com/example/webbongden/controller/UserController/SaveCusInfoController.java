package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Customer;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "SaveCusInfoController", value = "/save-cus-info")
public class SaveCusInfoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Thường không xử lý GET cho việc lưu thông tin, có thể redirect hoặc báo lỗi
        response.sendRedirect(request.getContextPath() + "/cart"); // Ví dụ: quay về trang giỏ hàng
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Đặt encoding ở đầu
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        // Giữ nguyên tên tham số như trong JSP (province, district, ward)
        String provinceName = request.getParameter("province"); // Tên tỉnh/TP
        String districtName = request.getParameter("district"); // Tên quận/huyện
        String wardName = request.getParameter("ward");       // Tên phường/xã
        String streetAddress = request.getParameter("streetAddress");
        String note = request.getParameter("note");
        String shippingFeeStr = request.getParameter("shippingFee"); // Lấy phí vận chuyển từ input ẩn

        // Xây dựng địa chỉ đầy đủ
        StringBuilder customerAddressBuilder = new StringBuilder();
        if (streetAddress != null && !streetAddress.trim().isEmpty()) {
            customerAddressBuilder.append(streetAddress);
        }
        if (wardName != null && !wardName.trim().isEmpty()) {
            if (customerAddressBuilder.length() > 0) customerAddressBuilder.append(", ");
            customerAddressBuilder.append(wardName);
        }
        if (districtName != null && !districtName.trim().isEmpty()) {
            if (customerAddressBuilder.length() > 0) customerAddressBuilder.append(", ");
            customerAddressBuilder.append(districtName);
        }
        if (provinceName != null && !provinceName.trim().isEmpty()) {
            if (customerAddressBuilder.length() > 0) customerAddressBuilder.append(", ");
            customerAddressBuilder.append(provinceName);
        }
        String customerAddress = customerAddressBuilder.toString();

        double shippingFee = 0.0;
        if (shippingFeeStr != null && !shippingFeeStr.isEmpty()) {
            try {
                shippingFee = Double.parseDouble(shippingFeeStr);
            } catch (NumberFormatException e) {
                // Ghi log lỗi hoặc xử lý nếu giá trị không hợp lệ
                System.err.println("Lỗi parse phí vận chuyển: " + shippingFeeStr + " - " + e.getMessage());
                // Bạn có thể đặt một giá trị mặc định hoặc thông báo lỗi tùy theo logic nghiệp vụ
            }
        }

        // Lấy hoặc tạo mới đối tượng Customer
        // Nếu bạn muốn cập nhật Customer đã có trong session (nếu người dùng quay lại sửa):
        Customer customerInfo = (Customer) session.getAttribute("customerInfo");
        if (customerInfo == null) {
            customerInfo = new Customer();
            // Nếu là khách hàng mới và có liên kết với account
            if (account != null) {
                customerInfo.setId(account.getId()); // Giả sử ID khách hàng liên kết với ID tài khoản
            }
        }
        // Nếu không có account session hoặc customerInfo là cho khách vãng lai, bạn có thể bỏ qua setId từ account.
        // Hoặc nếu ID khách hàng được quản lý riêng, bạn cần logic khác để gán ID.


        customerInfo.setCusName(customerName);
        customerInfo.setPhone(customerPhone);
        customerInfo.setAddress(customerAddress);
        customerInfo.setNote(note);
        customerInfo.setShippingFee(shippingFee); // <-- LƯU PHÍ VẬN CHUYỂN VÀO OBJECT

        session.setAttribute("customerInfo", customerInfo); // Lưu lại vào session

        // Quay lại trang cart.jsp và hiển thị Tab 3 (Thanh toán)
        response.sendRedirect(request.getContextPath() + "/cart#payment");
    }
}