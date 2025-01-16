package exrf.pos.dto.requests.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequestDto {
    @NotBlank(message = "Name must not be blank")
    private String name;

    private String description;
}
