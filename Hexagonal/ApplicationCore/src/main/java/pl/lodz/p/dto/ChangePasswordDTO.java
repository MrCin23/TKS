package pl.lodz.p.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String oldPassword;
    @NotBlank
    @Size(min=6, max=20)
    private String newPassword;
}
