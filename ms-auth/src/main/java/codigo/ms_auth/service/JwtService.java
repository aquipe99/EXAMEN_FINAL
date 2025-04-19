package codigo.ms_auth.service;

import codigo.ms_auth.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractUserName(String token);
    String generateToken(UserDetails userDetails, Usuario usuario);
    boolean validateToken(String token, UserDetails userDetails);
}
