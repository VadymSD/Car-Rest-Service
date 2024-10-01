package vadym.carrestservice.service;

import org.springframework.data.domain.Pageable;
import vadym.carrestservice.dto.CarDto;
import vadym.carrestservice.dto.CarSearchCriteria;
import vadym.carrestservice.entity.Car;

import java.util.List;
import java.util.Optional;

public interface CarService extends CommonService<Car, CarDto> {
    Optional<Car> findByMake(String make);

    List<Car> searchCars(CarSearchCriteria carSearchCriteria, Pageable pageable);
}
