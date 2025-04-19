package codigo.ms_productos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String nombre;
private Double precio;
private String categoria;
}
