package com.teamsolution.lab.grpc;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GrpcServiceDiscovery {

  private final EurekaClient eurekaClient;

  public String getGrpcTarget(String serviceName) {
    // Use EurekaClient to discover the service instance and return the gRPC target string
    Application application = eurekaClient.getApplication(serviceName.toUpperCase());

    if (application == null || application.getInstances().isEmpty()) {
      throw new RuntimeException("No instances found for service: " + serviceName);
    }

    InstanceInfo instanceInfo =
        application
            .getInstances()
            .get(0); // Get the first instance (you can implement load balancing here)

    String ip = instanceInfo.getIPAddr();

    String grpcPort =
        instanceInfo.getMetadata().get("grpcPort"); // Assuming you have grpcPort in metadata

    if (grpcPort == null) {
      throw new RuntimeException("gRPC port not found in metadata for service: " + serviceName);
    }

    return ip + ":" + grpcPort;
  }
}
