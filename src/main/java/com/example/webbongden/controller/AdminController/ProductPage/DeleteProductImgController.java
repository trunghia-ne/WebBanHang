package com.example.webbongden.controller.AdminController.ProductPage;

import com.example.webbongden.dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeleteProductImgController", value = "/delete-product-image")
public class DeleteProductImgController extends HttpServlet {
    private ProductDao productDao = new ProductDao();

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int imageId = Integer.parseInt(request.getParameter("id"));

            boolean deleted = productDao.deleteImageById(imageId);

            if (deleted) {
                request.setAttribute("actionStatus", "success");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể xóa ảnh");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi server");
        }
    }
}
