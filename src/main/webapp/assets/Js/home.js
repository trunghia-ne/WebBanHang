
window.addEventListener("load", function () {
  // Use SlickSlider
  $(document).ready(function () {
    $(".slider-wrapper").slick({
      infinite: true,
      slidesToShow: 1,
      autoplay: true,
      autoplaySpeed: 3000,
      prevArrow: `<button type='button' class='slick-prev slick-arrow'><i class="fa-solid fa-angle-left"></i></button>`,
      nextArrow: `<button type='button' class='slick-next slick-arrow'><i class="fa-solid fa-angle-right"></i></button>`,
      dots: true,
      responsive: [
        {
          breakpoint: 480,
          settings: {
            slidesToShow: 1,
            slidesToScroll: 1,
            infinite: true,
            dots: true,
            arrows: false,
          },
        },
      ],
    });
  });

  $(".feedback-list").slick({
    slidesToShow: 1, // Số lượng item hiển thị cùng lúc
    slidesToScroll: 1, // Số lượng item di chuyển mỗi lần cuộn
    infinite: true, // Cho phép cuộn vô hạn
    dots: true, // Hiển thị các dấu chấm chỉ mục
    arrows: true, // Hiển thị nút mũi tên
    vertical: false, // Thiết lập hiển thị ngang
    autoplay: true,
    autoplaySpeed: 3000,
    prevArrow: `<button type='button' class='slick-prev slick-arrow'><i class="fa-solid fa-angle-left"></i></button>`,
    nextArrow: `<button type='button' class='slick-next slick-arrow'><i class="fa-solid fa-angle-right"></i></button>`,
    responsive: [
      {
        breakpoint: 480,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
          infinite: true,
          dots: false,
          arrows: false,
          centerMode: true,
          centerPadding: "10px",
        },
      },
    ],
  });

  $(document).ready(function () {
    $(".list-product-hot").slick({
      infinite: true,
      slidesToShow: 5,
      slidesToScroll: 2,
      autoplay: true,
      autoplaySpeed: 2000,
      prevArrow: `<button type='button' class='slick-prev slick-arrow'><i class="fa-solid fa-angle-left"></i></button>`,
      nextArrow: `<button type='button' class='slick-next slick-arrow'><i class="fa-solid fa-angle-right"></i></button>`,
      responsive: [
        {
          breakpoint: 1025,
          settings: {
            slidesToShow: 4,
            slidesToScroll: 2,
            infinite: false,
          },
        },
        {
          breakpoint: 768,
          settings: {
            slidesToShow: 2,
            slidesToScroll: 1,
            infinite: true,
            dots: true,
            centerMode: true, // Bật chế độ center
            centerPadding: "10px", // Độ rộng của phần tử lòi ra
          },
        },
        {
          breakpoint: 480,
          settings: {
            slidesToShow: 2,
            slidesToScroll: 2,
            infinite: false,
            centerMode: true, // Bật chế độ center
            centerPadding: "10px", // Độ rộng của phần tử lòi ra
          },
        },
      ],
    });
  });

  const countdownTimers = document.querySelectorAll(".countdown-timer");

  countdownTimers.forEach(timer => {
    const endTime = new Date(timer.getAttribute("data-end-time")).getTime();
    const promotionId = timer.querySelector("p").id.split("-")[1]; // Lấy ID của promotion

    function updateCountdown() {
      const now = new Date().getTime();
      const distance = endTime - now;

      if (distance > 0) {
        const days = Math.floor(distance / (1000 * 60 * 60 * 24));
        const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((distance % (1000 * 60)) / 1000);

        document.getElementById(`days-${promotionId}`).textContent = days;
        document.getElementById(`hours-${promotionId}`).textContent = hours;
        document.getElementById(`minutes-${promotionId}`).textContent = minutes;
        document.getElementById(`seconds-${promotionId}`).textContent = seconds;
      } else {
        // Countdown đã hết
        document.getElementById(`days-${promotionId}`).textContent = "00";
        document.getElementById(`hours-${promotionId}`).textContent = "00";
        document.getElementById(`minutes-${promotionId}`).textContent = "00";
        document.getElementById(`seconds-${promotionId}`).textContent = "00";
      }
    }

    // Cập nhật countdown mỗi giây
    updateCountdown(); // Cập nhật ngay khi trang được load
    setInterval(updateCountdown, 1000);
  });

  const input = $('#search-input');
  const results = $('#results');

  input.on('input', function () {
    const val = $(this).val().trim();
    if (!val) {
      results.html('');
      results.hide();
      return;
    }

    $.ajax({
      url: `/WebBongDen_war/autocomplete`,
      method: 'GET',
      data: { query: val },
      dataType: 'json',
      success: function(data) {
        if (data.length === 0) {
          results.html('<div style="padding: 8px;">Không tìm thấy sản phẩm</div>');
          results.show();
          return;
        }

        const html = data.map(p => {
          const mainImage = (p.listImg && p.listImg.find(img => img.mainImage)) || null;
          const imageUrl = mainImage ? mainImage.url : 'https://via.placeholder.com/40';

          return `
          <div onclick="location.href='/WebBongDen_war/product-detail?id=${p.id}'" style="cursor:pointer; display:flex; padding:15px; border-bottom:1px solid #eee;">
            <img src="${imageUrl}" width="50" height="50" alt="${p.productName}" style="margin-right: 10px; border-radius: 4px; object-fit: cover;" />
            <div style="flex: 1; overflow: hidden;">
              <div style="font-weight: 500; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                ${p.productName}
              </div>
              <div style="color: #f00; margin-top: 4px; font-weight: 400; font-size: 13px; white-space: nowrap;">
                ${p.unitPrice.toLocaleString()} đ
              </div>
            </div>
          </div>
        `;
        }).join('');

        results.html(html);
        results.show();
      },
      error: function() {
        results.html('<div style="padding: 8px; color: red;">Lỗi tải dữ liệu</div>');
        results.show();
      }
    });
  });


// Ẩn kết quả khi click ngoài vùng input hoặc results
  $(document).on('click', function(e) {
    if (!$(e.target).closest('#search-input').length && !$(e.target).closest('#results').length) {
      $('#results').hide();
    }
  });
});
