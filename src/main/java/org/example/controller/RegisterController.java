package org.example.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.example.entity.Attachment;
import org.example.entity.User;
import org.example.repo.AttachmentRepository;
import org.example.repo.UserRepository;
import org.example.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.PublicKey;
import java.util.Random;

@Controller
@RequestMapping("/register")
public class RegisterController {
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    public RegisterController(PasswordEncoder passwordEncoder, EmailService emailService, UserRepository userRepository, AttachmentRepository attachmentRepository) {
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @SneakyThrows
    @PostMapping
    public String registerUser(@ModelAttribute User user,
                               @RequestParam MultipartFile file,
                               HttpServletRequest request) {
        Attachment attachment = Attachment.builder()
                .name(file.getOriginalFilename())
                .content(file.getBytes())
                .build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAttachment(attachment);

        request.getSession().setAttribute("tempUser", user);
        return "redirect:/register/send/email";
    }

    @GetMapping("/send/email")
    public String sendVerificationCode(HttpServletRequest request) {

        User tempUser = (User) request.getSession().getAttribute("tempUser");

        int otp = new Random().nextInt(100000, 1000000);
        tempUser.setOtp(otp);
        System.out.println("🔐  OTP  🗝️ : " + otp);

        Thread thread = new Thread(() -> {
            try {
                emailService.sendEmailToUser(tempUser);
                System.out.println("✅ Email Has Been Sent! ✅");
            } catch (MessagingException e) {
                e.printStackTrace();
                System.out.println("🔴🔴🔴Error sending email: " + e.getMessage());
            }
        });
        thread.start();

        HttpSession session = request.getSession();
        session.setAttribute("tempUser", tempUser);
        session.setAttribute("otp", tempUser.getOtp());
        return "auth/verify-email";
    }

    @PostMapping("/verify")
    public String verifyEmailAndSaveUser(HttpServletRequest request, @RequestParam String otp) {
        User tempUser = (User) request.getSession().getAttribute("tempUser");
        if (!tempUser.getOtp().toString().equals(otp)) {
            request.getSession().setAttribute("error", "❌Invalid verification code. Please try again.");
            return "redirect:/register/send/email";
        }
        request.getSession().setAttribute("message", "Email verified successfully!");
        request.getSession().removeAttribute("otp");
        request.getSession().removeAttribute("tempUser");
        attachmentRepository.save(tempUser.getAttachment());
        userRepository.save(tempUser);
        System.out.println("✅✅✅Email verified successfully!✅✅✅");

        return "redirect:/login";

    }

}
