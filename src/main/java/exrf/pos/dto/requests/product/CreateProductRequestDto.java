package exrf.pos.dto.requests.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequestDto {
    @NotBlank(message = "Name must not be blank")
    private String name;

    private String description;

    private Long price;

    private String sku;

    private String categoryId;
}
