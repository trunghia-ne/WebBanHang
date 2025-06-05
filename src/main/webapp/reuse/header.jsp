<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.example.webbongden.dao.model.Cart" %>
<%
    HttpSession httpSession = request.getSession(false);
    String username = null;
    if (httpSession != null) {
        username = (String) httpSession.getAttribute("username");
    }
    Cart cart = (httpSession != null) ? (Cart) httpSession.getAttribute("cart") : null;
    int totalQuantity = 0;

    if (cart != null) {
        totalQuantity = cart.getTotalQuantity();
    }
%>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<header class="header">
    <div class="header-top">
        <div class="header-left">
            <div style="display: flex; align-items: center">
                <a href="${pageContext.request.contextPath}/home" class="logo">
                    <img src="${pageContext.request.contextPath}/assets/img/logo2.png" alt="Description">
                </a>
            </div>
            <div class="search-bar" style="position: relative;">
                <form action="${pageContext.request.contextPath}/search" method="GET" id="search-form"
                      autocomplete="off">
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

                <a class="info-item" href="${pageContext.request.contextPath}/cart">
                    <i class="fas fa-shopping-cart"></i>
                    <span>Giỏ hàng</span>
                    <p>(<span class="quantity-product"><%= totalQuantity %></span>)</p>
                </a>

                <%
                    if (username != null) {
                %>
                <div class="header-user" style="display:block;">
                    <img src="${userInfo.avatar != null ? userInfo.avatar : 'https://static.vecteezy.com/system/resources/previews/018/765/757/original/user-profile-icon-in-flat-style-member-avatar-illustration-on-isolated-background-human-permission-sign-business-concept-vector.jpg'}"
                         alt="Avatar" class="avt-user"/>


                    <div class="user-info-dropdown">
                        <div class="dropdown-header">
                            <img src="${pageContext.request.contextPath}/img/icon-dropdownuser.png" alt=""/>
                            <p>Xin chào <span>${userInfo.customerName != null ? userInfo.customerName : "Khách"}.</span>
                            </p>
                        </div>
                        <div class="dropdown-content">
                            <div class="dropdown-item">
                                <i class="fa fa-user"></i>
                                <a href="${pageContext.request.contextPath}/userinfo">Thông tin cá nhân</a>
                            </div>

                            <div class="dropdown-item">
                                <i class="fa-regular fa-eye"></i>
                                <a href="${pageContext.request.contextPath}/userinfo">Đơn hàng gần đây</a>
                            </div>
                        </div>
                        <div class="dropdown-footer">
                            <i class="fa-solid fa-arrow-right-from-bracket"></i>
                            <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                        </div>
                    </div>
                </div>
                <%
                } else {
                %>
                <a href="${pageContext.request.contextPath}/login" id="login-header-btn" style="display: block">
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
            <li><a href="${pageContext.request.contextPath}/home">TRANG CHỦ</a></li>
            <li class="dropdown">
                <a href="${pageContext.request.contextPath}/CategoryController"
                >DANH MỤC
                    <i class="fa-solid fa-caret-down"></i>
                </a>
                <div class="submenu">
                    <ul id="category-list">
                        <c:forEach var="category" items="${categories}">
                            <li class="category">
                                <p class="category-header">${category.categoryName}</p>
                                <ul class="category-products">
                                    <c:forEach var="subCategory" items="${subCategoriesMap[category.id]}">
                                        <li class="category-item">
                                            <a href="${pageContext.request.contextPath}/CategoryController?categoryId=${category.id}&subCategoryId=${subCategory.id}"
                                               class="category-link">${subCategory.name}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </li>
            <li><a href="News.html">TIN TỨC</a></li>
            <li><a href="${pageContext.request.contextPath}/ContactController">LIÊN HỆ</a></li>
        </ul>
    </nav>

</header>
<% if (username != null) { %>
<%
    String wsProtocol = request.isSecure() ? "wss://" : "ws://";

    String serverName = request.getServerName();
    int serverPort = request.getServerPort();
    String contextPath = request.getContextPath();
    String socketUrl = wsProtocol + serverName + ":" + serverPort + contextPath + "/ws/notification/" + username;
%>

<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function () {
        const socketUrlFromServer = "<%= socketUrl %>";

        console.log("Attempting to connect to WebSocket at: " + socketUrlFromServer);

        const socket = new WebSocket(socketUrlFromServer);

        socket.onopen = function (event) {
            console.log('WebSocket connection established successfully!');
        };

        socket.onmessage = function (event) {
            console.log('Message from server: ', event.data);
            try {
                const data = JSON.parse(event.data);
                if (data.type === 'FORCE_LOGOUT') {
                    handleForceLogout(data.message);
                }
            } catch (e) {
                console.error('Error parsing message JSON', e);
            }
        };

        socket.onclose = function (event) {
            console.log('WebSocket connection closed. Code: ' + event.code + ', Reason: ' + event.reason);
        };

        socket.onerror = function (error) {
            console.error('WebSocket Error: An error occurred.', error);
        };

        function handleForceLogout(message) {
            Swal.fire({
                title: 'Thông Báo Hệ Thống',
                text: message,
                icon: 'warning',
                confirmButtonText: 'Đã hiểu',
                allowOutsideClick: false,
                allowEscapeKey: false
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = "<%= request.getContextPath() %>/LogoutController";
                }
            });
        }
    });
</script>
<% } %>