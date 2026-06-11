package com.consulter.infrastructure.web;

import com.consulter.application.exception.MemberAlreadyExistsException;
import com.consulter.application.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_returnsMapWithErrorMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Movement not found: abc-123");

        Map<String, String> result = handler.handleNotFound(ex);

        assertThat(result).containsEntry("error", "Movement not found: abc-123");
    }

    @Test
    void handleMemberAlreadyExists_returnsMapWithErrorMessage() {
        MemberAlreadyExistsException ex = new MemberAlreadyExistsException("User already member of account");

        Map<String, String> result = handler.handleMemberAlreadyExists(ex);

        assertThat(result).containsEntry("error", "User already member of account");
    }
}
