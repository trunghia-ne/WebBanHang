<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="./img/logo-fold.png" sizes="180x180" />
    <title>Admin-LogManagement</title>
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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <style>
        .btn-view-icon {
            background-color: #f1f3f5;
            color: #212529;
            border: 1px solid #ced4da;
            padding: 6px 10px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.2s ease;
        }

        .btn-view-icon i {
            font-size: 16px;
            color: #495057;
        }

        .btn-view-icon:hover {
            background-color: #dee2e6;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }

        /* Log Table Style */
        #log-table {
            width: 100%;
            border-collapse: collapse;
            font-family: 'Poppins', sans-serif;
            font-size: 14px;
            background-color: white;
            margin-top: 20px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        }

        /* Table header */
        #log-table thead {
            background-color: #f2f4f7;
            font-weight: 500;
        }

        #log-table th,
        #log-table td {
            padding: 12px 16px;
            border: 1px solid #dee2e6;
            text-align: left;
            vertical-align: middle;
            white-space: normal;
            word-break: break-word;
        }

        /* Alternate row color */
        #log-table tbody tr:nth-child(odd) {
            background-color: #fafbfc;
        }

        /* Hover effect */
        #log-table tbody tr:hover {
            background-color: #eef1f5;
        }

        /* Title (nếu có) */
        .tab-container h2 {
            font-size: 20px;
            font-weight: 700;
            margin-bottom: 15px;
        }

        /* Button style */
        .dt-button {
            background-color: #007bff !important;
            color: #fff !important;
            border: none !important;
            border-radius: 4px !important;
            padding: 8px 12px !important;
            font-size: 14px !important;
            margin-left: 10px !important;
            cursor: pointer !important;
        }

        /* JSON data cell (before/after data) */
        .json-cell {
            text-align: left !important;
            font-family: "Courier New", monospace;
            font-size: 13px;
            max-height: 120px;
            overflow-y: auto;
            white-space: pre-line;
        }

    </style>
</head>
<body>
<div class="wrapper">
    <%@ include file="header.jsp" %>

    <div class="main">
        <%@ include file="sidebar.jsp" %>

        <div class="main-content">
            <div class="tab-content" id="log-management-content">
                <div class="tab-container">
                    <table class="product-table" id="log-table">

                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/admin/admin_js/log.js?v=${System.currentTimeMillis()}" defer></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>

<!-- DataTables Buttons Extension -->
<script src="https://cdn.datatables.net/buttons/2.3.6/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.3.6/js/buttons.html5.min.js"></script>

<!-- pdfmake (hỗ trợ xuất PDF) -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>

<!-- JSZip (hỗ trợ xuất Excel) -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
</body>
</html>
