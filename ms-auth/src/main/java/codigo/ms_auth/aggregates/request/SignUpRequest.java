package codigo.ms_auth.aggregates.request;

import codigo.ms_auth.entity.Rol;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SignUpRequest {
    private String nombre;
    private String correo;
    private String password;
    @Enumerated(EnumType.STRING) private Rol rol;
}
