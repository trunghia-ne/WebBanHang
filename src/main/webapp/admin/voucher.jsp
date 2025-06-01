<%--
  Created by IntelliJ IDEA.
  User: daodu
  Date: 5/29/2025
  Time: 5:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="./img/logo-fold.png" sizes="180x180" />
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

    <title>Admin</title>
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
    <link
            rel="stylesheet"
            href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css"
    />

    <link rel="stylesheet" href="https://cdn.datatables.net/buttons/3.2.2/css/buttons.dataTables.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/admin.css?v=1">
</head>
<style>
    /* Bảng dữ liệu voucher */
    .voucher-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
        font-size: 15px;
        border: 1px solid #ccc;
    }

    .voucher-table th, .voucher-table td {
        padding: 10px 12px;
        border: 1px solid #ddd;
        vertical-align: middle;
        text-align: left;
    }

    .voucher-table thead {
        background-color: #f2f2f2;
    }

    .voucher-table thead th {
        font-weight: bold;
        text-transform: uppercase;
        font-size: 14px;
    }

    .voucher-table tbody tr:nth-child(even) {
        background-color: #fafafa;
    }

    .voucher-table tbody tr:nth-child(odd) {
        background-color: #fff;
    }

    .voucher-table tbody tr:hover {
        background-color: #f0f8ff;
        transition: background-color 0.3s ease;
    }

    /* Nút hành động */
    .btn {
        padding: 6px 10px;
        font-size: 13px;
        border-radius: 4px;
        border: none;
        cursor: pointer;
    }

    .btn-danger {
        background-color: #e74c3c;
        color: #fff;
    }

    .btn-danger:hover {
        background-color: #c0392b;
    }

    .btn-edit {
        background-color: #3498db;
        color: white;
        margin-right: 6px;
    }

    .btn-edit:hover {
        background-color: #2980b9;
    }

    /* Tiêu đề khu vực quản lý */

    .voucher-container h3 {
        font-size: 18px;
        margin: 20px 0 20px;
    }

    .form-group {
        margin-bottom: 15px;
    }

    .form-group label {
        display: block;
        font-weight: 500;
        margin-bottom: 5px;
    }

    .form-group input, .form-group select {
        width: 100%;
        padding: 8px 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

    .form-group button {
        padding: 8px 16px;
        font-size: 14px;
        background-color: #27ae60;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .form-group button:hover {
        background-color: #1e8449;
        transition: 0.3s;
    }

    div#editModal{
        top: 10% !important;
        width: 40%;
    }
</style>

