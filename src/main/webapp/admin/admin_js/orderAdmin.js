document.addEventListener("DOMContentLoaded", function () {
    $(document).ready(function () {
        const table = $("#order-table").DataTable({
            ajax: {
                url: "/WebBongDen_war/list-order", // URL Servlet trả về JSON
                type: "GET", // Phương thức HTTP
                dataSrc: "", // DataTables sẽ lấy dữ liệu từ gốc JSON
                data: function (d) {
                    d.searchValue = $("#order-search").val(); // Truyền giá trị tìm kiếm
                    d.status = $("#sort-select-order").val(); // Giá trị lọc trạng thái
                },
            },
            error: function (xhr, error, thrown) {
                console.log("Error:", error);
                console.log("Response Text:", xhr.responseText);
            },
            destroy: true, // Đảm bảo bảng được làm mới khi reload
            autoWidth: false,
            paging: true,
            pageLength: 10,
            columns: [
                { data: "id" }, // ID
                { data: "customerName" }, // Tên khách hàng
                { data: "createdAt" }, // Ngày đặt hàng
                {
                    data: "totalPrice", // Tổng tiền
                    render: function (data) {
                        return parseInt(data).toLocaleString("vi-VN", {
                            style: "currency",
                            currency: "VND",
                        });
                    },
                },
                { data: "address" }, // Địa chỉ
                { data: "orderStatus" }, // Trạng thái đơn hàng
                {
                    data: null, // Hóa đơn: Chi tiết
                    render: function (data, type, row) {
                        return `<button class="view-details" data-index="${row.id}">Chi tiết</button>`;
                    },
                },
                {
                    data: null, // Thao tác: Duyệt và Từ chối
                    render: function (data, type, row) {
                        if (row.orderStatus === "Pending") {
                            return `
                            <button class="approve-order" data-index="${row.id}">
                                <i class="fa-solid fa-check"></i>
                            </button>
                            <button class="reject-order" data-index="${row.id}">
                                <i class="fa-solid fa-times"></i>
                            </button>`;
                        } else {
                            return null;
                        }
                    },
                },
            ],
            lengthChange: false,
            searching: false,
            ordering: true,
            info: true,
            language: {
                paginate: {
                    previous: "Trước",
                    next: "Tiếp",
                },
                info: "Hiển thị _START_ đến _END_ của _TOTAL_ sản phẩm",
            },
        });

        $("#search-btn-order").on("click", function () {
            table.ajax.reload();
        });

        // Event delegation for approve button
        $("#order-table").on("click", ".approve-order", function () {
            const orderId = $(this).data("index");
            approveOrder(orderId);
        });

        // Event delegation for reject button
        $("#order-table").on("click", ".reject-order", function () {
            const orderId = $(this).data("index");
            rejectOrder(orderId);
        });


        function approveOrder(orderId) {
            // Hiển thị hộp thoại xác nhận
            Swal.fire({
                title: "Xác nhận phê duyệt đơn hàng?",
                text: "Bạn có chắc chắn muốn phê duyệt đơn hàng này?",
                icon: "question",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Đồng ý",
                cancelButtonText: "Hủy",
            }).then((result) => {
                if (result.isConfirmed) {
                    // Nếu người dùng nhấn Đồng ý
                    $.ajax({
                        url: `/WebBongDen_war/update-order-status`, // URL servlet xử lý
                        type: "POST",
                        contentType: "application/json",
                        data: JSON.stringify({
                            orderId: orderId.toString(),
                            status: "Approved", // Trạng thái mới
                        }),
                        success: function (response) {
                            Swal.fire("Thành công!", "Đơn hàng đã được phê duyệt.", "success");
                            table.ajax.reload(); // Reload lại bảng
                        },
                        error: function (xhr, status, error) {
                            Swal.fire("Lỗi!", "Không thể phê duyệt đơn hàng.", "error");
                            console.error("Error:", xhr.responseText);
                        },
                    });
                }
            });
        }


        function rejectOrder(orderId) {
            // Hiển thị hộp thoại xác nhận
            Swal.fire({
                title: "Xác nhận từ chối đơn hàng?",
                text: "Bạn có chắc chắn muốn từ chối đơn hàng này?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#d33",
                cancelButtonColor: "#3085d6",
                confirmButtonText: "Đồng ý",
                cancelButtonText: "Hủy",
            }).then((result) => {
                if (result.isConfirmed) {
                    // Nếu người dùng nhấn Đồng ý
                    $.ajax({
                        url: `/WebBongDen_war/update-order-status`, // URL servlet xử lý
                        type: "POST",
                        contentType: "application/json",
                        data: JSON.stringify({
                            orderId: orderId.toString(),
                            status: "Rejected", // Trạng thái mới
                        }),
                        success: function (response) {
                            Swal.fire("Thành công!", "Đơn hàng đã bị từ chối.", "success");
                            table.ajax.reload(); // Reload lại bảng
                        },
                        error: function (xhr, status, error) {
                            Swal.fire("Lỗi!", "Không thể từ chối đơn hàng.", "error");
                            console.error("Error:", xhr.responseText);
                        },
                    });
                }
            });
        }

        $("#sort-btn-order").on("click", function () {
            table.ajax.reload(); // Reload lại bảng với dữ liệu lọc mới
        });

        //Code nut xem chi tiet don hang
        $("#order-table").on("click", ".view-details", function () {
            const orderId = $(this).data("index"); // Lấy orderId từ nút
            const rowData = table.row($(this).parents("tr")).data(); // Lấy dữ liệu dòng hiện tại

            // Cập nhật thông tin khách hàng trong overlay
            $("#customer-name").text(rowData.customerName || "N/A");
            $("#customer-address").text(rowData.address || "N/A");
            $("#customer-phone").text(rowData.phone || "N/A");

            // Gửi yêu cầu lấy chi tiết hóa đơn
            $.ajax({
                url: "/WebBongDen_war/order-detail",
                type: "GET",
                data: { orderId: orderId },
                success: function (data) {
                    // Parse dữ liệu từ response
                    const orderDetails = data;
                    const $orderItemsBody = $("#order-items-body");

                    // Xóa nội dung cũ
                    $orderItemsBody.empty();

                    // Duyệt qua danh sách sản phẩm và thêm vào bảng
                    orderDetails.forEach((item) => {
                        $orderItemsBody.append(`
                            <tr>
                                <td>${item.productId}</td>
                                <td>${item.productName}</td>
                                <td>${item.quantity}</td>
                                <td>${parseFloat(item.unitPrice).toLocaleString("vi-VN", {
                            style: "currency",
                            currency: "VND",
                        })}</td>
                                <td>${parseFloat(item.amount).toLocaleString("vi-VN", {
                            style: "currency",
                            currency: "VND",
                        })}</td>
                            </tr>
                        `);
                    });

                    // Tính tổng tiền và cập nhật
                    const totalAmount = orderDetails.reduce((total, item) => total + parseFloat(item.amount), 0);
                    $("#total-amount").text(totalAmount.toLocaleString("vi-VN", {
                        style: "currency",
                        currency: "VND",
                    }));

                    // Hiển thị overlay
                    $(".overlay").addClass("active");
                },
                error: function (xhr, status, error) {
                    alert("Không thể lấy chi tiết đơn hàng. Vui lòng thử lại!");
                    console.error("Error:", xhr.responseText);
                },
            });
        });

        // Đóng overlay
        $("#close-invoice-details").on("click", function () {
            $(".overlay").removeClass("active");
        });
    })
})