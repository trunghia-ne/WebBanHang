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
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        String provinceName = request.getParameter("province");
        String districtName = request.getParameter("district");
        String wardName = request.getParameter("ward");
        String streetAddress = request.getParameter("streetAddress");
        String note = request.getParameter("note");
        String shippingFeeStr = request.getParameter("shippingFee");

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
                System.err.println("Lỗi parse phí vận chuyển: " + shippingFeeStr + " - " + e.getMessage());
            }
        }

        Customer customerInfo = (Customer) session.getAttribute("customerInfo");
        if (customerInfo == null) {
            customerInfo = new Customer();
            if (account != null) {
                customerInfo.setId(account.getId());
            }
        }


        customerInfo.setCusName(customerName);
        customerInfo.setPhone(customerPhone);
        customerInfo.setAddress(customerAddress);
        customerInfo.setNote(note);
        customerInfo.setShippingFee(shippingFee);

        session.setAttribute("customerInfo", customerInfo);

        response.sendRedirect(request.getContextPath() + "/cart#payment");
    }
}