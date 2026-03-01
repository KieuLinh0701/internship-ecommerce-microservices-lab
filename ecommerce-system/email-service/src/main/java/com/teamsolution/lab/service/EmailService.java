package com.teamsolution.lab.service;

import com.teamsolution.lab.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  public void sendWelcomeEmail(UserRegisteredEvent event) {
    try {
      String subject = "Mã OTP xác thực tài khoản";
      String body = "Mã OTP của bạn là: " + event.getRawOtp() + "\n\n" +
              "Mã có hiệu lực trong 5 phút.\n" +
              "Vui lòng không chia sẻ mã này cho người khác.";

      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(event.getEmail());
      message.setSubject(subject);
      message.setText(body);

      mailSender.send(message);

    } catch (Exception e) {
      log.error("Email sending failed: {}", e.getMessage(), e);
      throw new RuntimeException("Email sending failed", e);
    }
  }
}
