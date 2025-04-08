document.addEventListener("DOMContentLoaded", function () {
    $(document).ready(function () {
        const table = $("#log-table").DataTable({
            ajax: {
                url: "/WebBongDen_war/list-log",
                type: "GET",
                dataSrc: "",
            },
            error: function (xhr, error, thrown) {
                console.log("Error:", error);
                console.log("Response Text:", xhr.responseText);
            },
            destroy: true,
            autoWidth: false,
            paging: true,
            pageLength: 10,
            dom: 'Bfrtip',
            buttons: [
                {
                    extend: "pdfHtml5",
                    text: "Xuất PDF",
                    className: "btn btn-danger",
                    title: "Danh Sách Log Hệ Thống",
                    exportOptions: {
                        columns: [0, 1, 2, 3, 4, 5, 6, 7],
                    },
                },
            ],
            columns: [
                { data: "id", title: "ID" },
                { data: "accountId", title: "Tài khoản" },
                { data: "level", title: "Level" },
                { data: "action", title: "Hành động" },
                { data: "resource", title: "Tài nguyên" },
                {
                    data: "beforeData",
                    title: "Dữ liệu trước",
                    render: function (data) {
                        if (!data) return "—";
                        try {
                            const obj = JSON.parse(data);
                            return Object.entries(obj)
                                .map(([key, value]) => `<div><strong>${key}:</strong> ${value}</div>`)
                                .join("");
                        } catch (e) {
                            return `<i>Lỗi JSON</i>`;
                        }
                    }
                },
                {
                    data: "afterData",
                    title: "Dữ liệu sau",
                    render: function (data) {
                        if (!data) return "—";
                        try {
                            const obj = JSON.parse(data);
                            return Object.entries(obj)
                                .map(([key, value]) => `<div><strong>${key}:</strong> ${value}</div>`)
                                .join("");
                        } catch (e) {
                            return `<i>Lỗi JSON</i>`;
                        }
                    }
                },
                {
                    data: "time",
                    title: "Thời gian",
                    render: function (data) {
                        const date = new Date(data);
                        return date.toLocaleString("vi-VN");
                    }
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
                info: "Hiển thị _START_ đến _END_ của _TOTAL_ log",
            },
        });
    });
});
