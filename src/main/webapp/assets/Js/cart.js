document.addEventListener("DOMContentLoaded", async function () {
  // === PHẦN LẤY ĐỊA CHỈ TỪ provinces.open-api.vn ===
  const fetchData = async (url) => {
    try {
      const response = await fetch(url);
      if (!response.ok) {
        console.error(`Lỗi fetch API: ${response.status} ${response.statusText} cho URL: ${url}`);
        Notiflix.Notify.failure(`Lỗi tải dữ liệu địa chỉ (${response.status}). Vui lòng thử lại.`);
        return null;
      }
      return response.json();
    } catch (error) {
      console.error(`Lỗi mạng hoặc không thể parse JSON cho URL: ${url}`, error);
      Notiflix.Notify.failure('Lỗi kết nối mạng khi tải dữ liệu địa chỉ. Vui lòng kiểm tra kết nối.');
      return null;
    }
  };

  const provincesOpenApi = await fetchData("https://provinces.open-api.vn/api/p/");
  const provinceSelect = document.getElementById("province");
  const districtSelect = document.getElementById("district");
  const wardSelect = document.getElementById("ward");

  const ghnDistrictIdInput = document.getElementById("ghn_district_id");
  const ghnWardCodeInput = document.getElementById("ghn_ward_code");
  const hiddenShippingFeeInput = document.getElementById("hidden_shipping_fee");
  const finalShippingFeePaymentFormInput = document.getElementById("final_shipping_fee_payment_form");
  const finalTotalAmountPaymentFormInput = document.getElementById("final_total_amount_payment_form");

  if (provinceSelect && provincesOpenApi) {
    provincesOpenApi.forEach((province) => {
      const option = new Option(province.name, province.name);
      provinceSelect.appendChild(option);
    });
  } else {
    if (!provinceSelect) console.error("Không tìm thấy element #province");
    if (!provincesOpenApi) console.error("Không thể tải danh sách tỉnh/thành từ provinces.open-api.vn");
  }

  if (provinceSelect) {
    provinceSelect.addEventListener("change", async function () {
      const provinceName = this.value;
      if (districtSelect) districtSelect.innerHTML = '<option value="">Chọn Quận, Huyện</option>';
      if (wardSelect) wardSelect.innerHTML = '<option value="">Chọn Phường, Xã</option>';
      if (ghnDistrictIdInput) ghnDistrictIdInput.value = "";
      if (ghnWardCodeInput) ghnWardCodeInput.value = "";
      resetShippingFeeDisplay(); // OK: Thay đổi tỉnh -> reset

      if (!provinceName || !provincesOpenApi) return;
      const selectedProvinceData = provincesOpenApi.find(p => p.name === provinceName);
      if (selectedProvinceData) {
        const provinceDetails = await fetchData(`https://provinces.open-api.vn/api/p/${selectedProvinceData.code}?depth=2`);
        if (provinceDetails && provinceDetails.districts && districtSelect) {
          provinceDetails.districts.forEach((district) => {
            const option = new Option(district.name, district.name);
            districtSelect.appendChild(option);
          });
        }
      }
    });
  }

  if (districtSelect) {
    districtSelect.addEventListener("change", async function () {
      const districtName = this.value;
      const provinceName = provinceSelect ? provinceSelect.value : "";
      if (wardSelect) wardSelect.innerHTML = '<option value="">Chọn Phường, Xã</option>';
      if (ghnWardCodeInput) ghnWardCodeInput.value = "";
      resetShippingFeeDisplay(); // OK: Thay đổi quận/huyện -> reset

      if (!districtName || !provinceName || !provincesOpenApi) return;
      const selectedProvinceData = provincesOpenApi.find(p => p.name === provinceName);
      if (selectedProvinceData) {
        const provinceDetails = await fetchData(`https://provinces.open-api.vn/api/p/${selectedProvinceData.code}?depth=2`);
        if (provinceDetails && provinceDetails.districts) {
          const selectedDistrictData = provinceDetails.districts.find(d => d.name === districtName);
          if (selectedDistrictData) {
            const districtDetails = await fetchData(`https://provinces.open-api.vn/api/d/${selectedDistrictData.code}?depth=2`);
            if (districtDetails && districtDetails.wards && wardSelect) {
              districtDetails.wards.forEach((ward) => {
                const option = new Option(ward.name, ward.name);
                wardSelect.appendChild(option);
              });
            }
          }
        }
      }
    });
  }

  if (wardSelect) {
    wardSelect.addEventListener("change", async function () {
      const wardName = this.value;
      const districtName = districtSelect ? districtSelect.value : "";
      const provinceName = provinceSelect ? provinceSelect.value : "";

      // Sửa logic reset ở đây
      if (wardName && districtName && provinceName) {
        // Không reset ngay, để fetchGHNIdsAndThenCalculateFee xử lý UI
        await fetchGHNIdsAndThenCalculateFee(provinceName, districtName, wardName);
      } else {
        resetShippingFeeDisplay(); // Chỉ reset nếu người dùng chọn giá trị rỗng (ví dụ: "Chọn Phường Xã")
      }
    });
  }
  // === KẾT THÚC PHẦN LẤY ĐỊA CHỈ ===

  // === PHẦN TÍNH PHÍ VẬN CHUYỂN GHN VÀ CÁC HÀM LIÊN QUAN ===
  async function fetchGHNIdsAndThenCalculateFee(provinceName, districtName, wardName) {
    Notiflix.Loading.standard('Đang xác thực địa chỉ GHN...');
    // console.log(`[GHN Mapping] Bắt đầu tìm ID cho: Tỉnh ${provinceName}, Huyện ${districtName}, Xã ${wardName}`); // Có thể giữ hoặc bỏ tùy ý

    if (!ghnDistrictIdInput || !ghnWardCodeInput) {
      console.error("[GHN Mapping] Lỗi: Không tìm thấy #ghn_district_id hoặc #ghn_ward_code.");
      Notiflix.Loading.remove();
      Notiflix.Notify.failure('Lỗi cấu hình trang. Không thể tính phí.');
      resetShippingFeeDisplay();
      return;
    }

    try {
      const encodedProvince = encodeURIComponent(provinceName);
      const encodedDistrict = encodeURIComponent(districtName);
      const encodedWard = encodeURIComponent(wardName);
      const servletURL = `/WebBongDen_war/get-ghn-address-ids?province=${encodedProvince}&district=${encodedDistrict}&ward=${encodedWard}`;
      // console.log("[GHN Mapping] Calling servlet:", servletURL); // Có thể giữ hoặc bỏ tùy ý
      const response = await fetch(servletURL);

      if (!response.ok) {
        let errorMsg = `Lỗi HTTP ${response.status} khi gọi servlet địa chỉ.`;
        try {
          const errorData = await response.json();
          errorMsg = errorData.error || errorData.message || errorMsg;
        } catch (e) { /* Nếu không phải JSON, errorMsg giữ nguyên */ }
        console.error("[GHN Mapping] Lỗi từ servlet:", errorMsg);
        throw new Error(errorMsg);
      }

      const ids = await response.json();
      // console.log("[GHN Mapping] Phản hồi từ servlet:", ids); // Có thể giữ hoặc bỏ tùy ý

      if (ids.error) {
        Notiflix.Notify.failure(`Xác thực địa chỉ thất bại: ${ids.error}`);
        ghnDistrictIdInput.value = "";
        ghnWardCodeInput.value = "";
        resetShippingFeeDisplay();
      } else if (ids.ghnDistrictId && ids.ghnWardCode) {
        ghnDistrictIdInput.value = ids.ghnDistrictId;
        ghnWardCodeInput.value = ids.ghnWardCode;
        await calculateAndDisplayShippingFee();
      } else {
        Notiflix.Notify.failure('Không tìm thấy mã địa chỉ GHN tương ứng.');
        ghnDistrictIdInput.value = "";
        ghnWardCodeInput.value = "";
        resetShippingFeeDisplay();
      }
    } catch (error) {
      Notiflix.Notify.failure(`Lỗi kết nối khi xác thực địa chỉ: ${error.message}`);
      console.error("[GHN Mapping] Lỗi fetch/network:", error);
      if(ghnDistrictIdInput) ghnDistrictIdInput.value = "";
      if(ghnWardCodeInput) ghnWardCodeInput.value = "";
      resetShippingFeeDisplay();
    } finally {
      Notiflix.Loading.remove();
    }
  }

  async function calculateAndDisplayShippingFee() {
    const toDistrictId = ghnDistrictIdInput ? parseInt(ghnDistrictIdInput.value) : 0;
    const toWardCode = ghnWardCodeInput ? ghnWardCodeInput.value : "";
    // console.log("[GHN Fee CALC] Tính phí với District ID:", toDistrictId, "Ward Code:", toWardCode); // Có thể giữ hoặc bỏ tùy ý

    if (!toDistrictId || !toWardCode) {
      updateShippingFeeUI(0, false, "Thiếu mã GHN để tính phí");
      return;
    }
    updateShippingFeeUI(0, true, "Đang tính phí vận chuyển...");

    try {
      const response = await fetch("/WebBongDen_war/calculate-shipping-fee", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ to_district_id: toDistrictId, to_ward_code: toWardCode }),
      });
      const data = await response.json();
      // console.log("[GHN Fee CALC] Phản hồi từ servlet tính phí:", data); // Có thể giữ hoặc bỏ tùy ý

      if (data.error) {
        updateShippingFeeUI(0, false, `Lỗi tính phí: ${data.error}`);
        Notiflix.Notify.failure(`Lỗi tính phí từ GHN: ${data.error}`);
      } else if (typeof data.shipping_fee === 'number') {
        updateShippingFeeUI(data.shipping_fee);
        Notiflix.Notify.success('Đã cập nhật phí vận chuyển!');
      } else {
        updateShippingFeeUI(0, false, "Không có thông tin phí");
      }
    } catch (error) {
      updateShippingFeeUI(0, false, "Lỗi kết nối máy chủ tính phí");
      Notiflix.Notify.failure('Lỗi kết nối máy chủ tính phí vận chuyển.');
      console.error("[GHN Fee CALC] Lỗi fetch:", error);
    }
  }
  window.calculateAndDisplayShippingFeeGlobal = calculateAndDisplayShippingFee;

  // Trong file cart.js

  function updateShippingFeeUI(fee, isLoading = false, message = null) {
    let feeNumber = 0;
    if (typeof fee === 'number' && !isLoading && !message) {
      feeNumber = fee;
    }

    const feeText = isLoading ? (message || "Đang xử lý...")
        : (message ? message
                : (feeNumber > 0 ? `${feeNumber.toLocaleString()} VND`
                        : (feeNumber === 0 ? "Miễn phí" : "Chưa tính")
                )
        );

    let subTotalNumber = parseFloat(document.getElementById('cartSubtotal')?.value || window.cartSubtotal || 0);
    if (isNaN(subTotalNumber)) {
      const subTotalEl = document.querySelector('#cus-info .sub-total-price') || document.querySelector('#cart .total-price');
      subTotalNumber = subTotalEl ? (parseFloat(subTotalEl.textContent.replace(/[^\d.-]/g, '')) || 0) : 0;
    }

    const totalWithShipping = subTotalNumber + feeNumber;
    const totalWithShippingText = `${totalWithShipping.toLocaleString()} VND`;
    const subTotalText = `${subTotalNumber.toLocaleString()} VND`;

    // 1. Cập nhật các phần hiển thị TẠM TÍNH (Subtotal) chung
    document.querySelectorAll('.sub-total-price, .sub-total-amount, .sub-total-price-summary').forEach(el => {
      if (el) el.textContent = subTotalText;
    });
    // Đảm bảo "Tạm tính" trong tab giỏ hàng (#cart) cũng được cập nhật
    const cartTabSubtotalDisplay = document.querySelector('#cart .total-price');
    if (cartTabSubtotalDisplay) cartTabSubtotalDisplay.textContent = subTotalText;

    // 2. Cập nhật cho TAB GIỎ HÀNG (#cart) - phần phí và tổng cộng (nếu có)
    const shippingFeeCartTabEl = document.getElementById("shipping-fee-cart-tab");
    const grandTotalCartTabContainerEl = document.getElementById("grand-total-cart-tab-container");
    const grandTotalPriceCartTabEl = document.querySelector("#cart .grand-total-price-cart-tab");

    if (shippingFeeCartTabEl && grandTotalCartTabContainerEl && grandTotalPriceCartTabEl) {
      if (!isLoading && typeof fee === 'number' && fee >= 0 && !message) {
        shippingFeeCartTabEl.textContent = feeText;
        grandTotalPriceCartTabEl.textContent = totalWithShippingText;
        grandTotalCartTabContainerEl.style.display = 'flex';
        grandTotalCartTabContainerEl.style.flexDirection = 'row';
        grandTotalCartTabContainerEl.style.justifyContent = 'space-between';
      } else if (message && (message === "Chọn địa chỉ để tính phí" || message === "Chưa tính" || message.startsWith("Lỗi") || message === "Thiếu mã GHN để tính phí")) {
        shippingFeeCartTabEl.textContent = message;
        grandTotalCartTabContainerEl.style.display = 'none';
      } else if (isLoading) {
        shippingFeeCartTabEl.textContent = feeText;
        grandTotalCartTabContainerEl.style.display = 'none';
      } else {
        shippingFeeCartTabEl.textContent = "Sẽ được tính ở bước sau";
        grandTotalCartTabContainerEl.style.display = 'none';
      }
    }

    // 3. ******** SỬA Ở ĐÂY: Cập nhật phí và tổng tiền ở tab THÔNG TIN ĐẶT HÀNG (#cus-info) ********
    const shippingFeeCusInfoEl = document.getElementById("shipping-fee-cus-info");
    if (shippingFeeCusInfoEl) {
      shippingFeeCusInfoEl.textContent = feeText; // Cập nhật hiển thị phí ship
    }

    // Sửa selector ở dòng dưới từ getElementById thành querySelector với class
    const totalPriceWithShippingCusInfoEl = document.querySelector("#cus-info .total-price-with-shipping-cus-info");
    if (totalPriceWithShippingCusInfoEl) {
      totalPriceWithShippingCusInfoEl.textContent = totalWithShippingText; // Cập nhật hiển thị tổng tiền (gồm ship)
    } else {
      // Thêm log để dễ debug nếu không tìm thấy element
      // console.warn("Không tìm thấy element '#cus-info .total-price-with-shipping-cus-info' để cập nhật tổng tiền.");
    }

    // Cập nhật input ẩn chứa phí ship (dùng để submit form #customer-info-form)
    const hiddenShippingFeeInput = document.getElementById("hidden_shipping_fee"); // Đảm bảo biến này được khai báo ở phạm vi ngoài nếu dùng chung
    if (hiddenShippingFeeInput) {
      hiddenShippingFeeInput.value = feeNumber;
    }
    // ******** KẾT THÚC SỬA PHẦN #cus-info ********


    // 4. Cập nhật phí và tổng tiền ở tab THANH TOÁN (#payment)
    const shippingFeePaymentEl = document.getElementById("shipping-fee-payment");
    if (shippingFeePaymentEl) shippingFeePaymentEl.textContent = feeText;
    const totalPriceWithShippingPaymentEl = document.getElementById("total-price-with-shipping-payment");
    if (totalPriceWithShippingPaymentEl) totalPriceWithShippingPaymentEl.textContent = totalWithShippingText;

    const shippingFeePaymentSummaryEl = document.getElementById("shipping-fee-payment-summary");
    if (shippingFeePaymentSummaryEl) shippingFeePaymentSummaryEl.textContent = feeText;
    const totalPriceWithShippingSummaryEl = document.getElementById("total-price-with-shipping-summary");
    if (totalPriceWithShippingSummaryEl) totalPriceWithShippingSummaryEl.textContent = totalWithShippingText;

    // 5. Cập nhật input ẩn cho form thanh toán cuối cùng (#payment-form)
    // Đảm bảo các biến finalShippingFeePaymentFormInput và finalTotalAmountPaymentFormInput được khai báo ở phạm vi ngoài nếu dùng chung
    const finalShippingFeePaymentFormInput = document.getElementById("final_shipping_fee_payment_form");
    const finalTotalAmountPaymentFormInput = document.getElementById("final_total_amount_payment_form");
    if(finalShippingFeePaymentFormInput) finalShippingFeePaymentFormInput.value = feeNumber;
    if(finalTotalAmountPaymentFormInput) finalTotalAmountPaymentFormInput.value = totalWithShipping;

    // console.log(`[UI Update] FeeNumber: ${feeNumber}, FeeText: "${feeText}", Subtotal: ${subTotalNumber}, Total w/ Ship: ${totalWithShipping}, Message: "${message}"`);
  }

  function resetShippingFeeDisplay() {
    // console.log("[UI Reset] Gọi resetShippingFeeDisplay."); // Có thể giữ hoặc bỏ tùy ý
    updateShippingFeeUI(0, false, "Chọn địa chỉ để tính phí");
    if(hiddenShippingFeeInput) hiddenShippingFeeInput.value = 0;
    if(finalShippingFeePaymentFormInput) finalShippingFeePaymentFormInput.value = 0;
    let subTotalNumber = parseFloat(document.getElementById('cartSubtotal')?.value || window.cartSubtotal || 0);
    if (isNaN(subTotalNumber)) {
      const subTotalEl = document.querySelector('#cus-info .sub-total-price') || document.querySelector('#cart .total-price');
      subTotalNumber = subTotalEl ? (parseFloat(subTotalEl.textContent.replace(/[^\d.-]/g, '')) || 0) : 0;
    }
    if(finalTotalAmountPaymentFormInput) finalTotalAmountPaymentFormInput.value = subTotalNumber;
  }
  // === KẾT THÚC PHẦN TÍNH PHÍ GHN ===

  // === PHẦN CODE CHÍNH (TABS, SUBMIT FORM) ===
  const tabs = document.querySelectorAll('.tab-content');
  const navItems = document.querySelectorAll('.nav-item');

  function updateTabProgress(currentHash) {
    let isActive = true;
    navItems.forEach(item => {
      const tabHash = `#${item.getAttribute('data-tab')}`;
      if (tabHash === currentHash) {
        isActive = false;
        item.classList.add('active', 'completed');
      } else if (isActive) {
        item.classList.add('active', 'completed');
      } else {
        item.classList.remove('active', 'completed');
      }
    });
  }

  function switchTab(hash) {
    tabs.forEach(tab => { if(tab) tab.style.display = 'none'; });
    const targetTab = document.querySelector(hash);

    if (targetTab) {
      targetTab.style.display = 'block';
      updateTabProgress(hash);

      // SỬA LOGIC QUAN TRỌNG Ở ĐÂY
      if (hash === '#payment') {
        // console.log("[Tab Switch] Đang chuyển sang tab #payment."); // Có thể giữ hoặc bỏ tùy ý
        if (window.customerInfo) {
          const cusNameEl = document.querySelector('#payment .customer-name');
          if (cusNameEl) cusNameEl.textContent = window.customerInfo.cusName || "N/A";
          const cusPhoneEl = document.querySelector('#payment .customer-phone');
          if (cusPhoneEl) cusPhoneEl.textContent = window.customerInfo.phone || "N/A";
          const cusAddressEl = document.querySelector('#payment .customer-address');
          if (cusAddressEl) cusAddressEl.textContent = window.customerInfo.address || "Chưa cập nhật";

          if (typeof window.customerInfo.shippingFee === 'number' && window.customerInfo.shippingFee >= 0) {
            // console.log("[Tab Switch #payment] Sử dụng phí từ window.customerInfo:", window.customerInfo.shippingFee); // Có thể giữ hoặc bỏ tùy ý
            updateShippingFeeUI(window.customerInfo.shippingFee);
          } else {
            // console.warn("[Tab Switch #payment] Không có phí vận chuyển hợp lệ trong window.customerInfo. Hiển thị 'Chưa tính'."); // Có thể giữ hoặc bỏ tùy ý
            updateShippingFeeUI(0, false, "Chưa tính");
          }
        } else {
          console.error("[Tab Switch #payment] window.customerInfo không tồn tại. Hiển thị thông báo.");
          updateShippingFeeUI(0, false, "Thông tin khách hàng chưa có");
        }
      }
      // Không cần else if cho #cus-info hay #cart ở đây để xử lý phí,
      // vì event listeners của dropdown đã xử lý việc reset khi cần.
    } else {
      console.error(`Tab không tồn tại: ${hash}. Mặc định về #cart.`);
      const defaultTab = document.querySelector('#cart');
      if (defaultTab) {
        defaultTab.style.display = 'block';
        updateTabProgress('#cart');
      }
    }
  }

  // QUAN TRỌNG: Dòng resetShippingFeeDisplay() vô điều kiện đã được xóa khỏi đây.
  // resetShippingFeeDisplay(); // <<<< ĐÃ XÓA DÒNG NÀY

  const currentHash = window.location.hash || '#cart';
  switchTab(currentHash); // Gọi switchTab để thiết lập tab ban đầu

  window.addEventListener('hashchange', () => {
    const newHash = window.location.hash || '#cart';
    switchTab(newHash);
  });

  const customerInfoForm = document.getElementById("customer-info-form");
  if (customerInfoForm) {
    customerInfoForm.addEventListener("submit", function (e) {
      e.preventDefault();
      const feeText = document.getElementById("shipping-fee-cus-info")?.textContent || "";
      const currentFeeValue = hiddenShippingFeeInput ? hiddenShippingFeeInput.value : "-1";
      const isValidFee = !isNaN(parseFloat(currentFeeValue)) && parseFloat(currentFeeValue) >= 0 &&
          !feeText.includes("Đang tính") &&
          !feeText.includes("Lỗi") &&
          !feeText.includes("Vui lòng chọn") &&
          !feeText.includes("Thiếu mã GHN") &&
          !feeText.includes("Chưa có phí") && // Trạng thái này cũng không hợp lệ để submit
          feeText.trim() !== "Chọn địa chỉ để tính phí"; // Thêm điều kiện này

      if (!isValidFee) {
        Swal.fire({
          icon: 'warning',
          title: 'Phí vận chuyển không hợp lệ',
          text: 'Vui lòng chọn đầy đủ địa chỉ để hệ thống tính phí vận chuyển chính xác trước khi tiếp tục.',
        });
        // console.warn("[Form Submit] Phí vận chuyển không hợp lệ:", feeText, "Giá trị số:", currentFeeValue); // Có thể giữ hoặc bỏ tùy ý
        return;
      }
      Swal.fire({
        title: 'Đang lưu thông tin...',
        text: 'Vui lòng chờ trong giây lát.',
        allowOutsideClick: false,
        didOpen: () => { Swal.showLoading(); }
      });
      setTimeout(() => { this.submit(); }, 500);
    });
  }

  const paymentForm = document.getElementById('payment-form');
  if (paymentForm) {
    paymentForm.addEventListener('submit', function (event) {
      event.preventDefault();
      const finalFeeValue = finalShippingFeePaymentFormInput ? finalShippingFeePaymentFormInput.value : "-1";
      if (isNaN(parseFloat(finalFeeValue)) || parseFloat(finalFeeValue) < 0) {
        Swal.fire({
          icon: 'error',
          title: 'Lỗi phí vận chuyển',
          text: 'Không thể xác định phí vận chuyển. Vui lòng quay lại bước thông tin đặt hàng.',
        });
        return;
      }
      Swal.fire({
        title: 'Đang xử lý đơn hàng...',
        text: 'Vui lòng chờ trong giây lát.',
        allowOutsideClick: false,
        showConfirmButton: false,
        didOpen: () => { Swal.showLoading(); }
      });
      setTimeout(() => { this.submit(); }, 1000);
    });
  }
  // === KẾT THÚC PHẦN CODE CHÍNH CỦA BẠN ===

}); // Kết thúc DOMContentLoaded

