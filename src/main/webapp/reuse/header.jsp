<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.example.webbongden.dao.model.Cart" %>
<%
    HttpSession httpSession  = request.getSession(false); // Lấy session hiện tại (nếu có)
    String username = (String) httpSession.getAttribute("username");
    Cart cart = (Cart) httpSession.getAttribute("cart");
    int totalQuantity = 0;

    if (cart != null) {
        totalQuantity = cart.getTotalQuantity(); // Giả sử bạn đã có phương thức getTotalQuantity() trong lớp Cart
    }
%>
<header class="header">
    <div class="header-top">
        <div class="header-left">
            <div style="display: flex; align-items: center">
                <a href="/WebBongDen_war/home" class="logo">
                    <img src="./assets/img/logo2.png" alt="Description">
                </a>
            </div>
            <div class="search-bar" style="position: relative;">
                <form action="/WebBongDen_war/search" method="GET" id="search-form" autocomplete="off">
                    <input
                            placeholder="Bạn cần tìm gì?"
                            type="text"
                            name="value"
                            id="search-input"
                            autocomplete="off"
                    />
                    <button type="submit" aria-label="Search" class="search-btn">
                        <i class="fas fa-search"></i>
                    </button>
                </form>
                <!-- Thẻ chứa kết quả autocomplete -->
                <div id="results" style="
                  position: absolute;
                  top: 100%;
                  left: 0;
                  right: 0;
                  background: white;
                  border: 1px solid #ccc;
                  max-height: 300px;
                  overflow-y: auto;
                  display: none;
                  z-index: 9999;
                ">
                </div>
            </div>

        </div>

        <div class="header-right">
            <div class="info">
                <div class="info-item">
                    <i class="fas fa-headphones-alt"></i>
                    <span>Hotline 1900.5301</span>
                </div>

                <a class="info-item" href="support.html">
                    <i class="fa-solid fa-circle-question"></i>
                    <span>Hỗ trợ</span>
                </a>

                <a class="info-item" href="/WebBongDen_war/cart">
                    <i class="fas fa-shopping-cart"></i>
                    <span>Giỏ hàng</span>
                    <p>(<span class="quantity-product"><%= totalQuantity %></span>)</p>
                </a>

                <%
                    if (username != null) {
                %>
                <div class="header-user" style="display:block;">
                    <img src="${sessionScope.avatar != null ? sessionScope.avatar : 'images/default-avatar.png'}"
                         alt="Avatar" class="avt-user" />


                    <div class="user-info-dropdown">
                        <div class="dropdown-header">
                            <img src="./img/icon-dropdownuser.png" alt="" />
                            <p>Xin chào <span><%= username != null ? username : "Khách" %>.</span></p>
                        </div>
                        <div class="dropdown-content">
                            <div class="dropdown-item">
                                <i class="fa fa-user"></i>
                                <a href="/WebBongDen_war/userinfo">Thông tin cá nhân</a>
                            </div>

                            <div class="dropdown-item">
                                <i class="fa-regular fa-eye"></i>
                                <a href="/WebBongDen_war/userinfo">Đơn hàng gần đây</a>
                            </div>
                        </div>
                        <div class="dropdown-footer">
                            <i class="fa-solid fa-arrow-right-from-bracket"></i>
                            <a href="/WebBongDen_war/LogoutController">Đăng xuất</a>
                        </div>
                    </div>
                </div>
                <%
                } else { // Người dùng chưa đăng nhập
                %>
                <a href="/WebBongDen_war/login" id="login-header-btn" style="display: block">
                    <button class="login" id="login-btn">
                        <i class="fas fa-user"></i>
                        <span>Đăng nhập</span>
                    </button>
                </a>
                <%
                    }
                %>
            </div>
        </div>
    </div>

    <nav class="navbar">
        <ul class="navbar-list">
            <li><a href="/WebBongDen_war/home">TRANG CHỦ</a></li>
            <li class="dropdown">
                <a href="/WebBongDen_war/CategoryController"
                >DANH MỤC
                    <i class="fa-solid fa-caret-down"></i>
                </a>
                <div class="submenu">
                    <ul id="category-list">
                        <c:forEach var="category" items="${categories}">
                            <li class="category">
                                <p class="category-header">${category.categoryName}</p>
                                <ul class="category-products">
                                    <!-- Hiển thị danh mục con -->
                                    <c:forEach var="subCategory" items="${subCategoriesMap[category.id]}">
                                        <li class="category-item">
                                            <a href="/WebBongDen_war/CategoryController?categoryId=${category.id}&subCategoryId=${subCategory.id}" class="category-link">${subCategory.name}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </li>
            <li><a href="News.html">TIN TỨC</a></li>
            <li><a href="/WebBongDen_war/ContactController">LIÊN HỆ</a></li>
        </ul>
    </nav>
</header>
