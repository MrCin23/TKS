package pl.lodz.p.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
