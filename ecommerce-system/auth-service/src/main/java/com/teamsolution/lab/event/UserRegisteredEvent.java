package com.teamsolution.lab.event;

import com.teamsolution.lab.util.UuidGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisteredEvent {
    @Builder.Default
    private UUID eventId = UuidGenerator.generate();

    private String email;

    private String rawOtp;

    @Builder.Default
    private String timeStamp = Instant.now().toString();
}
