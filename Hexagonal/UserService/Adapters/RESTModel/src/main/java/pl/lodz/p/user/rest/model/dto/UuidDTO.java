package pl.lodz.p.user.rest.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UuidDTO {
    @NotBlank(message = "UUID cannot be blank")
    private String uuid;

    public UUID uuid(){
        return UUID.fromString(uuid);
    }
}
