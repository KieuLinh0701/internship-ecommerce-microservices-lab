package com.teamsolution.lab.service;

import com.teamsolution.lab.grpc.EmailGrpcClient;
import com.teamsolution.lab.grpc.notification.EmailRequest;
import com.teamsolution.lab.repository.NotificationRepository;
import com.teamsolution.lab.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationTemplateRepository templateRepository;
  private EmailGrpcClient emailClientService;

  public boolean sendEmail(EmailRequest request) {
    // 1. Lấy template
    //        NotificationTemplate template =
    // templateRepository.findByEventType(request.getEventType())
    //                .orElseThrow(() -> new RuntimeException("Template not found for event: " +
    // request.getEventType()));
    //
    //        // 2. Chèn template_vars vào template
    //        String subject = applyTemplate(template.getSubject(), request.getTemplateVars());
    //        String body = applyTemplate(template.getBodyTemplate(), request.getTemplateVars());
    //
    //        // 3. Lưu notification vào bảng notifications
    //        Notification notification = Notification.builder()
    //                .userId(request.getUserId())
    //                .type(request.getEventType())
    //                .channel(template.getChannel())
    //                .title(subject)
    //                .body(body)
    //                .status("PENDING")
    //                .retryCount(0)
    //                .createdAt(LocalDateTime.now())
    //                .build();
    //        notificationRepository.save(notification);
    //
    //        // 4. Gọi Email Service gRPC
    //        boolean success = emailClientService.sendEmail(request.getTo(), subject, body);
    //
    //        // 5. Update notification status
    //        notification.setStatus(success ? "SENT" : "FAILED");
    //        notification.setSentAt(success ? LocalDateTime.now() : null);
    //        notificationRepository.save(notification);
    //
    //        return success;

    return true;
  }
}
