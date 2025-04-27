package codigo.ms_ordenes.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-productos")
public interface ProductosClient {
    @GetMapping("/productos/{id}")
    ResponseEntity<Boolean> getProductoById(@PathVariable("id") Long id);
}
