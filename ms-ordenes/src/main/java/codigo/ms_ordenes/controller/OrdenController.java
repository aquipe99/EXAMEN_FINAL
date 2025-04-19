package codigo.ms_ordenes.controller;

import codigo.ms_ordenes.entity.Orden;
import codigo.ms_ordenes.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenController {
    private final OrdenService ordenService;
    @PostMapping
    public ResponseEntity<Orden> createOrden(@RequestBody Orden orden){
        Orden nuevaOrden=ordenService.createOrden(orden);
        return ResponseEntity.ok(nuevaOrden);
    }
    @GetMapping ResponseEntity<List<Orden>> ListOrden(){
        List<Orden> ordenes= ordenService.ListOrden();
        return ResponseEntity.ok(ordenes);
    }

}
