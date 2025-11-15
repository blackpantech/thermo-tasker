package com.blackpantech.printer_module.domain;

import java.time.Instant;

public record Task(String topic, String description, Instant dueDate) {}
