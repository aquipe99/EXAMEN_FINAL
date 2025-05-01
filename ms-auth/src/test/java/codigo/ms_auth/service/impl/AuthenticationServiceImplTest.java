package codigo.ms_auth.service.impl;

import codigo.ms_auth.aggregates.request.SignInRequest;
import codigo.ms_auth.aggregates.request.SignUpRequest;
import codigo.ms_auth.aggregates.response.SignInResponse;
import codigo.ms_auth.aggregates.response.UsuarioResponse;
import codigo.ms_auth.entity.Rol;
import codigo.ms_auth.entity.Usuario;
import codigo.ms_auth.repository.UsuarioRepository;
import codigo.ms_auth.service.JwtService;
import codigo.ms_auth.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;

    private Usuario usuario;
    private SignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setCorreo("test@example.com");
        usuario.setNombre("Test");
        usuario.setPassword("test1234");
        usuario.setRol(Rol.USUARIO);

        signUpRequest= new SignUpRequest();
        signUpRequest.setCorreo("test@example.com");
        signUpRequest.setNombre("Test");
        signUpRequest.setPassword("test1234");
        signUpRequest.setRol(Rol.USUARIO);

    }

    @Test
    void testResgitrarUsuario(){
        //arrange preparar_todo lo necesario para el test
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any())).thenReturn(usuario);
        //action
        Usuario resultado = authenticationServiceImpl.signUpUser(signUpRequest);

        // assert
        assertNotNull(resultado);
        assertEquals(signUpRequest.getCorreo(), resultado.getCorreo());
        assertEquals(signUpRequest.getNombre(), resultado.getNombre());
        assertEquals(signUpRequest.getRol(), resultado.getRol());
        assertEquals(signUpRequest.getPassword(), resultado.getPassword());
    }
    @Test
    void testErrorRegistrarUsuario(){
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.of(usuario));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationServiceImpl.signUpUser(signUpRequest);
        });
        assertEquals("El correo ya esta registrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));


    }

    @Test
    void testSignIn_UsuarioValido(){
        SignInRequest signInRequest = new SignInRequest("correo@prueba.com", "password123");
        usuario.setCorreo("correo@prueba.com");
        usuario.setPassword("password123");
        UserDetails userDetail = new org.springframework.security.core.userdetails.User(
                usuario.getCorreo(),
                usuario.getPassword(),
                new ArrayList<>()
        );

        when(usuarioRepository.findByCorreo(signInRequest.getCorreo()))
                .thenReturn(Optional.of(usuario));
        when(usuarioService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(usuario.getUsername()))
                .thenReturn(userDetail);
        when(jwtService.generateToken(userDetail, usuario)).thenReturn("token-jwt-generado");

        SignInResponse response = authenticationServiceImpl.signIn(signInRequest);
        // Assert
        assertNotNull(response);
        assertEquals("token-jwt-generado", response.getAccessToken());

        verify(authenticationManager).authenticate(any());
        verify(usuarioRepository).findByCorreo(signInRequest.getCorreo());
        verify(jwtService).generateToken(userDetail, usuario);
    }
    @Test
    void testSignIn_UsuarioNoExiste(){
        SignInRequest signInRequest = new SignInRequest("correo@inexistente.com", "passwordIncorrecto");

        when(usuarioRepository.findByCorreo(signInRequest.getCorreo()))
                .thenReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authenticationServiceImpl.signIn(signInRequest);
        });

        assertEquals("Error no se encontro el usuario", exception.getMessage());

    }
    @Test
    // token valido devuelve los datos del usuario
    void testValidaToken_TokenValido(){
        String token = "Bearer token-valido";
        String tokenLimpio = "token-valido";
        String correo = "usuario@gmail.com";

        Usuario usuarioMock = new Usuario();
        usuarioMock.setNombre("usuario");
        usuarioMock.setCorreo("usuario@gmail.com");
        usuarioMock.setRol(Rol.ADMIN);

        when(jwtService.extractUserName(tokenLimpio)).thenReturn(correo);
        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.of(usuarioMock));

        UsuarioResponse resultado= authenticationServiceImpl.ValidateToken(token);

        assertNotNull(resultado);
        assertEquals("usuario", resultado.getNombre());
        assertEquals("usuario@gmail.com", resultado.getCorreo());
        assertEquals(Rol.ADMIN, resultado.getRol());

    }
    @Test
    void testValidaToken_TokenInvalido(){
        RuntimeException exceptionNull = assertThrows(RuntimeException.class, () -> {
            authenticationServiceImpl.ValidateToken(null);
        });
        assertEquals("Token invalido o nulo", exceptionNull.getMessage());

        RuntimeException exceptionTokenNoValido = assertThrows(RuntimeException.class, () -> {
            authenticationServiceImpl.ValidateToken("TokenInvalidoSinBearer");
        });
        assertEquals("Token invalido o nulo", exceptionTokenNoValido.getMessage());

    }

    @Test
    void testValidaToken_UsuarioNoExiste(){
        String token = "Bearer token-valido";
        String tokenLimpio = "token-valido";
        String correo = "usuario@correo.com";

        when(jwtService.extractUserName(tokenLimpio)).thenReturn(correo);
        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authenticationServiceImpl.ValidateToken(token);
        });
        assertEquals("Usuario no encontrado", exception.getMessage());

    }
    @Test
    void testValidateToken_ExtractUserNameDevuelveNull() {
        String token = "Bearer token-valido";
        String tokenLimpio = "token-valido";

        // Simular que extractUserName devuelve null
        when(jwtService.extractUserName(tokenLimpio)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationServiceImpl.ValidateToken(token);
        });

        assertEquals("Token invalido o nulo", exception.getMessage());
    }

}