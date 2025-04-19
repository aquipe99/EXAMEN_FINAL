package codigo.ms_auth.service;

import codigo.ms_auth.aggregates.request.SignInRequest;
import codigo.ms_auth.aggregates.request.SignUpRequest;
import codigo.ms_auth.aggregates.response.SignInResponse;
import codigo.ms_auth.aggregates.response.UsuarioResponse;
import codigo.ms_auth.entity.Usuario;

public interface AuthenticationService {
    Usuario signUpUser(SignUpRequest signUpRequest);
    SignInResponse signIn(SignInRequest signInRequest);
    UsuarioResponse ValidateToken(String token);

}
