package codigo.ms_ordenes.service;

import codigo.ms_ordenes.entity.Orden;

import java.util.List;

public interface OrdenService {
    Orden createOrden (String token,Orden orden);
    List<Orden> ListOrden(String token);
}
