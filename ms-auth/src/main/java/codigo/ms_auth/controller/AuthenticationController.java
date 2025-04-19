package codigo.ms_auth.controller;

import codigo.ms_auth.aggregates.request.SignInRequest;
import codigo.ms_auth.aggregates.request.SignUpRequest;
import codigo.ms_auth.aggregates.response.SignInResponse;
import codigo.ms_auth.aggregates.response.UsuarioResponse;
import codigo.ms_auth.entity.Usuario;
import codigo.ms_auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.signUpUser(signUpRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authenticationService.signIn(signInRequest));
    }
    @GetMapping("validate")
    public ResponseEntity<UsuarioResponse> validate(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(authenticationService.ValidateToken(token));
    }

}
