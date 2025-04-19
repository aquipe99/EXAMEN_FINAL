package codigo.ms_auth.service;

import codigo.ms_auth.aggregates.response.UsuarioResponse;

import java.util.List;

public interface TestService {
    List<UsuarioResponse> ListSuperadmin();
    List<UsuarioResponse> ListAdmin();
    List<UsuarioResponse> ListUser();
}
