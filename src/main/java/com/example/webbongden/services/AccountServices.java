package com.example.webbongden.services;

import com.example.webbongden.dao.AccountDao;
import com.example.webbongden.dao.model.Account;
import jakarta.servlet.http.HttpSession; // Quan trọng: Sử dụng import phù hợp với Servlet API của bạn (jakarta hoặc javax)
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class AccountServices {
    public final AccountDao accountDao;

    public AccountServices() {
        this.accountDao = new AccountDao(); // Khởi tạo DAO
    }

    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    public List<Account> getAccountByUserName(String name) {
        return accountDao.getAccountByUserName(name);
    }

    public boolean addAccount(Account account) { // Dùng cho admin hoặc mục đích khác
        String hashedPassword = hashPassword(account.getPassword());
        account.setPassword(hashedPassword);
        return accountDao.addAccount(account);
    }

    public boolean deleteAccountById(int accountId) {
        return accountDao.deleteAccountById(accountId);
    }

    public Account authenticate(String username, String plainPassword) {
        Account account = accountDao.authenticate(username);
        if (account == null) {
            return null;
        }
        if (BCrypt.checkpw(plainPassword, account.getPassword())) {
            return account;
        }
        return null;
    }

    public Account getAccountById(int accountId) {
        return accountDao.getAccountById(accountId);
    }

    public boolean updateAccount(Account account) {
        return accountDao.updateAccount(account);
    }

    // Hàm hash mật khẩu (DÙNG CHUNG)
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Hàm kiểm tra mật khẩu (DÙNG CHUNG)
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    // Kiểm tra email có tồn tại (DÙNG CHUNG)
    public boolean checkEmailExists(String email) {
        return accountDao.checkEmailExists(email);
    }

    // Cập nhật mật khẩu tạm thời (Dùng cho chức năng "Quên mật khẩu")
    public boolean updatePassword(String email, String temporaryPassword) {
        String hashedPassword = BCrypt.hashpw(temporaryPassword, BCrypt.gensalt());
        return accountDao.updatePassword(email, hashedPassword);
    }

    // User (Dùng cho chức năng "Đổi mật khẩu" khi đã đăng nhập)
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Account account = accountDao.authenticate(username); // Hoặc getAccountByUserName và xử lý list
        if (account == null) {
            return false; // Tài khoản không tồn tại
        }
        if (!checkPassword(oldPassword, account.getPassword())) {
            return false; // Mật khẩu cũ không chính xác
        }
        String hashedPassword = hashPassword(newPassword);
        return accountDao.updatePassword(account.getEmail(), hashedPassword); // Giả sử DAO cập nhật pass qua email
    }

    // Tạo OTP ngẫu nhiên (DÙNG CHUNG)
    // Đảm bảo OTP luôn có 6 chữ số (từ 100000 đến 999999)
    public String generateOtp() {
        Random random = new Random();
        int otpNumber = 100000 + random.nextInt(900000);
        return String.format("%06d", otpNumber);
    }

    // Gửi OTP qua email (CŨ - Dùng cho "Khôi phục mật khẩu")
    // Giữ nguyên phương thức này cho chức năng khôi phục mật khẩu.
    public boolean sendOtpToEmail(String email, String otp) {
        try {
            String subject = "Mã OTP khôi phục mật khẩu"; // Chủ đề cố định
            String body = "Mã OTP của bạn là: " + otp + ". Mã có hiệu lực trong 5 phút."; // Nội dung cố định
            EmailService.sendEmail(email, subject, body); // Gọi EmailService.java của bạn
            System.out.println("OTP (khôi phục MK) đã được gửi tới: " + email);
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi gửi OTP (khôi phục MK) tới " + email + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendCustomOtpEmail(String toEmail, String otp, String subject, String bodyTemplate) {
        try {
            String emailBody = String.format(bodyTemplate, otp); // Thay thế %s trong template bằng mã OTP
            EmailService.sendEmail(toEmail, subject, emailBody); // Gọi EmailService.java của bạn
            System.out.println("Email OTP (tùy chỉnh) đã được gửi tới: " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi gửi Email OTP (tùy chỉnh) tới " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean initiateOtpRegistrationProcess(Account accountToRegister, HttpSession session) {
        String otp = generateOtp(); // Dùng lại hàm tạo OTP chung
        String userEmail = accountToRegister.getEmail();

        // Chuẩn bị nội dung email cho việc kích hoạt tài khoản
        String emailSubject = "Xác thực tài khoản WebBongDen của bạn";
        String emailBodyTemplate = "Chào mừng bạn đến với WebBongDen!\n\n"
                + "Mã OTP để kích hoạt tài khoản của bạn là: %s\n\n"
                + "Mã này sẽ hết hạn sau 5 phút.\n\n"
                + "Trân trọng,\n"
                + "Đội ngũ WebBongDen.";

        if (sendCustomOtpEmail(userEmail, otp, emailSubject, emailBodyTemplate)) {
            session.setAttribute("pendingRegistrationAccount", accountToRegister);
            session.setAttribute("registrationOtp", otp);
            session.setAttribute("registrationOtpTimestamp", System.currentTimeMillis());
            System.out.println("Đã khởi tạo đăng ký OTP cho email: " + userEmail);
            return true;
        }
        System.out.println("Không thể gửi OTP kích hoạt cho email: " + userEmail);
        return false;
    }

    public RegistrationResult finalizeOtpRegistration(String userSubmittedOtp, HttpSession session) {
        // Lấy thông tin từ session
        Account pendingAccount = (Account) session.getAttribute("pendingRegistrationAccount");
        String storedOtp = (String) session.getAttribute("registrationOtp");
        Long otpTimestamp = (Long) session.getAttribute("registrationOtpTimestamp");

        // Kiểm tra sự tồn tại của các thông tin trong session
        if (pendingAccount == null || storedOtp == null || otpTimestamp == null) {
            return new RegistrationResult(false, "Phiên đăng ký không hợp lệ hoặc đã hết hạn. Vui lòng thử đăng ký lại từ đầu.");
        }

        // Kiểm tra thời gian hiệu lực của OTP (ví dụ: 5 phút)
        long currentTime = System.currentTimeMillis();
        long otpAgeMillis = currentTime - otpTimestamp;
        long fiveMinutesInMillis = 5 * 60 * 1000; // 5 phút

        if (otpAgeMillis > fiveMinutesInMillis) {
            clearOtpRegistrationSessionAttributes(session); // Xóa session khi OTP hết hạn
            return new RegistrationResult(false, "Mã OTP đã hết hạn. Vui lòng đăng ký lại để nhận OTP mới.");
        }

        // So sánh OTP người dùng nhập với OTP đã lưu
        if (userSubmittedOtp != null && userSubmittedOtp.equals(storedOtp)) {
            // OTP hợp lệ
            // Hash mật khẩu trước khi lưu vào cơ sở dữ liệu
            String plainPassword = pendingAccount.getPassword();
            String hashedPassword = hashPassword(plainPassword); // Dùng lại hàm hashPassword chung
            pendingAccount.setPassword(hashedPassword);
            // Gọi DAO để thêm tài khoản vào cơ sở dữ liệu
            if (accountDao.addAccountUser(pendingAccount)) { // Sử dụng phương thức của DAO để thêm user
                clearOtpRegistrationSessionAttributes(session); // Dọn dẹp session sau khi đăng ký thành công
                return new RegistrationResult(true, "Đăng ký tài khoản thành công! Bạn có thể đăng nhập ngay bây giờ.");
            } else {
                // Lỗi khi lưu vào DB, không nên xóa session ngay để người dùng có thể thử lại nếu lỗi tạm thời
                // hoặc có thể do ràng buộc dữ liệu (ví dụ: email/username đã tồn tại - dù đã check trước đó)
                return new RegistrationResult(false, "Đã xảy ra lỗi khi lưu tài khoản. Vui lòng thử lại hoặc liên hệ hỗ trợ.");
            }
        } else {
            // OTP không chính xác
            return new RegistrationResult(false, "Mã OTP không chính xác. Vui lòng kiểm tra lại.");
        }
    }

    private void clearOtpRegistrationSessionAttributes(HttpSession session) {
        if (session != null) {
            session.removeAttribute("pendingRegistrationAccount");
            session.removeAttribute("registrationOtp");
            session.removeAttribute("registrationOtpTimestamp");
            System.out.println("Đã xóa các thuộc tính session của đăng ký OTP.");
        }
    }

    public static class RegistrationResult {
        private final boolean success;
        private final String message;

        public RegistrationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }

    public Set<String> getPermissionsByRoleId(int roleId) {
        return accountDao.findPermissionsByRoleId(roleId);
    }
}