<body>
<div class="wrapper">
    <%@ include file="header.jsp" %>

    <div class="main">
        <%@ include file="sidebar.jsp" %>

        <div class="main-content">
            <div class="tab-content" id="voucher-content">
                <div class="voucher-container">
                    <!-- Khu vực thêm voucher -->
                    <div class="add-voucher-area">
                        <h3>THÊM MÃ GIẢM GIÁ</h3>
                        <form action="AddVoucherController" method="post" id="voucher-form">
                            <div class="form-group">
                                <label for="code">Mã Voucher:</label>
                                <input type="text" id="code" name="code" required />
                            </div>
                            <div class="form-group">
                                <label for="discountType">Loại giảm giá:</label>
                                <select id="discountType" name="discountType" required>
                                    <option value="percent">Phần trăm</option>
                                    <option value="amount">Số tiền</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="discountValue">Giá trị giảm:</label>
                                <input type="number" id="discountValue" name="discountValue" required />
                            </div>
                            <div class="form-group">
                                <label for="startDate">Ngày bắt đầu:</label>
                                <input type="date" id="startDate" name="startDate" required />
                            </div>
                            <div class="form-group">
                                <label for="endDate">Ngày kết thúc:</label>
                                <input type="date" id="endDate" name="endDate" required />
                            </div>
                            <div class="form-group">
                                <label for="minOrderValue">Giá trị đơn hàng tối thiểu:</label>
                                <input type="number" id="minOrderValue" name="minOrderValue" />
                            </div>
                            <div class="form-group">
                                <label for="usageLimit">Số lần sử dụng:</label>
                                <input type="number" id="usageLimit" name="usageLimit" required />
                            </div>
                            <div class="form-group">
                                <button type="submit">Thêm Voucher</button>
                            </div>
                        </form>
                    </div>

                    <!-- Danh sách voucher -->
                    <div class="voucher-list">
                        <h3>DANH SÁCH MÃ GIẢM GIÁ</h3>
                        <table id="voucher-table">
                            <thead>
                            <tr>
                                <th>Mã</th>
                                <th>Loại</th>
                                <th>Giá trị</th>
                                <th>Ngày bắt đầu</th>
                                <th>Ngày kết thúc</th>
                                <th>Đơn tối thiểu</th>
                                <th>Giới hạn</th>
                                <th>Đã dùng</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%-- Lặp danh sách từ Servlet Controller --%>
                            <c:forEach var="voucher" items="${vouchers}">
                                <tr>
                                    <td>${voucher.code}</td>
                                    <td>${voucher.discountType}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${voucher.discountType == 'percent'}">
                                                ${voucher.discountValue}%
                                            </c:when>
                                            <c:when test="${voucher.discountType == 'amount'}">
                                                <fmt:formatNumber value="${voucher.discountValue}" type="currency" currencySymbol="₫" groupingUsed="true" />
                                            </c:when>
                                            <c:otherwise>
                                                ${voucher.discountValue}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${voucher.startDate}</td>
                                    <td>${voucher.endDate}</td>
                                    <td><fmt:formatNumber value="${voucher.minOrderValue}" type="currency" currencySymbol="₫" />
                                    </td>
                                    <td>${voucher.usageLimit}</td>
                                    <td>${voucher.usedCount}</td>
                                    <td>${voucher.status}</td>
                                    <td>
                                        <button class="btn btn-danger btn-delete" data-id="${voucher.id}">Xóa</button>
                                        <button type="button" class="btn btn-edit" onclick="editVoucher(${voucher.id}, '${voucher.code}', '${voucher.discountType}', '${voucher.discountValue}', '${voucher.startDate}', '${voucher.endDate}', '${voucher.minOrderValue}', '${voucher.usageLimit}', '${voucher.status}')">Sửa</button>
                                    </td>

                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!-- MODAL CẬP NHẬT -->
        <div id="editModal" style="display:none; position:fixed; top:20%; left:50%; transform:translateX(-50%); background:white; padding:20px; border:1px solid #ccc; z-index:999;">
            <h3>Cập nhật Voucher</h3>
            <form id="edit-form">
                <input type="hidden" name="id" id="edit-id">
                <div class="form-group">
                    <label>Mã:</label>
                    <input type="text" name="code" id="edit-code" required />
                </div>
                <div class="form-group">
                    <label>Loại:</label>
                    <select name="discountType" id="edit-discountType">
                        <option value="percent">Phần trăm</option>
                        <option value="amount">Số tiền</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Giá trị:</label>
                    <input type="number" name="discountValue" id="edit-discountValue" required />
                </div>
                <div class="form-group">
                    <label>Bắt đầu:</label>
                    <input type="date" name="startDate" id="edit-startDate" required />
                </div>
                <div class="form-group">
                    <label>Kết thúc:</label>
                    <input type="date" name="endDate" id="edit-endDate" required />
                </div>
                <div class="form-group">
                    <label>Đơn tối thiểu:</label>
                    <input type="number" name="minOrderValue" id="edit-minOrderValue" />
                </div>
                <div class="form-group">
                    <label>Số lần dùng:</label>
                    <input type="number" name="usageLimit" id="edit-usageLimit" required />
                </div>
                <div class="form-group">
                    <label>Trạng thái:</label>
                    <select name="status" id="edit-status">
                        <option value="active">Kích hoạt</option>
                        <option value="inactive">Ngừng</option>
                    </select>
                </div>
                <div class="form-group">
                    <button type="submit">Cập nhật</button>
                    <button type="button" onclick="$('#editModal').hide()">Hủy</button>
                </div>
            </form>
        </div>

    </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>

