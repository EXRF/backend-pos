package exrf.pos.dto.responses;

import exrf.pos.dto.ValidationErrorDto;

import java.util.List;

public class ValidationErrorResponseDto {
    private List<ValidationErrorDto> errors;

    public ValidationErrorResponseDto(List<ValidationErrorDto> errors) {
        this.errors = errors;
    }

    // Getter and Setter
    public List<ValidationErrorDto> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationErrorDto> errors) {
        this.errors = errors;
    }
}

