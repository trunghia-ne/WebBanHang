<%@ page import="com.example.webbongden.dao.model.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="./img/logo-fold.png" sizes="180x180" />
    <title>Thông tin khách hàng</title>
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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/header-footer.css?v=2.1">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/user.css?v=2">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
</head>
<style>
    .info-btn button {
        width: 120px;
        padding: 5px 0;
        background-color: red;
        color: white;
        border-radius: 4px;
    }

    .customer-info img {
        width: 60px;
        height: 60px;
        object-fit: cover;
        border-radius: 50%;
        transition: 0.3s;
    }
    .customer-info img:hover {
        opacity: 0.8;
    }
    /* Style tổng thể cho bảng ordersTable */
    #ordersTable {
        border-collapse: separate !important;
        border-spacing: 0 10px !important; /* khoảng cách giữa các hàng */
        width: 100%;
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 4px 12px rgb(0 0 0 / 0.1);
        background-color: #fff;
    }

    /* Header bảng */
    #ordersTable thead tr {
        background-color: white; /* nền trắng */
        color: black; /* chữ đen */
        font-weight: 600;
        border-radius: 10px;
    }


    #ordersTable thead th {
        padding: 12px 20px;
        text-align: left;
    }

    /* Các hàng dữ liệu */
    #ordersTable tbody tr {
        background-color: #f9f9f9;
        border-radius: 8px;
        transition: background-color 0.3s ease;
    }

    #ordersTable tbody tr:hover {
        background-color: #e9f1ff;
    }

    #ordersTable tbody td {
        padding: 14px 20px;
        vertical-align: middle;
    }

    /* Căn giữa cột Id và Tổng tiền */
    #ordersTable tbody td:nth-child(1),
    #ordersTable tbody td:nth-child(3) {
        text-align: center;
    }

    .modal {
        position: fixed;
        z-index: 9999;
        left: 0; top: 0; width: 100%; height: 100%;
        background-color: rgba(0,0,0,0.4);
        overflow: auto;
    }
    .modal-content tr, .modal-content th, .modal-content td {
        border: 1px solid #ccc;
        padding: 8px;
        text-align: center;
    }

    /* Căn trái các cột trong bảng */
    #ordersTable td, #ordersTable th {
        text-align: left !important;  /* Căn trái tất cả các cột */
    }

    /* Định dạng nút Chi tiết và Hủy */
    button.btn-detail, button.btn-cancel, button.btn-edit {
        background-color: #007bff;  /* Màu nền cho nút Chi tiết */
        color: white;
        border: none;
        padding: 6px 12px;
        font-size: 14px;
        cursor: pointer;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        border-radius: 4px;
        margin-right: 5px;
    }

    button.btn-cancel {
        background-color: #dc3545;  /* Màu nền cho nút Hủy */
    }

    button.btn-edit {
        background-color: #007bff;
    }

    button.btn-detail:hover, button.btn-cancel:hover {
        opacity: 0.8;  /* Làm mờ nút khi hover */
    }

    /* Định dạng icon trong nút */
    button i {
        margin-right: 5px;
    }

    #ordersTable_wrapper {
        width: 100%;
        overflow-x: auto;  /* Allow horizontal scroll */
        display: block; /* Ensures it is scrollable */
    }

    /* Ensure the table itself doesn't exceed its container */
    #ordersTable {
        width: 100%;
        table-layout: auto;  /* Helps in expanding content properly */
    }

    /* Prevent text wrapping in the table cells */
    #ordersTable th, #ordersTable td {
        white-space: nowrap;
    }

    /* Optional: Style the button group */
    #ordersTable td button {
        margin-right: 10px;
        display: inline-block;
    }