<!-- DataTables Buttons Extension -->
<script src="https://cdn.datatables.net/buttons/2.3.6/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.3.6/js/buttons.html5.min.js"></script>

<!-- pdfmake (hỗ trợ xuất PDF) -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>

<!-- JSZip (hỗ trợ xuất Excel) -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>

<script src="${pageContext.request.contextPath}/admin/admin_js/noti.js?v=${System.currentTimeMillis()}" defer></script>


<script>
    $(document).ready(function() {

        // Gọi load danh sách voucher ngay khi trang load xong
        loadVoucherList();

        $('#voucher-form').submit(function(e) {
            e.preventDefault();
            var formData = $(this).serialize();

            $.ajax({
                url: 'AddVoucherController',
                method: 'POST',
                data: formData,
                success: function(response) {
                    loadVoucherList(); // load lại danh sách sau khi thêm
                    $('#voucher-form')[0].reset();

                    Swal.fire({
                        icon: 'success',
                        title: 'Thêm voucher thành công!',
                        showConfirmButton: false,
                        timer: 1500
                    });
                },
                error: function(xhr, status, error) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Có lỗi xảy ra khi thêm voucher',
                        text: xhr.responseText || error,
                    });
                }
            });
        });

        // Xử lý sự kiện xóa voucher bằng Ajax
        $(document).on('click', '.btn-delete', function () {
            const id = $(this).data('id');

            Swal.fire({
                title: 'Bạn chắc chắn muốn xóa?',
                text: "Hành động này không thể hoàn tác!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#3085d6',
                confirmButtonText: 'Xóa',
                cancelButtonText: 'Hủy'
            }).then((result) => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: 'DeleteVoucherController',
                        method: 'POST',
                        data: { id: id },
                        success: function () {
                            loadVoucherList();
                            Swal.fire('Đã xóa!', 'Voucher đã được xóa.', 'success');
                        },
                        error: function (xhr) {
                            Swal.fire('Lỗi!', xhr.responseText, 'error');
                        }
                    });
                }
            });
        });

    });

    // Hàm load lại danh sách voucher bằng ajax GET
    function loadVoucherList() {
        $.ajax({
            url: 'AddVoucherController',
            method: 'GET',
            success: function(html) {
                var newTbody = $(html).find('#voucher-table tbody').html();
                $('#voucher-table tbody').html(newTbody);
            },
            error: function() {
                Swal.fire({
                    icon: 'error',
                    title: 'Không thể tải lại danh sách voucher',
                });
            }
        });
    }

    window.editVoucher = function(id, code, discountType, discountValue, startDate, endDate, minOrderValue, usageLimit, status) {
        $('#edit-id').val(id);
        $('#edit-code').val(code);
        $('#edit-discountType').val(discountType);
        $('#edit-discountValue').val(discountValue);
        $('#edit-startDate').val(startDate);
        $('#edit-endDate').val(endDate);
        $('#edit-minOrderValue').val(minOrderValue);
        $('#edit-usageLimit').val(usageLimit);
        $('#edit-status').val(status);
        $('#editModal').show();
    };

    $('#edit-form').submit(function(e) {
        e.preventDefault();
        var formData = $(this).serialize();

        $.ajax({
            url: 'EditVoucherController',
            method: 'POST',
            data: formData,
            success: function() {
                $('#editModal').hide();
                loadVoucherList();
                Swal.fire('Thành công', 'Đã cập nhật voucher!', 'success');
            },
            error: function(xhr) {
                Swal.fire('Lỗi', xhr.responseText, 'error');
            }
        });
    });

</script>
</body>
</html>
