package com.example.webbongden.filter;

import com.example.webbongden.dao.LogDao;
import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Log;
import com.example.webbongden.dao.model.SecurityUtils;
import com.example.webbongden.utils.NotificationUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebFilter("/*")
public class AdminActionLoggingFilter implements Filter {
    private final LogDao logDao = new LogDao();
    private static final Map<String, String> actionMap = new LinkedHashMap<>();

    static {
        actionMap.put("add-product-image", "ADD_PRODUCT_IMAGE");
        actionMap.put("add-product", "ADD_PRODUCT");
        actionMap.put("update-product", "UPDATE_PRODUCT");
        actionMap.put("edit-product-detail", "UPDATE_PRODUCT_DETAIL");
        actionMap.put("deleteproduct", "DELETE_PRODUCT");
        actionMap.put("delete-product-image", "DELETE_PRODUCT_IMAGE");
        actionMap.put("subcategories/add", "ADD_SUBCATEGORY");
        actionMap.put("subcategories/delete", "DELETE_SUBCATEGORY");
        actionMap.put("categories/add", "ADD_CATEGORY");
        actionMap.put("categories/delete", "DELETE_CATEGORY");
        actionMap.put("add-account", "ADD_ACCOUNT");
        actionMap.put("deleteaccount", "DELETE_ACCOUNT");
        actionMap.put("update-account", "UPDATE_ACCOUNT");
        actionMap.put("update-order-status", "UPDATE_ORDER_STATUS");
        actionMap.put("add-product-to-promotion", "ADD_PRODUCT_TO_PROMOTION");
        actionMap.put("add-promotion", "ADD_PROMOTION");
        actionMap.put("update-promotion", "UPDATE_PROMOTION");
        actionMap.put("delete-promotion", "DELETE_PROMOTION");
        actionMap.put("remove-product-from-promotion", "REMOVE_PRODUCT_FROM_PROMOTION");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        // Kiểm tra quyền truy cập của người dùng vào dashboard admin
        if (!SecurityUtils.hasPermission(req, "access_admin_dashboard")) {
            chain.doFilter(request, response);  // Nếu không có quyền, tiếp tục filter
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null) {
            chain.doFilter(request, response);  // Không có session, tiếp tục filter
            return;
        }

        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            chain.doFilter(request, response);  // Không có account trong session, tiếp tục filter
            return;
        }

        String uri = req.getRequestURI();
        String action = detectAdminAction(uri);  // Xác định action

        if (action == null) {
            chain.doFilter(request, response);  // Không có action, tiếp tục filter
            return;
        }

        // Tiếp tục chuỗi filter sau khi xác định action
        chain.doFilter(request, response);

        // Kiểm tra nếu trang là "view-only" (chỉ xem, không cần ghi log)
        String page = req.getParameter("page");
        if (page != null && isViewOnlyPage(page)) return;

        // Kiểm tra trạng thái action (success hay failure)
        Object status = req.getAttribute("actionStatus");
        boolean isSuccess = "success".equals(status);

        if (isSuccess) {
            try {
                Log logEntry = new Log();
                logEntry.setAccountId(account.getId());
                logEntry.setLevel(account.getRoleName());
                logEntry.setAction(action);
                logEntry.setResource(detectResourceFromAction(action));

                // Lấy dữ liệu trước và sau khi thay đổi để log
                Object beforeDataObj = request.getAttribute("beforeData");
                Object afterDataObj = request.getAttribute("afterData");

                logEntry.setBeforeData(beforeDataObj != null ? beforeDataObj.toString() : null);
                logEntry.setAfterData(afterDataObj != null ? afterDataObj.toString() : null);

                // Chèn log vào cơ sở dữ liệu một lần duy nhất
                int logId = logDao.insertLog(logEntry);

                // Gửi thông báo cho admin
                String message = buildNotificationMessage(action, req);
                NotificationUtils.notifyAllAdmins(message, "/search-log?logId=" + logId);

            } catch (Exception e) {
                System.err.println("⚠️ Logging failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String detectAdminAction(String uri) {
        uri = uri.toLowerCase();

        if (uri.contains("view") || uri.contains("list") || uri.contains("load")) return null;

        for (Map.Entry<String, String> entry : actionMap.entrySet()) {
            if (uri.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }

    private String detectResourceFromAction(String action) {
        if (action.contains("PRODUCT")) return "PRODUCT";
        if (action.contains("SUBCATEGORY")) return "SUBCATEGORY";
        if (action.contains("CATEGORY")) return "CATEGORY";
        if (action.contains("ACCOUNT")) return "ACCOUNT";
        if (action.contains("ORDER")) return "ORDER";
        if (action.contains("PROMOTION")) return "PROMOTION";
        return "UNKNOWN";
    }

    private String buildNotificationMessage(String action, HttpServletRequest req) {
        String verb;
        if (action.startsWith("ADD")) {
            verb = "thêm mới";
        } else if (action.startsWith("UPDATE")) {
            verb = "cập nhật";
        } else if (action.startsWith("DELETE") || action.startsWith("REMOVE")) {
            verb = "xoá";
        } else {
            verb = "thực hiện";
        }

        String resource = detectResourceFromAction(action).toLowerCase();
        String detail = "";

        switch (action) {
            case "ADD_PRODUCT_IMAGE":
                Object productIdObj = req.getAttribute("productId");
                if (productIdObj != null) {
                    detail = " cho sản phẩm có ID " + productIdObj;
                }
                break;

            case "UPDATE_ORDER_STATUS":
                Object orderId = req.getAttribute("orderId");
                Object status = req.getAttribute("newStatus");
                if (orderId != null && status != null) {
                    detail = " đơn hàng #" + orderId + " sang trạng thái \"" + status + "\"";
                } else if (orderId != null) {
                    detail = " đơn hàng #" + orderId;
                }
                break;

            case "ADD_ACCOUNT":
                Object username = req.getAttribute("username");
                if (username != null) {
                    detail = " \"" + username + "\"";
                }
                break;

            case "UPDATE_ACCOUNT":
                Object accountName = req.getAttribute("accountName");
                if (accountName != null) {
                    detail = " tài khoản \"" + accountName + "\"";
                }
                break;

            case "ADD_PRODUCT":
                Object productName = req.getAttribute("productName");
                if (productName != null) {
                    detail = " sản phẩm \"" + productName + "\"";
                }
                break;

            case "UPDATE_PRODUCT_DETAIL":
                Object productId = req.getAttribute("productId");
                if (productId != null) {
                    detail = " sản phẩm \"" + productId + "\"";
                }
                break;

            case "DELETE_PRODUCT":
                Object deletedProductId = req.getAttribute("productId");
                if (deletedProductId != null) {
                    detail = " sản phẩm có ID \"" + deletedProductId + "\"";
                }
                break;

            case "DELETE_PRODUCT_IMAGE":
                Object deletedImgProductId = req.getAttribute("productId");
                if (deletedImgProductId != null) {
                    detail = " ảnh của sản phẩm có ID \"" + deletedImgProductId + "\"";
                }
                break;
        }
        return "Admin vừa " + verb + " " + resource + detail;
    }

    private boolean isViewOnlyPage(String page) {
        if (page == null) return false;
        page = page.toLowerCase();
        return page.contains("statistics")
                || page.contains("dashboard")
                || page.contains("list")
                || page.contains("report")
                || page.contains("view")
                || page.contains("detail");
    }
}


