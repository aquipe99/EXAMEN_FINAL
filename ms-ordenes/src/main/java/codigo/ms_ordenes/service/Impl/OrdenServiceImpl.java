package codigo.ms_ordenes.service.Impl;

import codigo.ms_ordenes.entity.Orden;
import codigo.ms_ordenes.repository.OrdenRepository;
import codigo.ms_ordenes.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository ordenRepository;
    @Override
    public Orden createOrden(Orden orden) {
        orden.setFecha(LocalDateTime.now());
        return ordenRepository.save(orden);
    }

    @Override
    public List<Orden> ListOrden() {
        return ordenRepository.findAll();
    }
}
