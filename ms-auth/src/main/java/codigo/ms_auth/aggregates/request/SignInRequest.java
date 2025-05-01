package codigo.ms_auth.aggregates.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignInRequest {
    private String correo;
    private String password;
}
