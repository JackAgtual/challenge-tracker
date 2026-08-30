package com.agtual.challengetracker.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;

@Configuration
public class OpenApiConfig {

    private static final Content problemDetailContent = new Content()
            .addMediaType("application/problem+json",
                    new MediaType().schema(
                            new Schema<ProblemDetail>().$ref("#/components/schemas/ProblemDetail")));

    @Bean
    OpenApiCustomizer globalErrorResponsesCustomizer() {
        return openApi -> {

            ResolvedSchema resolvedSchema = ModelConverters.getInstance()
                    .resolveAsResolvedSchema(
                            new AnnotatedType(ProblemDetail.class));

            openApi.getComponents().addSchemas(
                    "ProblemDetail",
                    resolvedSchema.schema);

            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                addApiResponseToOperation(operation, AlreadyExistsException.HTTP_STATUS, AlreadyExistsException.TITLE);
                addApiResponseToOperation(operation, ForbiddenException.HTTP_STATUS, ForbiddenException.TITLE);
                addApiResponseToOperation(operation, NotFoundException.HTTP_STATUS, NotFoundException.TITLE);
                addApiResponseToOperation(operation, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
            }));
        };
    }

    private void addApiResponseToOperation(Operation operation, HttpStatus httpStatus, String description) {
        operation.getResponses().addApiResponse(String.valueOf(httpStatus.value()),
                new ApiResponse().description(description)
                        .content(problemDetailContent));

    }
}
