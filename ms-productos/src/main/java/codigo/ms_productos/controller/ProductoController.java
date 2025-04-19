package codigo.ms_productos.controller;

import codigo.ms_productos.entity.Producto;
import codigo.ms_productos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<Producto> createProduct(@RequestBody Producto producto){
        return  ResponseEntity.ok(productoService.createProduct(producto));
    }
    @GetMapping
    public ResponseEntity<List<Producto>> ListProduct(){
        return ResponseEntity.ok(productoService.listProducto());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProduct(@PathVariable Long id, @RequestBody Producto producto){
        return ResponseEntity.ok(productoService.updateProducto(id,producto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id){
        Map<String, String> response = new HashMap<>();
        boolean valid= productoService.deleteProducto(id);
        if(valid){
            response.put("mensaje", "Producto eliminado correctamente");
            return ResponseEntity.ok(response);
        }
        else{
            response.put("mensaje", "Producto no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
