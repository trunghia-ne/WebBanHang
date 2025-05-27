<%@ page import="com.example.webbongden.dao.model.Cart" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.example.webbongden.dao.model.CartItem" %>
<%@ page import="com.example.webbongden.dao.model.Customer" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--

  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/15/2024
  Time: 10:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%
    HttpSession cartSession = request.getSession(); // Lấy session hiện tại (nếu có)
    Cart cart = (Cart) cartSession.getAttribute("cart"); // Lấy giỏ hàng từ session
    Customer cus = (Customer) cartSession.getAttribute("customerInfo"); // Lấy customerInfo từ session (đã bao gồm shippingFee nếu controller đã lưu)

    double cartSubtotalForJs = 0;
    if (cart != null && !cart.isEmpty()) {
        cartSubtotalForJs = cart.getTotalPriceNumber(); // Giả sử bạn đã thêm phương thức này vào Cart.java
    }

    // --- MODIFIED PART: Calculate initial values for payment tab ---
    double initialShippingFee = 0;
    double initialTotalWithShipping = cartSubtotalForJs; // Bắt đầu bằng tổng tiền hàng

    if (cus != null) {
        initialShippingFee = cus.getShippingFee(); // Lấy phí ship từ đối tượng Customer
        if (initialShippingFee >= 0) { // Phí vận chuyển hợp lệ (bao gồm cả miễn phí = 0)
            initialTotalWithShipping += initialShippingFee;
        } else {
            // Nếu phí vận chuyển là số âm hoặc chưa được tính đúng cách, coi như là 0 cho hiển thị ban đầu
            initialShippingFee = 0; // Hoặc một giá trị mặc định khác bạn muốn
            // initialTotalWithShipping sẽ giữ nguyên là cartSubtotalForJs
        }
    }
    // --- END OF MODIFIED PART ---
