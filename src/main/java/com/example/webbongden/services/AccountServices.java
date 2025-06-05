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
        this.accountDao = new AccountDao();
    }

    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    public List<Account> getAccountByUserName(String name) {
        return accountDao.getAccountByUserName(name);
    }

    public boolean addAccount(Account account) {
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

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public boolean checkEmailExists(String email) {
        return accountDao.checkEmailExists(email);
    }

    public boolean updatePassword(String email, String temporaryPassword) {
        String hashedPassword = BCrypt.hashpw(temporaryPassword, BCrypt.gensalt());
        return accountDao.updatePassword(email, hashedPassword);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Account account = accountDao.authenticate(username);
        if (account == null) {
            return false;
        }
        if (!checkPassword(oldPassword, account.getPassword())) {
            return false;
        }
        String hashedPassword = hashPassword(newPassword);
        return accountDao.updatePassword(account.getEmail(), hashedPassword);
    }

    public String generateOtp() {
        Random random = new Random();
        int otpNumber = 100000 + random.nextInt(900000);
        return String.format("%06d", otpNumber);
    }

    public boolean sendOtpToEmail(String email, String otp) {
        try {
            String subject = "Mã OTP khôi phục mật khẩu"; // Chủ đề cố định
            String body = "Mã OTP của bạn là: " + otp + ". Mã có hiệu lực trong 5 phút.";
            EmailService.sendEmail(email, subject, body);
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
            String emailBody = String.format(bodyTemplate, otp);
            EmailService.sendEmail(toEmail, subject, emailBody);
            System.out.println("Email OTP (tùy chỉnh) đã được gửi tới: " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi gửi Email OTP (tùy chỉnh) tới " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean initiateOtpRegistrationProcess(Account accountToRegister, HttpSession session) {
        String otp = generateOtp();
        String userEmail = accountToRegister.getEmail();

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
        Account pendingAccount = (Account) session.getAttribute("pendingRegistrationAccount");
        String storedOtp = (String) session.getAttribute("registrationOtp");
        Long otpTimestamp = (Long) session.getAttribute("registrationOtpTimestamp");

        if (pendingAccount == null || storedOtp == null || otpTimestamp == null) {
            return new RegistrationResult(false, "Phiên đăng ký không hợp lệ hoặc đã hết hạn. Vui lòng thử đăng ký lại từ đầu.");
        }

        long currentTime = System.currentTimeMillis();
        long otpAgeMillis = currentTime - otpTimestamp;
        long fiveMinutesInMillis = 5 * 60 * 1000; // 5 phút

        if (otpAgeMillis > fiveMinutesInMillis) {
            clearOtpRegistrationSessionAttributes(session);
            return new RegistrationResult(false, "Mã OTP đã hết hạn. Vui lòng đăng ký lại để nhận OTP mới.");
        }

        if (userSubmittedOtp != null && userSubmittedOtp.equals(storedOtp)) {
            String plainPassword = pendingAccount.getPassword();
            String hashedPassword = hashPassword(plainPassword);
            pendingAccount.setPassword(hashedPassword);
            if (accountDao.addAccountUser(pendingAccount)) {
                clearOtpRegistrationSessionAttributes(session);
                return new RegistrationResult(true, "Đăng ký tài khoản thành công! Bạn có thể đăng nhập ngay bây giờ.");
            } else {
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