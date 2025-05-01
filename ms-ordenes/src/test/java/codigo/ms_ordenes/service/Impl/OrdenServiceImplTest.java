package codigo.ms_ordenes.service.Impl;

import codigo.ms_ordenes.aggregates.response.UsuarioResponse;
import codigo.ms_ordenes.entity.Orden;
import codigo.ms_ordenes.entity.Rol;
import codigo.ms_ordenes.repository.OrdenRepository;
import codigo.ms_ordenes.rest.AuthClient;
import codigo.ms_ordenes.rest.ProductosClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrdenServiceImplTest {

    @Mock
    private OrdenRepository ordenRepository;
    @Mock
    private AuthClient authClient;
    @Mock
    private ProductosClient productosClient;

    @InjectMocks
    private OrdenServiceImpl ordenServiceImpl;

    private String token;
    private Orden orden;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        token = "valid-token";
        orden = new Orden();
        orden.setProductosIds(List.of(1L, 2L));
    }
    @Test
    void testCreateOrden_UsuarioConPermisoYProductosValidos() {

        UsuarioResponse usuarioResponse = new UsuarioResponse("Usuario", "usuario@correo.com", Rol.USUARIO);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);

        when(productosClient.getProductoById(1L)).thenReturn(ResponseEntity.ok(true));
        when(productosClient.getProductoById(2L)).thenReturn(ResponseEntity.ok(true));

        Orden ordenGuardada = new Orden();
        ordenGuardada.setId(1L);
        when(ordenRepository.save(any(Orden.class))).thenReturn(ordenGuardada);


        Orden resultado = ordenServiceImpl.createOrden(token, orden);


        assertNotNull(resultado);
    }
    @Test
    void testCreateOrden_UsuarioSinPermiso_ADMIN() {

        UsuarioResponse usuarioResponse = new UsuarioResponse("Admin", "admin@correo.com", Rol.ADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);


        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ordenServiceImpl.createOrden(token, orden);
        });

        assertEquals("No tienes permisos para crear productos.", ex.getMessage());
    }
    @Test
    void testCreateOrden_UsuarioSinPermiso_USUARIO() {

        UsuarioResponse usuarioResponse = new UsuarioResponse("Cliente", "cliente@correo.com", Rol.SUPERADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);


        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ordenServiceImpl.createOrden(token, orden);
        });

        assertEquals("No tienes permisos para crear productos.", ex.getMessage());
    }
    @Test
    void testCreateOrden_ProductoNoExisteFalse() {

        UsuarioResponse usuarioResponse = new UsuarioResponse("Usuario", "usuario@correo.com", Rol.USUARIO);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);

        when(productosClient.getProductoById(1L)).thenReturn(ResponseEntity.ok(false));


        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ordenServiceImpl.createOrden(token, orden);
        });

        assertEquals("El producto con ID 1 no existe.", ex.getMessage());

    }
    @Test
    void testCreateOrden_ProductoNoExisteNull() {
        // Arrange
        UsuarioResponse usuarioResponse = new UsuarioResponse("Usuario", "usuario@correo.com", Rol.USUARIO);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);

        when(productosClient.getProductoById(1L)).thenReturn(ResponseEntity.ok(null));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ordenServiceImpl.createOrden(token, orden);
        });

        assertEquals("El producto con ID 1 no existe.", ex.getMessage());

    }
    @Test
    void testListOrden_UsuarioAdminDebeRetornarLista() {
        // Arrange
        UsuarioResponse usuarioResponse = new UsuarioResponse("Admin", "admin@correo.com", Rol.ADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);

        List<Orden> ordenes = List.of(new Orden(), new Orden());
        when(ordenRepository.findAll()).thenReturn(ordenes);

        // Act
        List<Orden> resultado = ordenServiceImpl.ListOrden(token);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

    }
    @Test
    void testListOrden_UsuarioSuperAdminDebeRetornarLista() {
        // Arrange
        UsuarioResponse usuarioResponse = new UsuarioResponse("Admin", "admin@correo.com", Rol.SUPERADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);

        List<Orden> ordenes = List.of(new Orden(), new Orden());
        when(ordenRepository.findAll()).thenReturn(ordenes);

        // Act
        List<Orden> resultado = ordenServiceImpl.ListOrden(token);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

    }
    @Test
    void testListOrden_UsuarioNoAutorizado() {

        UsuarioResponse usuarioResponse = new UsuarioResponse("Usuario", "usuario@correo.com", Rol.USUARIO);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ordenServiceImpl.ListOrden(token);
        });

        assertEquals("No tienes permisos para crear productos.", exception.getMessage());

    }
}