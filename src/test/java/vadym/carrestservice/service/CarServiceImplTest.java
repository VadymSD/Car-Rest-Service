package vadym.carrestservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import vadym.carrestservice.dto.CarSearchCriteria;
import vadym.carrestservice.entity.Car;
import vadym.carrestservice.repository.CarRepository;
import vadym.carrestservice.service.impl.CarServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CarServiceImpl.class)
class CarServiceImplTest {

    @MockBean
    CarRepository carRepository;

    @Autowired
    CarServiceImpl carService;

    @Test
    void searchCarsTest() {
        CarSearchCriteria criteria = new CarSearchCriteria("make", "model", 2000, 2022, "category");
        Pageable pageable = PageRequest.of(1, 1, Sort.by("id"));
        List<Car> cars = Collections.singletonList(new Car());
        Page<Car> page = new PageImpl<>(cars, pageable, cars.size());

        when(carRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        carService.searchCars(criteria, pageable);

        verify(carRepository).findAll(any(Specification.class), any(Pageable.class));
    }
}
