package codigo.ms_auth.service.impl;

import codigo.ms_auth.aggregates.response.UsuarioResponse;
import codigo.ms_auth.entity.Rol;
import codigo.ms_auth.entity.Usuario;
import codigo.ms_auth.repository.UsuarioRepository;
import codigo.ms_auth.service.TestService;
import codigo.ms_auth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioResponse> ListSuperadmin() {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getRol()== Rol.SUPERADMIN)
                .map(usuario -> new UsuarioResponse(
                        usuario.getNombre(),
                        usuario.getCorreo(),
                        usuario.getRol()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioResponse> ListAdmin() {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getRol()== Rol.ADMIN)
                .map(usuario -> new UsuarioResponse(
                        usuario.getNombre(),
                        usuario.getCorreo(),
                        usuario.getRol()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioResponse> ListUser() {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getRol()== Rol.USUARIO)
                .map(usuario -> new UsuarioResponse(
                        usuario.getNombre(),
                        usuario.getCorreo(),
                        usuario.getRol()
                ))
                .collect(Collectors.toList());
    }
}
