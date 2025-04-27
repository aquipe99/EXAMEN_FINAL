package codigo.ms_productos.service.Impl;

import codigo.ms_productos.aggregates.response.UsuarioResponse;
import codigo.ms_productos.entity.Producto;
import codigo.ms_productos.entity.Rol;
import codigo.ms_productos.repository.ProductoRepository;
import codigo.ms_productos.rest.AuthClient;
import codigo.ms_productos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private  final ProductoRepository productoRepository;
    private final AuthClient authClient;
    @Override
    public Producto createProduct(String token,Producto producto) {
        UsuarioResponse usuarioResponse=authClient.validateToken(token);
        if(usuarioResponse.getRol()== Rol.ADMIN || usuarioResponse.getRol() == Rol.SUPERADMIN)
        {
            Optional<Producto> productExist=productoRepository.findByNombre(producto.getNombre());
            if(productExist.isPresent()){
                throw new RuntimeException("Ya existe un producto con ese nombre");
            }
            Producto newProducto= Producto.builder()
                    .nombre(producto.getNombre())
                    .precio(producto.getPrecio())
                    .categoria(producto.getCategoria())
                    .build();
            return productoRepository.save(newProducto);
        }else {
            throw new RuntimeException("No tienes permisos para crear productos.");
        }
    }

    @Override
    public List<Producto> listProducto(String token) {
        UsuarioResponse usuarioResponse=authClient.validateToken(token);
        if(usuarioResponse.getRol()== Rol.ADMIN || usuarioResponse.getRol() == Rol.SUPERADMIN)
        {
            return productoRepository.findAll();
        }else {
            throw new RuntimeException("No tienes permisos para listar productos.");
        }

    }

    @Override
    public Producto updateProducto(String token,Long id, Producto producto) {
        UsuarioResponse usuarioResponse=authClient.validateToken(token);
        if(usuarioResponse.getRol()== Rol.ADMIN || usuarioResponse.getRol() == Rol.SUPERADMIN){
            Producto productExits= productoRepository.findById(id)
                    .orElseThrow(()-> new RuntimeException("Prodcuto no encontrado"));

            Producto updateProducto = Producto.builder()
                    .id(productExits.getId())
                    .nombre(producto.getNombre())
                    .precio(producto.getPrecio())
                    .categoria(producto.getCategoria())
                    .build();
            return productoRepository.save(updateProducto);
        }else {
            throw new RuntimeException("No tienes permisos para actualizar productos.");
        }

    }

    @Override
    public boolean deleteProducto(String token,Long id) {
        UsuarioResponse usuarioResponse=authClient.validateToken(token);
        if(usuarioResponse.getRol()== Rol.ADMIN || usuarioResponse.getRol() == Rol.SUPERADMIN){
            if(productoRepository.existsById(id)){
                productoRepository.deleteById(id);
                return true;
            }else {
                return false;
            }
        }else {
            throw new RuntimeException("No tienes permisos para eliminar productos.");
        }

    }

    @Override
    public boolean getProductoById(Long id) {
        return productoRepository.existsById(id);
    }
}
