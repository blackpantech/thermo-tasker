package com.blackpantech.central_module.domain;

import java.time.Instant;
import java.util.UUID;

public record Task(UUID id, String topic, String description, Instant dueDate) {
}
