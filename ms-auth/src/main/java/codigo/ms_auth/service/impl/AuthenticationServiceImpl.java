package codigo.ms_auth.service.impl;

import codigo.ms_auth.aggregates.constants.Constants;
import codigo.ms_auth.aggregates.request.SignInRequest;
import codigo.ms_auth.aggregates.request.SignUpRequest;
import codigo.ms_auth.aggregates.response.SignInResponse;
import codigo.ms_auth.aggregates.response.UsuarioResponse;
import codigo.ms_auth.entity.Usuario;
import codigo.ms_auth.repository.UsuarioRepository;
import codigo.ms_auth.service.AuthenticationService;
import codigo.ms_auth.service.JwtService;
import codigo.ms_auth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class AuthenticationServiceImpl implements AuthenticationService {
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @Override
    public Usuario signUpUser(SignUpRequest signUpRequest) {
        Optional<Usuario> usuarioOptional=usuarioRepository.findByCorreo(signUpRequest.getCorreo());
        if(usuarioOptional.isPresent()){
            throw  new RuntimeException("El correo ya esta registrado");
        }
        Usuario usuario=getUsuarioEntity(signUpRequest);
        return usuarioRepository.save(usuario);
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getCorreo(),signInRequest.getPassword()
        ));
        var usuario =usuarioRepository.findByCorreo(signInRequest.getCorreo()).orElseThrow(
                () -> new UsernameNotFoundException("Error no se encontro el usuario")
        );
        UserDetails userDetails=usuarioService.userDetailsService().loadUserByUsername(usuario.getUsername());
        var token =jwtService.generateToken(userDetails,usuario);
        return SignInResponse.builder()
                .accessToken(token)
                .build();
    }

    @Override
    public UsuarioResponse ValidateToken(String token) {
        if(token ==null || !token.startsWith("Bearer ")){
            throw new RuntimeException("Token invalido o nulo");
        }
        final String tokenLimpio;
        final String userEmail;
        tokenLimpio=token.substring(7);
        userEmail=jwtService.extractUserName(tokenLimpio);
        if(Objects.nonNull(userEmail)){
            Usuario usuario= usuarioRepository.findByCorreo(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            return new UsuarioResponse(
                    usuario.getNombre(),
                    usuario.getCorreo(),
                    usuario.getRol()
            );
        }
        throw  new RuntimeException("Token invalido o nulo");
    }

    private Usuario getUsuarioEntity(SignUpRequest signUpRequest){
        return Usuario.builder()
                .nombre(signUpRequest.getNombre())
                .correo(signUpRequest.getCorreo())
                .password(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()))
                .rol(signUpRequest.getRol())
                .isAccountNonExpired(Constants.STATUS_ACTIVE)
                .isAccountNonLocked(Constants.STATUS_ACTIVE)
                .isCredentialsNonExpired(Constants.STATUS_ACTIVE)
                .isEnabled(Constants.STATUS_ACTIVE)
                .build();
    }
}
