package codigo.ms_productos.service.Impl;

import codigo.ms_productos.entity.Producto;
import codigo.ms_productos.repository.ProductoRepository;
import codigo.ms_productos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private  final ProductoRepository productoRepository;
    @Override
    public Producto createProduct(Producto producto) {
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
    }

    @Override
    public List<Producto> listProducto() {
        return productoRepository.findAll();
    }

    @Override
    public Producto updateProducto(Long id, Producto producto) {
        Producto productExits= productoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Prodcuto no encontrado"));

        Producto updateProducto = Producto.builder()
                .id(productExits.getId())
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .categoria(producto.getCategoria())
                .build();
        return productoRepository.save(updateProducto);
    }

    @Override
    public boolean deleteProducto(Long id) {
        if(productoRepository.existsById(id)){
            productoRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
}
