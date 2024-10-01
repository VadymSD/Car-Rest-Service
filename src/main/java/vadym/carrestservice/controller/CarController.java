package vadym.carrestservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vadym.carrestservice.dto.CarDto;
import vadym.carrestservice.dto.CarSearchCriteria;
import vadym.carrestservice.entity.Car;
import vadym.carrestservice.service.CarService;

import java.util.List;

@Tag(name = "CarRestController")
@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Operation(summary = "Get all cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car has been found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class)) })
    })
    @GetMapping("/manufacturers")
    public ResponseEntity<List<Car>> findAll(@ParameterObject Pageable pageable) {

        return ResponseEntity.ok(carService.findAll(pageable));
    }

    @Operation(summary = "Search cars with specified parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cars have been found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class)) })
    })
    @GetMapping
    public ResponseEntity<List<Car>> searchCars(@Parameter(description = "make of car to be searched")
                                                @RequestParam(required = false) String make,
                                                @Parameter(description = "model of car to be searched")
                                                @RequestParam(required = false) String model,
                                                @Parameter(description = "minimal year of car to be searched")
                                                @RequestParam(required = false) Integer minYear,
                                                @Parameter(description = "maximal year of car to be searched")
                                                @RequestParam(required = false) Integer maxYear,
                                                @Parameter(description = "category of car to be searched")
                                                @RequestParam(required = false) String category,
                                                @ParameterObject Pageable pageable) {

        CarSearchCriteria carSearchCriteria = new CarSearchCriteria(make, model, minYear, maxYear, category);

        return ResponseEntity.ok(carService.searchCars(carSearchCriteria, pageable));
    }

    @Operation(summary = "Create a car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car has been created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class)) })
    })
    @PostMapping("/manufacturers/{make}/models/{model}/year/{year}/category/{category}")
    public ResponseEntity<Car> createCar(@Parameter(description = "make of car to be created") @PathVariable String make,
                                         @Parameter(description = "model of car to be created") @PathVariable String model,
                                         @Parameter(description = "year of car to be created") @PathVariable Integer year,
                                         @Parameter(description = "category of car to be created") @PathVariable String category) {
        CarDto car = CarDto.builder()
                .make(make)
                .model(model)
                .year(year)
                .category(category)
                .build();

        return ResponseEntity.ok(carService.save(car));
    }

    @Operation(summary = "Update a car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car has been updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "404", description = "Car with specified id has not been not found",
                    content = @Content)
    })
    @PutMapping("/manufacturers/{make}/models/{model}/year/{year}/category/{category}/{id}")
    public ResponseEntity<Car> updateCar(@Parameter(description = "make of car to be updated") @PathVariable String make,
                                         @Parameter(description = "model of car to be updated") @PathVariable String model,
                                         @Parameter(description = "year of car to be updated") @PathVariable Integer year,
                                         @Parameter(description = "category of car to be updated") @PathVariable String category,
                                         @Parameter(description = "id of car to be updated") @PathVariable Long id) {
        CarDto car = CarDto.builder()
                .make(make)
                .model(model)
                .year(year)
                .category(category)
                .build();

        return ResponseEntity.ok(carService.update(id, car));
    }

    @Operation(summary = "Delete a car by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car has been deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "404", description = "Car with specified id has not been not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@Parameter(description = "id of car to be deleted") @PathVariable Long id) {
        carService.delete(id);

        return ResponseEntity.ok("Car with ID " + id + " deleted Successfully");
    }
}
