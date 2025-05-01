package codigo.ms_productos.service.Impl;

import codigo.ms_productos.aggregates.response.UsuarioResponse;
import codigo.ms_productos.entity.Producto;
import codigo.ms_productos.entity.Rol;
import codigo.ms_productos.repository.ProductoRepository;
import codigo.ms_productos.rest.AuthClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductoServiceImplTest {
    @Mock
    private AuthClient authClient;
    @Mock
    private ProductoRepository productoRepository;
    @InjectMocks
    private ProductoServiceImpl productoServiceImpl;

    private Producto producto;
    private String token;
    private Producto producto1;
    private Producto producto2;
    private Long productoId;
    private Producto productoNuevo;
    private Producto productoExistente;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        producto = Producto.builder().id(1L).nombre("Producto 1").precio(100.0).categoria("Categoria A").build();
        producto1 = Producto.builder().id(1L).nombre("Producto 1").precio(100.0).categoria("Categoria A").build();
        producto2 = Producto.builder().id(2L).nombre("Producto 2").precio(200.0).categoria("Categoria B").build();
        token = "valid-token";
        productoId = 1L;
        productoNuevo = Producto.builder()
                .nombre("Actualizado")
                .precio(300.0)
                .categoria("Electrónica")
                .build();
        productoExistente = Producto.builder()
                .id(productoId)
                .nombre("Original")
                .precio(100.0)
                .categoria("Computadoras")
                .build();
    }
    @Test
    void testProducto_CrearProductoAdmin(){

        UsuarioResponse usuarioResponse= new UsuarioResponse("alex","alex@gmail.com", Rol.ADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);
        when(productoRepository.findByNombre(producto.getNombre())).thenReturn(Optional.empty());
        when(productoRepository.save(any())).thenReturn(producto);

        Producto resultado = productoServiceImpl.createProduct(token,producto);

        assertNotNull(resultado);
        assertEquals(producto.getNombre(), resultado.getNombre());
        assertEquals(producto.getPrecio(), resultado.getPrecio());
        assertEquals(producto.getCategoria(), resultado.getCategoria());
    }
    @Test
    void testProducto_CrearProductoSuperAdmin(){

        UsuarioResponse usuarioResponse= new UsuarioResponse("alex","alex@gmail.com", Rol.SUPERADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);
         when(productoRepository.findByNombre(producto.getNombre())).thenReturn(Optional.empty());
        when(productoRepository.save(any())).thenReturn(producto);

         Producto resultado = productoServiceImpl.createProduct(token,producto);

        assertNotNull(resultado);
        assertEquals(producto.getNombre(), resultado.getNombre());
        assertEquals(producto.getPrecio(), resultado.getPrecio());
        assertEquals(producto.getCategoria(), resultado.getCategoria());
    }
    @Test
    void testProducto_UsuarioSinPermiso(){
        UsuarioResponse usuarioResponse= new UsuarioResponse("alex","alex@gmail.com", Rol.USUARIO);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoServiceImpl.createProduct(token, producto);
        });

        assertEquals("No tienes permisos para crear productos.", exception.getMessage());
    }
    @Test
    void testCreateProduct_ProductoExistente() {

        when(authClient.validateToken(token)).thenReturn(new UsuarioResponse("Usuario 1","correo@prueba.com",Rol.ADMIN));

        when(productoRepository.findByNombre(producto.getNombre())).thenReturn(Optional.of(producto));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoServiceImpl.createProduct(token, producto);
        });

        assertEquals("Ya existe un producto con ese nombre", exception.getMessage());
    }

    @Test
    void testlistProducto_UsuarioAutorizadoAdmin(){
        UsuarioResponse usuarioResponse = new UsuarioResponse("Admin", "admin@correo.com", Rol.ADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);
        when(productoRepository.findAll()).thenReturn(List.of(producto1, producto2));

        List<Producto> resultado = productoServiceImpl.listProducto(token);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Producto 1", resultado.get(0).getNombre());
    }
    @Test
    void testlistProducto_UsuarioAutorizadoSuperadmin(){
        UsuarioResponse usuarioResponse = new UsuarioResponse("Admin", "admin@correo.com", Rol.SUPERADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);
        when(productoRepository.findAll()).thenReturn(List.of(producto1, producto2));

        List<Producto> resultado = productoServiceImpl.listProducto(token);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Producto 1", resultado.get(0).getNombre());
    }

    @Test
    void testlistProducto_UsuarioNoutorizado(){
        UsuarioResponse usuarioResponse = new UsuarioResponse("Cliente", "cliente@correo.com", Rol.USUARIO);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoServiceImpl.listProducto(token);
        });

        assertEquals("No tienes permisos para listar productos.", exception.getMessage());
    }

    @Test
    void updateProducto_UsuarioAutorizadoAmin(){

        UsuarioResponse usuarioResponse = new UsuarioResponse("Admin", "admin@correo.com", Rol.ADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);
        when(productoRepository.findById(productoId)).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Producto resultado = productoServiceImpl.updateProducto(token,productoId,productoNuevo);

        assertNotNull(resultado);
        assertEquals("Actualizado", resultado.getNombre());
        assertEquals(300.0, resultado.getPrecio());
        assertEquals("Electrónica", resultado.getCategoria());
        assertEquals(productoId, resultado.getId());
    }
    @Test
    void testUpdateProducto_UsuarioRolSuperAdmin() {
        UsuarioResponse usuarioResponse = new UsuarioResponse("SuperAdmin", "superadmin@correo.com", Rol.SUPERADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any())).thenReturn(producto);

        Producto result = productoServiceImpl.updateProducto(token, 1L, producto);

        assertNotNull(result);
        assertEquals(producto.getNombre(), result.getNombre());
    }
    @Test
    void testUpdateProducto_UsuarioNoAhotirizado() {


        UsuarioResponse usuarioResponse = new UsuarioResponse("Usuario", "usuario@correo.com", Rol.USUARIO);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);


        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productoServiceImpl.updateProducto(token, productoId, producto)
        );

        assertEquals("No tienes permisos para actualizar productos.", exception.getMessage());
    }
    @Test
    void testUpdateProducto_ProductoNoExiste() {
        // Arrange

        UsuarioResponse usuarioResponse = new UsuarioResponse("Admin", "admin@correo.com", Rol.ADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);
        when(productoRepository.findById(productoId)).thenReturn(Optional.empty());

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productoServiceImpl.updateProducto(token, productoId, producto)
        );

        assertEquals("Prodcuto no encontrado", exception.getMessage());

    }

    @Test
    void testDeleteProducto_Admin_ProductoExitse(){
        UsuarioResponse usuarioResponse = new UsuarioResponse("Admin", "admin@mail.com", Rol.ADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);
        when(productoRepository.existsById(1L)).thenReturn(true);

        boolean result = productoServiceImpl.deleteProducto(token, 1L);

        assertTrue(result);
    }
    @Test
    void testDeleteProducto_Superadmin_ProductoExitse(){
        UsuarioResponse usuarioResponse = new UsuarioResponse("Admin", "admin@mail.com", Rol.SUPERADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);
        when(productoRepository.existsById(1L)).thenReturn(true);

        boolean result = productoServiceImpl.deleteProducto(token, 1L);

        assertTrue(result);
    }
    @Test
    void testDeleteProducto_UsuarioSinPermisos() {
        UsuarioResponse usuarioResponse = new UsuarioResponse("Cliente", "cliente@mail.com", Rol.USUARIO);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoServiceImpl.deleteProducto(token, 1L);
        });

        assertEquals("No tienes permisos para eliminar productos.", exception.getMessage());

    }
    @Test
    void testDeleteProducto_SuperAdmin_ProductoNoExiste() {
        UsuarioResponse usuarioResponse = new UsuarioResponse("SuperAdmin", "superadmin@mail.com", Rol.SUPERADMIN);
        when(authClient.validateToken(token)).thenReturn(usuarioResponse);
        when(productoRepository.existsById(1L)).thenReturn(false);

        boolean result = productoServiceImpl.deleteProducto(token, 1L);

        assertFalse(result);
    }

    @Test
    void testGetProductoById(){
        Long id = 1L;
        when(productoRepository.existsById(id)).thenReturn(true);

        boolean result = productoServiceImpl.getProductoById(id);

        assertTrue(result);
    }
    @Test
    void testGetProductoById_NoExiste() {
        Long id = 2L;
        when(productoRepository.existsById(id)).thenReturn(false);

        boolean result = productoServiceImpl.getProductoById(id);

        assertFalse(result);

    }

}