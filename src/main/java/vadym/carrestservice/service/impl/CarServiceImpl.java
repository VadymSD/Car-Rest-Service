package vadym.carrestservice.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vadym.carrestservice.dto.CarDto;
import vadym.carrestservice.dto.CarSearchCriteria;
import vadym.carrestservice.entity.Car;
import vadym.carrestservice.exceptions.CarNotFoundException;
import vadym.carrestservice.repository.CarRepository;
import vadym.carrestservice.service.CarService;
import vadym.carrestservice.specification.CarSpecifications;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Optional<Car> findByMake(String make){
        Optional<Car> car = carRepository.findByMake(make);

        if (car.isPresent()) {
            logger.info("Car with make {} found", make);
        } else {
            logger.warn("Car with make {} not found", make);
            throw new CarNotFoundException(make);
        }

        return car;
    }

    @Override
    public Optional<Car> findById(Long id) {
        Optional<Car> car = carRepository.findById(id);

        if (car.isPresent()) {
            logger.info("Car with id {} found", id);
        } else {
            logger.warn("Car with id {} not found", id);
            throw new CarNotFoundException(id);
        }

        return car;
    }

    @Override
    public List<Car> searchCars(CarSearchCriteria criteria, Pageable pageable) {
        Specification<Car> spec = buildSpecification(criteria);

        try {
            Page<Car> pagedResult = carRepository.findAll(spec, pageable);
            logger.info("Found {} cars matching search criteria", pagedResult.getTotalElements());
            return getCarsFromPage(pagedResult);
        } catch (RuntimeException e) {
            logger.error("Error searching cars", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Car> findAll(Pageable pageable) {
        try {
            Page<Car> pagedResult = carRepository.findAll(pageable);
            logger.info("Found {} total cars", pagedResult.getTotalElements());
            return getCarsFromPage(pagedResult);
        } catch (RuntimeException e) {
            logger.error("Error searching cars", e);
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public Car save(CarDto dto) {
        Car car = Car.builder()
                .make(dto.getMake())
                .model(dto.getModel())
                .year(dto.getYear())
                .category(dto.getCategory())
                .build();

        try {
            Car savedCar = carRepository.save(car);
            logger.info("New car saved successfully: {}", savedCar);
            return savedCar;
        } catch (RuntimeException e) {
            logger.error("Error saving car", e);
        }

        return car;
    }

    @Override
    @Transactional
    public Car update(Long id, CarDto dto) {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException(id));

        car.setMake(dto.getMake());
        car.setModel(dto.getModel());
        car.setYear(dto.getYear());
        car.setCategory(dto.getCategory());

        try {
            Car savedCar = carRepository.save(car);
            logger.info("Car with ID {} updated successfully", id);
            return savedCar;
        } catch (RuntimeException e) {
            logger.error("Error saving car", e);
        }

        return car;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Car> car = carRepository.findById(id);

        if (car.isPresent()) {
            carRepository.deleteById(id);
            logger.info("Car with ID {} deleted successfully", id);
        } else {
            logger.warn("Car with ID {} not found, deletion failed", id);
            throw new CarNotFoundException(id);
        }
    }

    private Specification<Car> buildSpecification(CarSearchCriteria criteria) {
        Specification<Car> spec = Specification.where(null);

        if (criteria.getMake() != null) {
            spec = spec.and(CarSpecifications.hasManufacturer(criteria.getMake()));
        }
        if (criteria.getModel() != null) {
            spec = spec.and(CarSpecifications.hasModel(criteria.getModel()));
        }
        if (criteria.getMinYear() != null && criteria.getMaxYear() != null) {
            spec = spec.and(CarSpecifications.hasYearBetween(criteria.getMinYear(), criteria.getMaxYear()));
        }
        if (criteria.getCategory() != null) {
            spec = spec.and(CarSpecifications.hasCategory(criteria.getCategory()));
        }

        return spec;
    }

    private List<Car> getCarsFromPage(Page<Car> pagedResult) {
        return pagedResult.hasContent() ? pagedResult.getContent() : Collections.emptyList();
    }
}
