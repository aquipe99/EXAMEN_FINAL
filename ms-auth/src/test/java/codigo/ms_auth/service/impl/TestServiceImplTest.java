package codigo.ms_auth.service.impl;

import codigo.ms_auth.aggregates.response.UsuarioResponse;
import codigo.ms_auth.entity.Rol;
import codigo.ms_auth.entity.Usuario;
import codigo.ms_auth.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TestServiceImplTest {
    @Mock
    private  UsuarioRepository usuarioRepository;

    @InjectMocks
    private TestServiceImpl testServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListSuperadmin(){
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("alex");
        usuario1.setCorreo("alex@gmail.com");
        usuario1.setRol(Rol.SUPERADMIN);
        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setNombre("ana");
        usuario2.setCorreo("ana@gmail.com");
        usuario2.setRol(Rol.SUPERADMIN);
        Usuario usuario3 = new Usuario();
        usuario3.setId(3L);
        usuario3.setNombre("bob");
        usuario3.setCorreo("bob@gmail.com");
        usuario3.setRol(Rol.USUARIO);

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2,usuario3);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        TestServiceImpl testService= new TestServiceImpl(usuarioRepository);

        List<UsuarioResponse> resultado = testService.ListSuperadmin();

        assertEquals(2,resultado.size());

        assertTrue(resultado.stream()
                .allMatch(resp -> resp.getRol() == Rol.SUPERADMIN));
        assertEquals("alex", resultado.get(0).getNombre());
        assertEquals("ana", resultado.get(1).getNombre());

    }
    @Test
    void testListSdmin(){
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("alex");
        usuario1.setCorreo("alex@gmail.com");
        usuario1.setRol(Rol.ADMIN);
        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setNombre("ana");
        usuario2.setCorreo("ana@gmail.com");
        usuario2.setRol(Rol.ADMIN);
        Usuario usuario3 = new Usuario();
        usuario3.setId(3L);
        usuario3.setNombre("bob");
        usuario3.setCorreo("bob@gmail.com");
        usuario3.setRol(Rol.USUARIO);

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2,usuario3);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        TestServiceImpl testService= new TestServiceImpl(usuarioRepository);

        List<UsuarioResponse> resultado = testService.ListAdmin();

        assertEquals(2,resultado.size());

        assertTrue(resultado.stream()
                .allMatch(resp -> resp.getRol() == Rol.ADMIN));
        assertEquals("alex", resultado.get(0).getNombre());
        assertEquals("ana", resultado.get(1).getNombre());

    }
    @Test
    void testListUsuario(){
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("alex");
        usuario1.setCorreo("alex@gmail.com");
        usuario1.setRol(Rol.USUARIO);
        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setNombre("ana");
        usuario2.setCorreo("ana@gmail.com");
        usuario2.setRol(Rol.USUARIO);
        Usuario usuario3 = new Usuario();
        usuario3.setId(3L);
        usuario3.setNombre("bob");
        usuario3.setCorreo("bob@gmail.com");
        usuario3.setRol(Rol.ADMIN);

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2,usuario3);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        TestServiceImpl testService= new TestServiceImpl(usuarioRepository);

        List<UsuarioResponse> resultado = testService.ListUser();

        assertEquals(2,resultado.size());

        assertTrue(resultado.stream()
                .allMatch(resp -> resp.getRol() == Rol.USUARIO));
        assertEquals("alex", resultado.get(0).getNombre());
        assertEquals("ana", resultado.get(1).getNombre());

    }
}