const cloudName = "dptmqc8lj";
const uploadPreset = "web_upload";

document.getElementById("upload-image").addEventListener("change", function (e) {
    const files = e.target.files;

    // Không làm gì nếu không chọn file
    if (files.length === 0) return;

    Swal.fire({
        title: "Bạn có muốn thêm hình ảnh?",
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "Có, thêm ngay!",
        cancelButtonText: "Hủy",
    }).then(async (result) => {
        if (result.isConfirmed) {
            const uploadedUrls = [];
            const productId = document.getElementById("product-id-hidden").value;

            for (let file of files) {
                const formData = new FormData();
                formData.append("file", file);
                formData.append("upload_preset", uploadPreset);
                formData.append("folder", "product-images");

                try {
                    const res = await fetch(`https://api.cloudinary.com/v1_1/${cloudName}/image/upload`, {
                        method: "POST",
                        body: formData,
                    });

                    const data = await res.json();
                    if (!data.secure_url) throw new Error("Upload thất bại");

                    // Gửi ảnh về server để lưu DB
                    await fetch("/WebBongDen_war/add-product-image", {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({
                            productId: productId,
                            url: data.secure_url,
                        }),
                    });

                    // Hiển thị ảnh lên giao diện
                    const imageList = document.getElementById("product-images-list");
                    imageList.innerHTML += `
                        <div class="image-wrapper">
                            <img src="${data.secure_url}" alt="Ảnh sản phẩm" />
                            <div class="image-overlay">
                                <button class="edit-img-btn" type="button"><i class="fa-solid fa-pen"></i></button>
                                <button class="delete-img-btn" type="button"><i class="fa-solid fa-trash"></i></button>
                                />
                            </div>
                        </div>
                    `;

                    // ✅ Toast thông báo thành công
                    Toastify({
                        text: "Tải ảnh lên thành công!",
                        duration: 3000,
                        gravity: "top",
                        position: "right",
                        backgroundColor: "#28a745",
                        close: true,
                    }).showToast();

                } catch (error) {
                    console.error("❌ Upload thất bại:", error);
                    Toastify({
                        text: "Tải ảnh lên thất bại!",
                        duration: 3000,
                        gravity: "top",
                        position: "right",
                        backgroundColor: "#dc3545",
                        close: true,
                    }).showToast();
                }
            }
        } else {
            // Nếu hủy chọn ảnh thì reset input
            e.target.value = "";
        }
    });
});

document.getElementById("product-images-list").addEventListener("click", function (e) {
    const editBtn = e.target.closest(".edit-img-btn");
    if (editBtn) {
        const wrapper = editBtn.closest(".image-wrapper");
        if (!wrapper) return;

        const inputFile = wrapper.querySelector(".edit-image-input");
        if (inputFile) {
            inputFile.click();
            console.log('Mở dialog chọn file sửa ảnh');
        }
    }

    if (e.target.closest(".delete-img-btn")) {
        const btn = e.target.closest(".delete-img-btn");
        const imgId = btn.dataset.id;

        Swal.fire({
            title: "Bạn có chắc muốn xóa ảnh này?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Xóa",
            cancelButtonText: "Hủy"
        }).then(async result => {
            if (result.isConfirmed) {
                try {
                    const res = await fetch(`/WebBongDen_war/delete-product-image?id=${imgId}`, {
                        method: "DELETE"
                    });
                    if (!res.ok) throw new Error();
                    btn.closest(".image-wrapper").remove();
                    Toastify({
                        text: "Đã xóa ảnh!",
                        duration: 3000,
                        gravity: "top",
                        position: "right",
                        backgroundColor: "#28a745",
                        close: true,
                    }).showToast();
                } catch {
                    Toastify({
                        text: "Xóa ảnh thất bại!",
                        duration: 3000,
                        gravity: "top",
                        position: "right",
                        backgroundColor: "#dc3545",
                        close: true,
                    }).showToast();
                }
            }
        });
    }
});

document.getElementById("product-images-list").addEventListener("change", async function(e) {
    const inputFile = e.target;
    if (!inputFile.classList.contains("edit-image-input")) return;

    const files = inputFile.files;
    if (!files || files.length === 0) return;

    const file = files[0];
    const imageId = inputFile.dataset.imageId;

    // Xác nhận và upload, update ảnh
    const result = await Swal.fire({
        title: "Bạn có muốn cập nhật ảnh này?",
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "Có, cập nhật!",
        cancelButtonText: "Hủy",
    });

    if (!result.isConfirmed) {
        inputFile.value = ""; // reset input
        return;
    }

    try {
        // Upload lên Cloudinary
        const formData = new FormData();
        formData.append("file", file);
        formData.append("upload_preset", uploadPreset);
        formData.append("folder", "product-images");

        const resCloud = await fetch(`https://api.cloudinary.com/v1_1/${cloudName}/image/upload`, {
            method: "POST",
            body: formData,
        });
        const dataCloud = await resCloud.json();

        if (!dataCloud.secure_url) throw new Error("Upload thất bại");

        // Gửi URL mới về server để update database
        const resServer = await fetch("/WebBongDen_war/update-product-image", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                imgId: imageId,
                url: dataCloud.secure_url,
            }),
        });

        if (!resServer.ok) throw new Error("Lỗi server, status: " + resServer.status);

        const data = await resServer.json();

        if (!data.success) {
            throw new Error(data.message || "Cập nhật DB thất bại");
        }

        console.log("Cập nhật thành công");


        // Cập nhật ảnh trên giao diện
        const wrapper = inputFile.closest(".image-wrapper");
        const img = wrapper.querySelector("img");
        img.src = dataCloud.secure_url;

        Toastify({
            text: "Cập nhật ảnh thành công!",
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#28a745",
            close: true,
        }).showToast();

    } catch (error) {
        console.error(error);
        Toastify({
            text: "Cập nhật ảnh thất bại!",
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#dc3545",
            close: true,
        }).showToast();
    }

    inputFile.value = ""; // reset input
});

const imageInput = document.getElementById("product-image-upload");
const previewContainer = document.getElementById("preview-container");

imageInput.addEventListener("change", () => {
    previewContainer.innerHTML = ""; // Clear old previews

    const files = imageInput.files;
    Array.from(files).forEach(file => {
        if (!file.type.startsWith("image/")) return;

        const reader = new FileReader();
        reader.onload = e => {
            const img = document.createElement("img");
            img.src = e.target.result;
            previewContainer.appendChild(img);
        };
        reader.readAsDataURL(file);
    });
});