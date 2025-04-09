package com.example.webbongden.controller.UserController;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

@WebServlet(name = "ContactController", value = "/ContactController")
@MultipartConfig
public class ContactController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/user//contact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        String fullMessage = "Tên: " + name + "\nEmail: " + email + "\n\nNội dung:\n" + message;

        final String username = "daoduynhat2004@gmail.com";
        final String password = "epkn kwhk zwdo dutq";

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
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse("your-destination-email@gmail.com"));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(fullMessage);

            Transport.send(mimeMessage);
            response.getWriter().write("✅ Gửi liên hệ thành công!");
        } catch (MessagingException e) {
            e.printStackTrace();
            response.getWriter().write("❌ Gửi liên hệ thất bại!");
        }
    }

}