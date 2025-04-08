package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.UserDao;
import com.example.webbongden.dao.model.CustomerUpdate;
import com.example.webbongden.dao.model.User;
import com.example.webbongden.services.AccountServices;
import com.example.webbongden.services.UserSevices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "EditCusInfoController", value = "/edit-cus-info")
public class EditCusInfoController extends HttpServlet {
    private final UserSevices userSevices = new UserSevices();
    private final UserDao userDao = new UserDao();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            // Lấy dữ liệu từ request
            String json = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            Gson gson = new GsonBuilder()
                    .serializeNulls()  // 👈 cho phép hiện cả field có giá trị null
                    .create();

            CustomerUpdate updateRequest = gson.fromJson(json, CustomerUpdate.class);

            // 2. Lấy thông tin cũ (trước khi update)
            User beforeUpdate = userDao.getCustomerById(updateRequest.getCustomerId());
            // Gọi service để cập nhật thông tin
            boolean isUpdated = userSevices.updateCustomerInfo(
                    updateRequest.getCustomerId(),
                    updateRequest.getCusName(),
                    updateRequest.getAddress(),
                    updateRequest.getPhone()
            );
            if (isUpdated) {
                User afterUpdate = new User();
                afterUpdate.setCustomerName(updateRequest.getCusName());
                afterUpdate.setPhone(updateRequest.getPhone());
                afterUpdate.setAddress(updateRequest.getAddress());

                request.setAttribute("beforeData", gson.toJson(beforeUpdate));
                request.setAttribute("afterData", gson.toJson(afterUpdate));
                request.setAttribute("actionStatus", "success");
                // Phản hồi kết quả
                response.getWriter().write("{\"success\": " + isUpdated + "}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi hệ thống!\"}");
        }
    }
}
