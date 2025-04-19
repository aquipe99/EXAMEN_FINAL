package codigo.ms_productos.service;

import codigo.ms_productos.entity.Producto;

import java.util.List;

public interface ProductoService {
    Producto createProduct(Producto producto);
    List<Producto> listProducto();
    Producto updateProducto(Long id,Producto producto);
    boolean deleteProducto(Long id);
}
