package vadym.carrestservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarSearchCriteria {

    private String make;

    private String model;

    private Integer minYear;

    private Integer maxYear;

    private String category;
}
