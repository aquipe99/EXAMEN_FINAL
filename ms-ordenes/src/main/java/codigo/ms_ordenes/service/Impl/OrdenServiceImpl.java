package codigo.ms_ordenes.service.Impl;

import codigo.ms_ordenes.aggregates.response.UsuarioResponse;
import codigo.ms_ordenes.entity.Orden;
import codigo.ms_ordenes.entity.Rol;
import codigo.ms_ordenes.repository.OrdenRepository;
import codigo.ms_ordenes.rest.AuthClient;
import codigo.ms_ordenes.rest.ProductosClient;
import codigo.ms_ordenes.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository ordenRepository;
    private final AuthClient authClient;
    private  final ProductosClient productosClient;

    @Override
    public Orden createOrden(String token,Orden orden) {
        UsuarioResponse usuarioResponse=authClient.validateToken(token);
        if(usuarioResponse.getRol()== Rol.USUARIO){
            for (Long productoId : orden.getProductosIds()) {
                ResponseEntity<Boolean> response = productosClient.getProductoById(productoId);
                if (response.getBody() == null || !response.getBody()) {
                    throw new RuntimeException("El producto con ID " + productoId + " no existe.");
                }
            }
            orden.setFecha(LocalDateTime.now());
            return ordenRepository.save(orden);
        }else {
            throw new RuntimeException("No tienes permisos para crear productos.");
        }
    }

    @Override
    public List<Orden> ListOrden(String token) {
        UsuarioResponse usuarioResponse=authClient.validateToken(token);
        if(usuarioResponse.getRol()== Rol.ADMIN || usuarioResponse.getRol() == Rol.SUPERADMIN){
            return ordenRepository.findAll();
        }else {
            throw new RuntimeException("No tienes permisos para crear productos.");
        }

    }
}
