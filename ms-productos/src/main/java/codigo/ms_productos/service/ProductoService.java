package codigo.ms_productos.service;

import codigo.ms_productos.entity.Producto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ProductoService {
    Producto createProduct(String token, Producto producto);
    List<Producto> listProducto(String token);
    Producto updateProducto(String token,Long id,Producto producto);
    boolean deleteProducto(String token,Long id);
    boolean getProductoById(Long id);
}
