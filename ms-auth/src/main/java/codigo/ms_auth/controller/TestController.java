package codigo.ms_auth.controller;

import codigo.ms_auth.aggregates.response.UsuarioResponse;
import codigo.ms_auth.entity.Usuario;
import codigo.ms_auth.service.TestService;
import codigo.ms_auth.service.impl.TestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test/")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/superadmin")
    public ResponseEntity<List<UsuarioResponse>> listSuperadmin(){
        return ResponseEntity.ok(testService.ListSuperadmin());
    }
    @GetMapping("/admin")
    public ResponseEntity<List<UsuarioResponse>> listAdmin(){
        return ResponseEntity.ok(testService.ListAdmin());
    }
    @GetMapping("/user")
    public ResponseEntity<List<UsuarioResponse>> listuser(){
        return ResponseEntity.ok(testService.ListUser());
    }
}
