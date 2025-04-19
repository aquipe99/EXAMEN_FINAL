package codigo.ms_ordenes.repository;

import codigo.ms_ordenes.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden,Long> {
}
