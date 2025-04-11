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
                        columns: [0, 1, 2, 3, 4, 6],
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
                    data: null,
                    title: "Chi tiết thay đổi",
                    render: function (data, type, row) {
                        const before = row.beforeData ? row.beforeData.replace(/'/g, "&apos;") : "";
                        const after = row.afterData ? row.afterData.replace(/'/g, "&apos;") : "";
                        return `
            <button class="btn-show-diff btn-view-icon" 
                    data-before='${before}' 
                    data-after='${after}'>
                <i class="fa fa-eye"></i>
            </button>
        `;
                    }
                },
                {
                    data: "time",
                    title: "Thời gian",
                    render: function (data) {
                        const date = new Date(data);
                        return date.toLocaleString("vi-VN");
                    }
                }
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

        $(document).on("click", ".btn-show-diff", function () {
            const beforeRaw = $(this).data("before");
            const afterRaw = $(this).data("after");

            let beforeText = "Không có dữ liệu";
            let afterText = "Không có dữ liệu";

            try {
                if (beforeRaw) {
                    const objBefore = typeof beforeRaw === "object" ? beforeRaw : JSON.parse(beforeRaw);
                    beforeText = JSON.stringify(objBefore, null, 2);
                }
                if (afterRaw) {
                    const objAfter = typeof afterRaw === "object" ? afterRaw : JSON.parse(afterRaw);
                    afterText = JSON.stringify(objAfter, null, 2);
                }
            } catch (e) {
                beforeText = beforeRaw || "Không có";
                afterText = afterRaw || "Không có";
            }

            Swal.fire({
                title: "CHI TIẾT THAY ĐỔI",
                html: `
            <div style="display: flex; gap: 20px; text-align: left;">
                <div style="flex: 1;">
                    <h4>Dữ liệu trước</h4>
                    <pre style="background: #f8f9fa; padding: 10px; border-radius: 5px; white-space: pre-wrap;">${beforeText}</pre>
                </div>
                <div style="flex: 1;">
                    <h4>Dữ liệu sau</h4>
                    <pre style="background: #f1f3f5; padding: 10px; border-radius: 5px; white-space: pre-wrap;">${afterText}</pre>
                </div>
            </div>
        `,
                width: 900,
                showCloseButton: true,
                confirmButtonText: "Đóng"
            });
        });
    });
});
