package com.blackpantech.central_module.infrastructure.controller;

import java.time.LocalDateTime;

public record TaskForm(String topic, String description, LocalDateTime dueDate) {
}
