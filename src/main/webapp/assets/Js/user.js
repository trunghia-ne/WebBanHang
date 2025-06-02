function showContent(sectionId) {
  const menuAll = document.querySelectorAll(".menu-user li");
  menuAll.forEach((item) => {
    item.classList.remove('active');
  });
  const selectedItem = Array.from(menuAll).find(item => item.getAttribute("data-section") === sectionId);
  if (selectedItem) {
    selectedItem.classList.add("active");
  }

  // Ẩn tất cả các phần nội dung
  document.querySelectorAll(".content_section").forEach((section) => {
    section.style.display = "none";
  });
  // Hiển thị phần nội dung được chọn
  document.getElementById(sectionId).style.display = "block";
}

document.getElementById('edit-info').addEventListener('click', function () {
  // Lấy tất cả các input cần chỉnh sửa
  const inputs = document.querySelectorAll('.info-form input:not([id="email"]):not([id="create-date"])');

  // Bật chế độ chỉnh sửa
  inputs.forEach(input => {
    input.readOnly = false; // Tắt chế độ readonly
    input.classList.add('editable'); // Thêm lớp để có thể thay đổi kiểu dáng (nếu cần)
  });

  // Hiển thị nút lưu và ẩn nút sửa
  document.getElementById('edit-info').style.display = 'none';
  document.getElementById('save-info').style.display = 'inline-block';
});

// Khi lưu thông tin
document.getElementById('save-info').addEventListener('click', function (e) {
  e.preventDefault();

  // Lấy ID khách hàng từ thuộc tính data
  const customerId = document.getElementById('userInfo').getAttribute('data-customer-id');

  // Thu thập dữ liệu từ các input
  const formData = {
    customerId: customerId,
    cusName: document.getElementById('username').value,
    address: document.getElementById('address').value,
    phone: document.getElementById('phone').value,
  };

  console.log(formData); // Kiểm tra dữ liệu trước khi gửi

  // Gửi AJAX để cập nhật thông tin
  $.ajax({
    url: '/WebBongDen_war/edit-cus-info',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(formData),
    success: function (response) {
      if (response.success) {
        Swal.fire('Thành công!', 'Thông tin của bạn đã được cập nhật.', 'success');

        // Đặt lại trạng thái readonly sau khi lưu
        const inputs = document.querySelectorAll('.info-form input:not([id="email"]):not([id="create-date"])');
        inputs.forEach(input => {
          input.readOnly = true; // Bật lại chế độ readonly
          input.classList.remove('editable'); // Xóa lớp editable
        });

        // Ẩn nút lưu và hiển thị nút sửa
        document.getElementById('save-info').style.display = 'none';
        document.getElementById('edit-info').style.display = 'inline-block';
      } else {
        Swal.fire('Thất bại!', 'Không thể cập nhật thông tin. Vui lòng thử lại.', 'error');
      }
    },
    error: function () {
      Swal.fire('Lỗi!', 'Đã xảy ra lỗi khi cập nhật thông tin.', 'error');
    }
  });
});

//Xử ly doi mk
const validator = new JustValidate('.change_password_form');

validator
    .addField('#oldPassword', [
      {
        rule: 'required',
        errorMessage: 'Vui lòng nhập mật khẩu cũ',
      },
    ])
    .addField('#newPassword', [
      {
        rule: 'required',
        errorMessage: 'Vui lòng nhập mật khẩu mới',
      },
      {
        validator: (value) =>
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/.test(value),
        errorMessage:
            'Mật khẩu phải có chữ hoa, chữ thường, số, ký tự đặc biệt và ít nhất 8 ký tự',
      },
    ])
    .addField('#confirm_password', [
      {
        rule: 'required',
        errorMessage: 'Vui lòng nhập lại mật khẩu',
      },
      {
        validator: (value, fields) => {
          return value === fields['#newPassword'].elem.value;
        },
        errorMessage: 'Mật khẩu xác nhận không khớp',
      },
    ])
    .onSuccess((event) => {
      event.preventDefault();

      const form = event.target;
      const data = new URLSearchParams();
      data.append('oldPassword', form.oldPassword.value);
      data.append('newPassword', form.newPassword.value);
      data.append('confirmPassword', form.confirmPassword.value);

      fetch('/WebBongDen_war/change-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: data,
      })
          .then((res) => res.json())
          .then((res) => {
            Toastify({
              text: res.message,
              duration: 3000,
              gravity: 'top',
              position: 'right',
              close: true,
              style: {
                background: res.success
                    ? 'linear-gradient(to right, #00b09b, #96c93d)'
                    : 'linear-gradient(to right, #ff416c, #ff4b2b)',
                color: '#fff',
                borderRadius: '6px',
              },
            }).showToast();

            if (res.success) form.reset();
          });
    })

    // ✅ Hiển thị lỗi bằng Toastify
    .onFail((fields) => {
      const firstErrorField = Object.values(fields)[0];
      if (firstErrorField && firstErrorField.message) {
        Toastify({
          text: firstErrorField.message,
          duration: 3000,
          gravity: 'top',
          position: 'right',
          close: true,
          style: {
            background: 'linear-gradient(to right, #ff416c, #ff4b2b)',
            color: '#fff',
            borderRadius: '6px',
          },
        }).showToast();
      }
    });