// === HÀM updateQuantity ===
function updateQuantity(button, change) {
  const form = button.closest('form');
  const quantityInput = form.querySelector('input[name="quantity"]');
  const productId = form.querySelector('input[name="productId"]').value;
  const currentQuantity = parseInt(quantityInput.value, 10);
  let newValue = currentQuantity + change;

  if (newValue < 1) {
    Notiflix.Report.warning('Số lượng không hợp lệ', 'Số lượng sản phẩm phải lớn hơn hoặc bằng 1.', 'Đã hiểu');
    return;
  }
  quantityInput.value = newValue;
  Notiflix.Loading.standard('Đang cập nhật giỏ hàng...');

  fetch(form.action, {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: `productId=${productId}&quantity=${newValue}`,
  })
      .then((response) => {
        if (!response.ok) {
          let errorMsg = `Lỗi mạng hoặc server: ${response.status}`;
          return response.text().then(text => {
            try {
              const jsonData = JSON.parse(text);
              errorMsg = jsonData.message || jsonData.error || errorMsg;
            } catch (e) { if(text) errorMsg = text; }
            throw new Error(errorMsg);
          });
        }
        return response.json();
      })
      .then((data) => {
        Notiflix.Loading.remove();
        if (data.success) {
          Notiflix.Notify.success('Cập nhật giỏ hàng thành công!');
          if (typeof data.cartSubtotalNumeric !== 'undefined') {
            window.cartSubtotal = data.cartSubtotalNumeric;
            const cartSubtotalInput = document.getElementById('cartSubtotal');
            if(cartSubtotalInput) cartSubtotalInput.value = data.cartSubtotalNumeric;
          } else {
            if (data.totalPrice && typeof data.totalPrice === 'string') { // Fallback
              window.cartSubtotal = parseFloat(data.totalPrice.replace(/[^\d.-]/g, '')) || 0;
              if(document.getElementById('cartSubtotal')) document.getElementById('cartSubtotal').value = window.cartSubtotal;
            }
          }

          const subTotalTextForUI = `${(window.cartSubtotal || 0).toLocaleString()} VND`;
          document.querySelectorAll('.sub-total-price, #cart .total-price, .sub-total-amount, .sub-total-price-summary').forEach(el => {
            if(el) el.textContent = subTotalTextForUI;
          });

          const headerCartQuantityEl = document.querySelector(".quantity-product");
          if (headerCartQuantityEl && typeof data.totalQuantity !== 'undefined') {
            headerCartQuantityEl.textContent = data.totalQuantity;
          }

          const ghnWardCodeVal = document.getElementById("ghn_ward_code")?.value || ""; // Sử dụng optional chaining và default value
          const ghnDistrictIdVal = document.getElementById("ghn_district_id")?.value || "";
          if (ghnWardCodeVal && ghnDistrictIdVal) {
            if (typeof window.calculateAndDisplayShippingFeeGlobal === 'function') {
              // console.log("[Update Quantity] Gọi lại tính phí vận chuyển."); // Có thể giữ hoặc bỏ tùy ý
              window.calculateAndDisplayShippingFeeGlobal();
            }
          } else {
            // console.log("[Update Quantity] Chưa có ID GHN, reset hiển thị phí."); // Có thể giữ hoặc bỏ tùy ý
            resetShippingFeeDisplay(); // Giữ lại reset này: nếu thay đổi giỏ hàng và chưa chọn địa chỉ, phí nên reset.
          }
        } else {
          Notiflix.Notify.failure(data.message || 'Cập nhật giỏ hàng thất bại.');
          quantityInput.value = currentQuantity;
        }
      })
      .catch((error) => {
        Notiflix.Loading.remove();
        Notiflix.Notify.failure(`Lỗi cập nhật giỏ hàng: ${error.message}.`);
        quantityInput.value = currentQuantity;
        console.error('[updateQuantity] Lỗi:', error);
      });
}