package org.project.onlinebookstore.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        int status,
        String message,
        String path,
        LocalDateTime timestamp,
        Map<String, String> errors
) {}
