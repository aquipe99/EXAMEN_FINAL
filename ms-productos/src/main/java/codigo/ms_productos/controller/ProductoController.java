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
    public ResponseEntity<Producto> createProduct(@RequestHeader("Authorization") String token, @RequestBody Producto producto){
        return  ResponseEntity.ok(productoService.createProduct(token,producto));
    }
    @GetMapping
    public ResponseEntity<List<Producto>> ListProduct(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(productoService.listProducto(token));
    }
    @GetMapping("/{id}")
    public  ResponseEntity<Boolean> getProductoById(@PathVariable Long id){
        return ResponseEntity.ok(productoService.getProductoById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProduct(@RequestHeader("Authorization") String token,@PathVariable Long id, @RequestBody Producto producto){
        return ResponseEntity.ok(productoService.updateProducto(token,id,producto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@RequestHeader("Authorization") String token,@PathVariable Long id){
        Map<String, String> response = new HashMap<>();
        boolean valid= productoService.deleteProducto(token,id);
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
