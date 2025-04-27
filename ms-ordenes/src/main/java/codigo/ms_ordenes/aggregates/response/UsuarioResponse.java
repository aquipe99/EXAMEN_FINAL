package codigo.ms_ordenes.aggregates.response;

import codigo.ms_ordenes.entity.Rol;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioResponse {
    private String nombre;
    private String correo;
    @Enumerated(EnumType.STRING) private Rol rol;
}
