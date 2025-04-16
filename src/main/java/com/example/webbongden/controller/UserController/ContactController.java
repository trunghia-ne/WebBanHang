package com.example.webbongden.controller.UserController;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Properties;

@WebServlet(name = "ContactController", value = "/ContactController")
@MultipartConfig
public class ContactController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/user/contact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        String fullMessage = "<b>Tên:</b> " + name + "<br><b>Email:</b> " + email + "<br><br><b>Nội dung:</b><br>" + message;

        final String username = "ryanz1292004@gmail.com";
        final String password = "xfpg rywy kemf yfde";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        response.setContentType("text/plain;charset=UTF-8");
        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse("ryanz1292004@gmail.com"));
            mimeMessage.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));


            // Sử dụng MimeMultipart để gửi nội dung dưới dạng HTML
            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(fullMessage, "text/html; charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);
            mimeMessage.setContent(multipart);

            Transport.send(mimeMessage);
            response.getWriter().write("✅ Gửi liên hệ thành công!");
        } catch (MessagingException e) {
            e.printStackTrace();
            response.getWriter().write("❌ Gửi liên hệ thất bại!");
        }
    }

}
