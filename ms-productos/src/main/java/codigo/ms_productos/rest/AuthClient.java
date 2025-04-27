package codigo.ms_productos.rest;

import codigo.ms_productos.aggregates.response.UsuarioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-auth")
public interface AuthClient {
    @GetMapping("/auth/validate")
    UsuarioResponse validateToken(@RequestHeader("Authorization") String token);
}
