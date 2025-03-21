package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.*;
import com.example.webbongden.pay.Config;
import com.example.webbongden.services.OrderSevices;
import com.example.webbongden.services.ProductServices;
import com.example.webbongden.services.PromotionService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "PayCartController", value = "/PayCartController")
public class PayCartController extends HttpServlet {
    private static final OrderSevices orderServices;
    private static final PromotionService promotionService;
    private static final ProductServices productServices;
    static {
        orderServices = new OrderSevices();
        promotionService = new PromotionService();
        productServices = new ProductServices();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Lấy thông tin từ session
        Cart cart = (Cart) session.getAttribute("cart");
        Customer customerInfo = (Customer) session.getAttribute("customerInfo");
        Account account = (Account) session.getAttribute("account");

        if (cart == null || customerInfo == null || account == null) {
            request.setAttribute("errorMessage", "Thanh toán thất bại. Vui lòng kiểm tra lại thông tin giỏ hàng và khách hàng.");
            request.getRequestDispatcher("/user/cart.jsp").forward(request, response);
            return;
        }

        try {
            // Lấy phương thức thanh toán từ request
            String paymentMethod = request.getParameter("paymentMethod");

            // Tạo hóa đơn
            Invoices invoice = new Invoices();
            invoice.setAccountId(account.getId());
            invoice.setCreatedAt(new Date());
            invoice.setTotalPrice(cart.getTotalPriceNumber());
            invoice.setPaymentStatus("Pending");

            int promotionId = 0;
            List<OrderDetail> orderDetails = new ArrayList<>();

            for (CartItem item : cart.getItems()) {
                productServices.decreaseStockQuantity(item.getProductId(), item.getQuantity());
                OrderDetail detail = new OrderDetail();
                detail.setProductId(item.getProductId());
                detail.setQuantity(item.getQuantity());
                detail.setUnitPrice(item.getPrice());
                detail.setItemDiscount(0);
                detail.setAmount(item.getPrice() * item.getQuantity());

                Promotion gift = promotionService.getPromotionById(item.getProductId());
                if (gift != null && promotionId == 0) {
                    promotionId = gift.getId();
                }

                orderDetails.add(detail);
            }

            invoice.setPromotionId(promotionId);

            // Lưu hóa đơn và chi tiết đơn hàng
            orderServices.createOrderAndInvoice(invoice, orderDetails, customerInfo);

            // Nếu chọn COD, kết thúc tại đây
            if ("COD".equals(paymentMethod)) {
                session.removeAttribute("cart");
                response.sendRedirect("/WebBongDen_war/cart#finish");
                return;
            }

            // Nếu chọn VNPay, tiếp tục xử lý
            int hoadon =  invoice.getId();
            // VNP:
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";

            long amount = (long) (cart.getTotalPriceNumber() * 100);
            String vnp_TxnRef = hoadon+"";//dky ma rieng
            String vnp_IpAddr = Config.getIpAddress(request);

            String vnp_TmnCode = Config.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", orderType);

            String locate = request.getParameter("language");
            if (locate != null && !locate.isEmpty()) {
                vnp_Params.put("vnp_Locale", locate);
            } else {
                vnp_Params.put("vnp_Locale", "vn");
            }
            vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
            System.out.println("VNPay Pay URL: " + Config.vnp_PayUrl);
            response.sendRedirect(paymentUrl);
            System.out.println("VNPay URL: " + paymentUrl);
            // Xóa giỏ hàng khỏi session sau khi thanh toán
            session.removeAttribute("cart");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
