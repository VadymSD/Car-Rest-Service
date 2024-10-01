package vadym.carrestservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long carId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "make")
    private String make;

    @NotNull
    @Size(max = 100)
    @Column(name = "year_column")
    private Integer year;

    @NotBlank
    @Size(max = 100)
    @Column(name = "model")
    private String model;

    @NotBlank
    @Size(max = 100)
    @Column(name = "category")
    private String category;
}
