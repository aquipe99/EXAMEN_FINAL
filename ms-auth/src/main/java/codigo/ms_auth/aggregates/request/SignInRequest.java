package codigo.ms_auth.aggregates.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest {
    private String correo;
    private String password;
}