</style>
<body>
<div class="wrapper">
    <jsp:include page="../reuse/header.jsp" />
    <div class="main">
        <div class="container container-cus">
            <div class="sidebar-info-customer">
                <div class="customer-info">
                    <form id="avatarForm" enctype="multipart/form-data" method="post">
                        <label for="avatarInput" title="Click để thay đổi ảnh đại diện" style="cursor: pointer;">
                            <img
                                    id="avatarPreview"
                                    src="${userInfo.avatar != null ? userInfo.avatar : 'https://static.vecteezy.com/system/resources/previews/018/765/757/original/user-profile-icon-in-flat-style-member-avatar-illustration-on-isolated-background-human-permission-sign-business-concept-vector.jpg'}"
                                    alt="avatarUser"
                            />
                        </label>
                        <input type="file" id="avatarInput" name="avatar" accept="image/*" style="display: none;" />
                    </form>
                    <p class="customer-name">${userInfo.customerName}</p>
                </div>

                <div class="menu-user">
                    <nav>
                        <ul class="menu-user">
                            <li
                                    data-section="information_account"
                                    class="active"
                                    onclick="showContent('information_account')"
                            >
                                <i class="fa-solid fa-user"></i>
                                <a href="javascript:void(0)">Thông tin tài khoản</a>
                            </li>
                            <li data-section="order" onclick="showContent('order')">
                                <i class="fa-solid fa-bars-progress"></i>
                                <a href="javascript:void(0)">Quản lý đơn hàng</a>
                            </li>
                            <li
                                    data-section="change_password"
                                    onclick="showContent('change_password')"
                            >
                                <i class="fa-solid fa-lock"></i>
                                <a href="javascript:void(0)">Đổi mật khẩu</a>
                            </li>
                            <li>
                                <i class="fa-solid fa-right-from-bracket"></i>
                                <a href="/WebBongDen_war/LogoutController" id="logoutLink">Đăng xuất</a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
            <div class="content">
                <!-- Thông tin tài khoản -->
                <div
                        id="information_account"
                        class="content_section"
                >
                    <div class="information_header" id="userInfo" data-customer-id="${userInfo.customerId}">
                        <h2>THÔNG TIN TÀI KHOẢN</h2>
                    </div>
                    <form class="info-form">
                        <div class="user-name dlex">
                            <label for="username">Họ tên:</label>
                            <div>
                                <input type="text" id="username" name="username" required readonly value="${userInfo.customerName}"/>
                            </div>
                        </div>

                        <div class="email-cus dlex">
                            <label for="email">Email:</label>
                            <div>
                                <input type="text" id="email" name="email" value="${userInfo.email}" readonly/>
                            </div>
                        </div>

                        <div class="phone-cus dlex">
                            <label for="phone">Số điện thoại:</label>
                            <div>
                                <input type="tel" id="phone" name="phone" value="${userInfo.phone}" readonly/>
                            </div>
                        </div>

                        <div class="phone-cus dlex">
                            <label for="address">Địa chỉ:</label>
                            <div>
                                <input type="text" id="address" name="address" value="${userInfo.address}" readonly/>
                            </div>
                        </div>

                        <div class="create-date-cus dlex">
                            <label for="create-date">Ngày tạo:</label>
                            <div>
                                <input type="text" id="create-date" name="create-date" value="${userInfo.createdAt}" readonly/>
                            </div>
                        </div>


                        <div class="info-btn">

                            <button type="submit" id="save-info">Lưu</button>



                            <button type="button" id="edit-info">Sửa thông tin</button>

                        </div>
                    </form>
                    <p id="saveMessage" style="display: none; color: green">
                        Hồ sơ của bạn đã được lưu!
                    </p>
                </div>
                <!-- Quản lý đơn hàng -->
                <div id="order" class="content_section" style="display: none;">
                    <div class="order_header">
                        <h2>ĐƠN HÀNG ĐÃ ĐẶT</h2>
                    </div>

                    <div id="ordersTable_wrapper">
                        <table id="ordersTable" class="display" style="width:100%">
                            <thead>
                            <tr>
                                <th>Id</th>
                                <th>Địa chỉ</th>
                                <th>SDT</th>
                                <th>Ngày đặt</th>
                                <th>Tổng tiền</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                            </thead>
                            <tbody>
                            <!-- Dữ liệu sẽ được load tự động -->
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- đổi mật khẩu -->
                <div
                        id="change_password"
                        class="content_section"
                        style="display: none"
                >
                    <div class="change_password_header">
                        <h1>ĐỔI MẬT KHẨU</h1>
                        <div class="title">
                            Để bảo mật tài khoản, vui lòng không chia sẻ mật khẩu cho
                            người khác
                        </div>
                    </div>
                    <form class="change_password_form" action="changePassword" method="POST">
                        <div class="dlex">
                            <label for="oldPassword">Mật khẩu cũ:</label>
                            <div>
                                <input type="password" id="oldPassword" name="oldPassword" placeholder="Nhập mật khẩu hiện tại" required />
                            </div>
                        </div>

                        <div class="dlex">
                            <label for="newPassword">Mật khẩu mới:</label>
                            <div>
                                <input type="password" id="newPassword" name="newPassword" placeholder="Nhập mật khẩu mới" required />
                            </div>
                        </div>

                        <div class="dlex">
                            <label for="confirm_password">Nhập lại mật khẩu:</label>
                            <div>
                                <input type="password" id="confirm_password" name="confirmPassword" placeholder="Nhập lại mật khẩu mới" required />
                            </div>
                        </div>
                        <button class="submit" type="submit">Xác nhận</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../reuse/footer.jsp" />
</div>
</body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
<script src="https://cdn.jsdelivr.net/npm/just-validate@latest/dist/just-validate.production.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/assets/Js/user.js?v=${System.currentTimeMillis()}"></script>

<!-- DataTables JS -->
<script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
</html>