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