%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="./img/logo-fold.png" sizes="180x180"> <%-- Nên dùng ${pageContext.request.contextPath}/img/logo-fold.png --%>
    <title>Giỏ hàng & Thanh toán</title>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
            integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
            crossorigin="anonymous"
            referrerpolicy="no-referrer"
    />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
            href="https://fonts.googleapis.com/css2?family=Nunito+Sans:ital,opsz,wght@0,6..12,200..1000;1,6..12,200..1000&family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
            rel="stylesheet"
    />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/cart.css?v=2.2"> <%-- Increased version for cache busting --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/header-footer.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">
    <style>
        .empty-cart {
            height: 50%;
            display: flex;
            flex-direction: column;
            gap: 20px;
            justify-content: center;
            align-items: center;
        }

        .back-to-shopping-btn {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-transform: uppercase;
            font-weight: 500;
            font-size: 16px;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .back-to-shopping-btn:hover {
            background-color: #0056b3;
            transform: scale(1.05);
        }

        .empty-cart-image {
            max-width: 200px;
            margin-bottom: 20px;
            opacity: 0.8;
            transition: opacity 0.3s ease;
        }

        .empty-cart-image:hover {
            opacity: 1;
        }
    </style>
</head>
<body>
<div class="wrapper">
    <jsp:include page="../reuse/header.jsp" />
    <div class="main">
        <a class="direction" style="margin: 20px 140px; display: block; text-decoration: none; color: black;" href="${pageContext.request.contextPath}/home">
            <i class="fa-solid fa-arrow-left"></i>
            <span id="direction-cart">Mua thêm sản phẩm</span>
        </a>
        <div class="nav">
            <div class="nav-item active" data-tab="cart">Giỏ hàng</div>
            <div class="nav-item" data-tab="cus-info">Thông tin đặt hàng</div>
            <div class="nav-item" data-tab="payment">Thanh toán</div>
            <div class="nav-item" data-tab="finish">Hoàn tất</div>
        </div>

        <div class="tab-container">
            <div class="tab-content" id="cart">
                <div class="container-top">
                    <% if (cart == null || cart.getItems().isEmpty()) { %>
                    <div class="empty-cart">
                        <img src="${pageContext.request.contextPath}/assets/img/empty-cart.png" alt="Giỏ hàng trống" class="empty-cart-image">
                        <p>Giỏ hàng của bạn trống.</p>
                        <a href="${pageContext.request.contextPath}/home">
                            <button class="back-to-shopping-btn">Quay lại mua hàng</button>
                        </a>
                    </div>
                    <% } else { %>
                    <div class="product">
                        <ul class="list-product">
                            <% for (CartItem item : cart.getItems()) { %>
                            <li class="product-item">
                                <div class="box">
                                    <div class="product-img">
                                        <img src="<%= item.getImageUrl() %>" alt="<%= item.getProductName() %>">
                                    </div>
                                    <div class="delete-item">
                                        <a href="${pageContext.request.contextPath}/delete-cart-item?productId=<%= item.getProductId() %>" class="delete-btn">Xóa</a>
                                    </div>
                                </div>
                                <div class="product-detail">
                                    <div class="product-name"><%= item.getProductName() %></div>
                                    <div class="gift-section" style="display: flex; gap: 10px">
                                        <% if (item.getGiftName() != null && !item.getGiftName().isEmpty()) { %>
                                        <p><%= item.getGiftName() %></p>
                                        <% } else { %>
                                        <p>Không có quà tặng.</p>
                                        <% } %>
                                    </div>
                                </div>
                                <div class="product-qp">
                                    <div class="product-price">
                                        <p>
                                            <% if (item.getUnitPrice() > item.getPrice()) { %>
                                            <s><fmt:formatNumber value="<%= item.getUnitPrice() %>" type="currency" currencySymbol="" minFractionDigits="0" maxFractionDigits="0"/> VND</s>
                                            <% } %>
                                        </p>
                                        <p>
                                            <%= item.getFormattedPrice() %> VND
                                        </p>
                                    </div>
                                    <div class="quantity-selector">
                                        <form action="${pageContext.request.contextPath}/update-cart" method="post" onsubmit="return false;">
                                            <input type="hidden" name="productId" value="<%= item.getProductId() %>" />
                                            <button class="quantity-btn decrease" type="button" onclick="updateQuantity(this, -1)">-</button>
                                            <input
                                                    type="number"
                                                    name="quantity"
                                                    class="quantity-input"
                                                    value="<%= item.getQuantity() %>"
                                                    min="1"
                                                    readonly
                                            />
                                            <button class="quantity-btn increase" type="button" onclick="updateQuantity(this, 1)">+</button>
                                        </form>
                                    </div>
                                </div>
                            </li>
                            <% } %>
                        </ul>
                        <div class="a"></div>
                    </div>
                    <% } %>
                </div>
                <% if (cart != null && !cart.getItems().isEmpty()) { %>
                <div class="container-bottom">
                    <div class="result">
                        <div class="b">
                            <p>Tạm tính:</p>
                            <p class="total-price"><%= cart.getTotalPrice() %> VND</p>
                        </div>
                        <div class="b">
                            <p>Phí vận chuyển:</p>
                            <p>Sẽ được tính ở bước sau</p>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/cart#cus-info">
                        <button class="buy-btn" data-tab="cus-info">ĐẶT HÀNG NGAY</button>
                    </a>
                </div>
                <% } %>
            </div>
            <div class="tab-content" id="cus-info">
                <form action="${pageContext.request.contextPath}/save-cus-info" id ="customer-info-form" method="post">
                    <input type="hidden" id="cartSubtotal" value="<%= cartSubtotalForJs %>">
                    <div class="cus-info">
                        <div class="container-top">
                            <div class="cus-info-item">
                                <p class="title">Thông tin khách mua hàng</p>
                                <div class="cus-field-info">
                                    <div class="form-field">
                                        <input type="text" class="form-input" id="cus-name" name="customerName" placeholder=" " required value="<%= (cus != null && cus.getCusName() != null) ? cus.getCusName() : "" %>"/>
                                        <label for="cus-name" class="form-label">Nhập họ tên</label>
                                    </div>

                                    <div class="form-field">
                                        <input type="tel" class="form-input" id="cus-tele" name="customerPhone" placeholder=" " required value="<%= (cus != null && cus.getPhone() != null) ? cus.getPhone() : "" %>"/>
                                        <label for="cus-tele" class="form-label">Nhập số điện thoại</label>
                                    </div>
                                </div>
                            </div>

                            <div class="address-container">
                                <p class="title">Địa chỉ khách hàng</p>
                                <div class="address-box">
                                    <div class="address-row">
                                        <select id="province" name="province" required>
                                            <option value="">Chọn Tỉnh, Thành phố</option>
                                        </select>
                                        <input type="hidden" id="ghn_province_id" name="ghn_province_id">

                                        <select id="district" name="district" required>
                                            <option value="">Chọn Quận, Huyện</option>
                                        </select>
                                        <input type="hidden" id="ghn_district_id" name="ghn_district_id">
                                    </div>

                                    <div class="address-row">
                                        <select id="ward" name="ward" required>
                                            <option value="">Chọn Phường, Xã</option>
                                        </select>
                                        <input type="hidden" id="ghn_ward_code" name="ghn_ward_code">

                                        <div class="form-field">
                                            <%
                                                String streetAddressValue = "";
                                                if (cus != null && cus.getAddress() != null) {
                                                    String[] addressParts = cus.getAddress().split(",");
                                                    if (addressParts.length > 0) {
                                                        streetAddressValue = addressParts[0].trim();
                                                    }
                                                }
                                            %>
                                            <input type="text" class="form-input" id="number-address" name="streetAddress" placeholder=" " required value="<%= streetAddressValue %>"/>
                                            <label for="number-address" class="form-label">Số nhà, tên đường</label>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-field">
                                <input type="text" class="form-input" id="note" name="note" placeholder=" " value="<%= (cus != null && cus.getNote() != null) ? cus.getNote() : "" %>"/>
                                <label for="note" class="form-label">Lưu ý, yêu cầu khác (Không bắt buộc)</label>
                            </div>
                        </div>
                        <div class="container-bottom">
                            <div class="result">
                                <div class="b">
                                    <p>Tạm tính:</p>
                                    <p class="sub-total-price"><%= cart != null ? cart.getTotalPrice() : "0" %> VND</p>
                                </div>
                                <div class="b">
                                    <p>Phí vận chuyển:</p>
                                    <p id="shipping-fee-cus-info">Chọn địa chỉ để tính phí</p>
                                </div>
                                <div class="b">
                                    <p>Tổng tiền (gồm ship):</p>
                                    <p class="total-price-with-shipping-cus-info"><%= cart != null ? cart.getTotalPrice() : "0" %> VND</p>
                                </div>
                            </div>
                            <input type="hidden" name="shippingFee" id="hidden_shipping_fee" value="0"> <%-- Input ẩn này sẽ được JS cập nhật và gửi đi --%>
                            <button class="buy-btn" id="buy-btn-tab2" type="submit" data-tab="payment">TIẾP TỤC</button>
                            <p style="font-size: 12px">
                                Phí vận chuyển sẽ được cập nhật sau khi bạn chọn đầy đủ địa chỉ.
                            </p>
                        </div>
                    </div>
                </form>
            </div>

            <%-- ======================= MODIFIED PAYMENT TAB ======================= --%>
            <form method="POST" action="${pageContext.request.contextPath}/PayCartController" id="payment-form">
                <input type="hidden" name="finalShippingFee" id="final_shipping_fee_payment_form" value="<%= initialShippingFee %>">
                <input type="hidden" name="finalTotalAmount" id="final_total_amount_payment_form" value="<%= initialTotalWithShipping %>">

                <div class="tab-content" id="payment" style="<%= (cus == null && !"#payment".equals(request.getParameter("tab"))) ? "display: none;" : "" %>">
                    <div class="container-top">
                        <div class="section order-info">
                            <div class="order-info">
                                <h2>Thông tin đặt hàng</h2>
                                <p>
                                    <span>Khách hàng:</span>
                                    <span class="customer-name"><%= (cus != null && cus.getCusName() != null) ? cus.getCusName() : "N/A" %></span>
                                </p>
                                <p>
                                    <span>Số điện thoại:</span>
                                    <span class="customer-phone"><%= (cus != null && cus.getPhone() != null) ? cus.getPhone() : "N/A" %></span>
                                </p>
                                <p>
                                    <span>Địa chỉ nhận hàng:</span>
                                    <span class="customer-address"><%= (cus != null && cus.getAddress() != null) ? cus.getAddress() : "Chưa cập nhật" %></span>
                                </p>
                                <p>
                                    <span>Tạm tính:</span>
                                    <span class="highlight sub-total-amount">
                                        <fmt:formatNumber value="<%= cartSubtotalForJs %>" type="currency" currencySymbol="" minFractionDigits="0" maxFractionDigits="0"/> VND
                                    </span>
                                </p>
                                <p>
                                    <span>Phí vận chuyển:</span>
                                    <span class="highlight shipping-fee" >
                                        <% if (initialShippingFee > 0) { %>
                                            <fmt:formatNumber value="<%= initialShippingFee %>" type="currency" currencySymbol="" minFractionDigits="0" maxFractionDigits="0"/> VND
                                        <% } else if (initialShippingFee == 0 && cus != null && cus.getAddress() != null && !cus.getAddress().isEmpty()) { %>
                                            Miễn phí
                                        <% } else { %>
                                            Chưa tính
                                        <% } %>
                                    </span>
                                </p>
                                <p>
                                    <span>Tổng tiền (gồm ship):</span>
                                    <span class="highlight total-amount-with-shipping" >
                                        <fmt:formatNumber value="<%= initialTotalWithShipping %>" type="currency" currencySymbol="" minFractionDigits="0" maxFractionDigits="0"/> VND
                                    </span>
                                </p>
                            </div>

                            <div class="gift-section">
                                <h3>Quà tặng:</h3>
                                <%
                                    if (cart != null && cart.getItems() != null) {
                                        boolean hasGift = false;
                                        for (CartItem item : cart.getItems()) {
                                            if (item.getGiftName() != null && !item.getGiftName().isEmpty()) {
                                                hasGift = true;
                                %>
                                <p style="font-size: 11px">- <%= item.getGiftName() %> (Sản phẩm: <%= item.getProductName() %>)</p>
                                <%
                                        }
                                    }
                                    if (!hasGift) { %><p style="font-size: 11px">Không có quà tặng kèm theo.</p><% }
                            } else { %><p style="font-size: 11px">Không có quà tặng kèm theo.</p><% }
                            %>
                            </div>
                        </div>
                        <hr />
                        <div class="discount-code">
                            <div class="form-field">
                                <input
                                        type="text"
                                        class="form-input"
                                        id="discount-code-cus"
                                        name="discountCode"
                                        placeholder=" "
                                />
                                <label for="discount-code-cus" class="form-label">Mã giảm giá</label>
                            </div>
                            <button type="button">Áp dụng mã</button>
                        </div>
                        <hr />
                        <div class="payment-method">
                            <h2>Chọn hình thức thanh toán</h2>
                            <div class="option">
                                <label for="cod">
                                    <input type="radio" id="cod" name="paymentMethod" value="COD" checked />
                                    <i class="fas fa-box"></i> Thanh toán khi giao hàng (COD)
                                </label>

                                <label>
                                    <input type="radio" name="paymentMethod" value="vnpay">
                                    <i class="fas fa-credit-card"></i> Thanh toán qua VNPAY
                                </label>

                                <label>
                                    <input type="radio" name="paymentMethod" value="momo">
                                    <i class="fas fa-mobile-alt"></i> Thanh toán qua MoMo
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="container-bottom">
                        <div class="result">
                            <div class="b">
                                <p>Tạm tính:</p>
                                <p class="sub-total-price-summary">
                                    <fmt:formatNumber value="<%= cartSubtotalForJs %>" type="currency" currencySymbol="" minFractionDigits="0" maxFractionDigits="0"/> VND
                                </p>
                            </div>
                            <div class="b">
                                <p>Phí vận chuyển:</p>
                                <p>
                                    <% if (initialShippingFee > 0) { %>
                                    <fmt:formatNumber value="<%= initialShippingFee %>" type="currency" currencySymbol="" minFractionDigits="0" maxFractionDigits="0"/> VND
                                    <% } else if (initialShippingFee == 0 && cus != null && cus.getAddress() != null && !cus.getAddress().isEmpty()) { %>
                                    Miễn phí
                                    <% } else { %>
                                    Chưa tính
                                    <% } %>
                                </p>
                            </div>
                            <div class="b">
                                <p>Tổng tiền (gồm ship):</p>
                                <p class="total-price-with-shipping-summary">
                                    <fmt:formatNumber value="<%= initialTotalWithShipping %>" type="currency" currencySymbol="" minFractionDigits="0" maxFractionDigits="0"/> VND
                                </p>
                            </div>
                        </div>
                        <button class="buy-btn" type="submit">ĐẶT HÀNG NGAY</button>
                    </div>
                </div>
            </form>
            <%-- ======================= END OF MODIFIED PAYMENT TAB ======================= --%>

            <div class="tab-content" id="finish" style="padding: 70px 0">
                <section style="margin-top: 50px; text-align: center;">
                    <div>
                        <img src="https://daphongthuyvn.com/files/assets/dat-hang-thanh-cong.jpg" <%-- Nên dùng ${pageContext.request.contextPath}/assets/img/... --%>
                             alt="Transaction Status" style="max-width: 200px; margin-bottom: 20px;"
                        >
                    </div>

                    <c:if test="${not empty sessionScope.transResult && sessionScope.transResult == true}">
                        <div>
                            <h3 style="font-weight: bold; color: #28a745;">
                                Bạn đã đặt hàng thành công!
                                <i class="fas fa-check-circle"></i>
                            </h3>
                            <p style="font-size: 18px; margin-top: 15px;">Vui lòng để ý điện thoại, nhân viên sẽ sớm liên hệ để xác nhận đơn hàng.</p>
                            <p style="font-size: 18px; margin-top: 5px;">Số điện thoại tư vấn (nếu cần):
                                <strong style="color: red; font-size: 20px;">0383459560</strong>
                            </p>
                            <a href="${pageContext.request.contextPath}/home" class="back-to-shopping-btn" style="margin-top: 20px;">Tiếp tục mua sắm</a>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.transResult && sessionScope.transResult == false}">
                        <div>
                            <h3 style="font-weight: bold; color: #dc3545;">
                                Đặt hàng thất bại!
                            </h3>
                            <p style="font-size: 18px; margin-top: 15px;">Đã có lỗi xảy ra trong quá trình đặt hàng. Vui lòng thử lại hoặc liên hệ với chúng tôi.</p>
                            <p style="font-size: 18px;">Liên hệ tổng đài để được tư vấn:
                                <strong style="color: red; font-size: 20px;">0383456xxx</strong>
                            </p>
                            <a href="${pageContext.request.contextPath}/cart#payment" class="back-to-shopping-btn" style="margin-top: 20px; background-color: #ffc107; color: #333;">Thử lại thanh toán</a>
                        </div>
                    </c:if>

                    <c:if test="${empty sessionScope.transResult && request.getParameter('orderSuccess') == 'true'}">
                        <div>
                            <h3 style="font-weight: bold; color: #28a745;">
                                Bạn đã đặt hàng thành công!
                                <i class="fas fa-check-circle"></i>
                            </h3>
                            <p style="font-size: 18px; margin-top: 15px;">Chúng tôi đã tiếp nhận đơn hàng và sẽ sớm xử lý.</p>
                            <p style="font-size: 18px; margin-top: 5px;">Vui lòng để ý điện thoại, nhân viên sẽ sớm liên hệ để xác nhận đơn hàng.</p>
                            <p style="font-size: 18px;">Số điện thoại tư vấn (nếu cần):
                                <strong style="color: red; font-size: 20px;">0383459560</strong>
                            </p>
                            <a href="${pageContext.request.contextPath}/home" class="back-to-shopping-btn" style="margin-top: 20px;">Tiếp tục mua sắm</a>
                        </div>
                    </c:if>
                    <%-- <% session.removeAttribute("transResult"); %> --%>
                </section>
            </div>
        </div>
    </div>
    <jsp:include page="../reuse/footer.jsp" />
</div>
<script>
    window.cartSubtotal = <%= cartSubtotalForJs %>;
    // Đảm bảo thư viện Gson đã được thêm vào project của bạn
    // và Customer.java có getter/setter cho shippingFee
    window.customerInfo = <%= (cus != null) ? new com.google.gson.Gson().toJson(cus) : "null" %>;
</script>
<script src="${pageContext.request.contextPath}/assets/Js/cart.js?v=${System.currentTimeMillis()}"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/notiflix/dist/notiflix-3.2.6.min.css" />
<script src="https://cdn.jsdelivr.net/npm/notiflix/dist/notiflix-3.2.6.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</body>
</html>