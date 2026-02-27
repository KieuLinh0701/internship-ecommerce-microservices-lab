package com.teamsolution.demo.grpc;

import com.teamsolution.lab.grpc.email.EmailRequest;
import com.teamsolution.lab.grpc.email.EmailResponse;
import com.teamsolution.lab.grpc.email.EmailServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@GrpcService
@RequiredArgsConstructor
public class EmailGrpcService extends EmailServiceGrpc.EmailServiceImplBase {

  private final JavaMailSender mailSender;

  @Override
  public void sendEmail(EmailRequest request, StreamObserver<EmailResponse> responseObserver) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(request.getTo());
      message.setSubject(request.getSubject());
      message.setText(request.getBody());

      mailSender.send(message);

      EmailResponse response =
          EmailResponse.newBuilder().setSuccess(true).setMessage("Email sent successfully").build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      EmailResponse response =
          EmailResponse.newBuilder()
              .setSuccess(false)
              .setMessage("Failed to send email: " + e.getMessage())
              .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }
}