$('#avatarInput').on('change', async function () {
  const file = this.files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append("file", file);
  formData.append("upload_preset", "web_upload");
  formData.append("folder", "avatars");

  try {
    const res = await fetch(`https://api.cloudinary.com/v1_1/dptmqc8lj/image/upload`, {
      method: "POST",
      body: formData,
    });

    const data = await res.json();
    if (!data.secure_url) throw new Error("Upload thất bại");

    console.log("Sending avatar update payload:", {
      customerId: $('#userInfo').data('customer-id'),
      avatarUrl: data.secure_url
    });

    // Gửi URL ảnh avatar về server để lưu vào DB
    await fetch("/WebBongDen_war/update-avatar", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        customerId: $('#userInfo').data('customer-id'),
        avatarUrl: data.secure_url
      }),
    });

    $('#avatarPreview').attr('src', data.secure_url);

    Toastify({
      text: "Cập nhật avatar thành công!",
      duration: 3000,
      gravity: "top",
      position: "right",
      style: {
        background: "linear-gradient(to right, #00b09b, #96c93d)",
        color: "#fff",
      },
    }).showToast();
  } catch (error) {
    console.error("Upload thất bại:", error);
    Toastify({
      text: "Tải avatar thất bại!",
      duration: 3000,
      gravity: "top",
      position: "right",
      backgroundColor: "#dc3545",
      close: true,
    }).showToast();
  }
});

$(document).ready(function() {
  const userId = $('#userInfo').data('customer-id'); // hoặc document.getElementById(...).getAttribute(...)

  const table = $('#ordersTable').DataTable({
    ajax: {
      url: '/WebBongDen_war/user-orders',
      data: function(d) {
        d.userId = userId;
      },
      dataSrc: '',
      error: function() {
        alert('Lỗi khi tải dữ liệu đơn hàng.');
      }
    },
    columns: [
      { data: 'id' },
      {
        data: 'createdAt',
        render: function(data) {
          if (!data) return '';
          const date = new Date(data);
          return date.toLocaleDateString('vi-VN');
        }
      },
      {
        data: 'totalPrice',
        render: $.fn.dataTable.render.number('.', ',', 0, '₫')
      },
      { data: 'orderStatus' },
      {
        data: null,
        render: function(data, type, row) {
          console.log(row);
          return `
          <button class="btn-detail" data-id="${row.id}">Chi tiết</button>
          <button class="btn-cancel" data-id="${row.id}">Hủy</button>
        `;
        }
      }
    ],
    order: [[1, 'desc']], // Sắp xếp theo cột ngày tạo, giảm dần (mới nhất trước)
    language: {
      url: 'https://cdn.datatables.net/plug-ins/1.13.4/i18n/vi.json'
    },
    pageLength: 5,
    lengthMenu: [5, 10, 20, 50],
    processing: true,
    responsive: true,
    searching: true,
    ordering: true
  });

  // Đăng ký sự kiện click đúng cách trên tbody, dùng event delegation
  $('#ordersTable tbody').on('click', '.btn-detail', function() {
    const orderId = $(this).data('id');

    fetch(`/WebBongDen_war/order-detail?orderId=${orderId}`)
        .then(res => res.json())
        .then(data => {
          const products = data.orderDetails;
          const shippingFee = Number(data.shippingFee) || 0;

          if (!products || products.length === 0) {
            Swal.fire({
              icon: 'info',
              title: 'Chi tiết đơn hàng',
              html: '<p>Không có sản phẩm trong đơn hàng này.</p>',
            });
            return;
          }

          // Hàm định dạng tiền VND
          const formatVND = num => num.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });

          // Tạo HTML bảng chi tiết đơn hàng
          let tableHtml = `
            <table style="width:100%; border-collapse: collapse; border: 1px solid #ccc;">
                <thead>
                    <tr style="background-color:#f2f2f2;">
                        <th style="border: 1px solid #ccc; padding: 8px;">Sản phẩm</th>
                        <th style="border: 1px solid #ccc; padding: 8px;">Số lượng</th>
                        <th style="border: 1px solid #ccc; padding: 8px;">Đơn giá</th>
                        <th style="border: 1px solid #ccc; padding: 8px;">Giảm giá</th>
                        <th style="border: 1px solid #ccc; padding: 8px;">Thành tiền</th>
                    </tr>
                </thead>
                <tbody>`;

          products.forEach(p => {
            tableHtml += `
                    <tr>
                        <td style="border: 1px solid #ccc; padding: 8px;">${p.productName}</td>
                        <td style="border: 1px solid #ccc; padding: 8px; text-align:center;">${p.quantity}</td>
                        <td style="border: 1px solid #ccc; padding: 8px; text-align:right;">${formatVND(p.unitPrice)}</td>
                        <td style="border: 1px solid #ccc; padding: 8px; text-align:right;">${formatVND(p.itemDiscount)}</td>
                        <td style="border: 1px solid #ccc; padding: 8px; text-align:right;">${formatVND(p.amount)}</td>
                    </tr>`;
          });

          tableHtml += `
                <tr>
                    <td colspan="4" style="border: 1px solid #ccc; padding: 8px; text-align: right; font-weight: bold;">Phí vận chuyển:</td>
                    <td style="border: 1px solid #ccc; padding: 8px; text-align: right;">${formatVND(shippingFee)}</td>
                </tr>
                </tbody>
            </table>`;

          Swal.fire({
            title: 'Chi tiết đơn hàng',
            html: tableHtml,
            width: '700px',
            confirmButtonText: 'Đóng',
            didOpen: () => {
              const swalContent = Swal.getHtmlContainer();
              if (swalContent) {
                const table = swalContent.querySelector('table');
                if (table) {
                  table.style.fontSize = '14px';
                }
              }
            }
          });
        })
        .catch(() => Swal.fire('Lỗi', 'Lỗi khi tải chi tiết đơn hàng', 'error'));
  });
});


