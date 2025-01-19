package exrf.pos.dto.requests;

import exrf.pos.model.enums.ERole;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Long id;
    private ERole name;
}
