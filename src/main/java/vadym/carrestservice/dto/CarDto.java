package vadym.carrestservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarDto {
    private String make;

    private Integer year;

    private String model;

    private String category;
}
