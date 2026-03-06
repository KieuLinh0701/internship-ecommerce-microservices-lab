package com.teamsolution.lab.service;

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

  public void sendVerificationEmail(String email, String rawOtp) {
    send(email, "Verify your email", "Your OTP: " + rawOtp);
  }

  public void sendPasswordResetEmail(String email, String rawOtp) {
    send(email, "Reset your password", "Your OTP: " + rawOtp);
  }

  private void send(String to, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(body);
    mailSender.send(message);
    log.info("Email sent to: {}", to);
  }
}
