package com.teamsolution.common.grpc.executor;

import com.teamsolution.common.grpc.mapper.GrpcErrorMapper;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GrpcExecutor {

  private final GrpcErrorMapper errorMapper;
  private final GrpcRetryExecutor retryExecutor;

  public <T> T execute(Callable<T> callable) {
    return retryExecutor.execute(
        () -> {
          try {
            return callable.call();
          } catch (StatusRuntimeException e) {
            log.error(
                "gRPC call failed, status={}, description={}",
                e.getStatus().getCode(),
                e.getStatus().getDescription(),
                e);

            throw errorMapper.map(e);
          }
        });
  }
}
