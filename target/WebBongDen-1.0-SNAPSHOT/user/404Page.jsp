<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>404 - Không tìm thấy trang</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: "Poppins", sans-serif;
            background: #f2f2f2;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }

        .container {
            text-align: center;
            padding: 40px;
            max-width: 500px;
        }

        .container img {
            max-width: 100%;
            height: auto;
            margin-bottom: 30px;
        }

        .container h1 {
            font-size: 80px;
            margin: 0;
            color: #ff6b6b;
        }

        .container h2 {
            font-size: 24px;
            color: #333;
            margin: 10px 0;
        }

        .container p {
            font-size: 16px;
            color: #777;
            margin-bottom: 30px;
        }

        .btn-home {
            display: inline-block;
            padding: 12px 24px;
            background-color: #4CAF50;
            color: white;
            border: none;
            text-decoration: none;
            font-size: 16px;
            border-radius: 8px;
            transition: background-color 0.3s ease;
        }

        .btn-home:hover {
            background-color: #388e3c;
        }
    </style>
</head>
<body>
<div class="container">
    <img src="https://storyset.com/images/404/12.svg" alt="404 image" />
    <h2>Trang không tồn tại</h2>
    <p>Xin lỗi! Trang bạn tìm không tồn tại hoặc đã bị xoá.</p>
    <a href="<%= request.getContextPath() %>/home" class="btn-home">⬅ Quay về Trang Chủ</a>
</div>
</body>
</html>
