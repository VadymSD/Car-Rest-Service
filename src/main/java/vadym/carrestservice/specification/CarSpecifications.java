package vadym.carrestservice.specification;

import org.springframework.data.jpa.domain.Specification;
import vadym.carrestservice.entity.Car;

public class CarSpecifications {

    public static Specification<Car> hasManufacturer(String make) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("make")),
                        "%" + make.toUpperCase() + "%"
                );
    }

    public static Specification<Car> hasModel(String model) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("model")),
                        "%" + model.toUpperCase() + "%"
                );
    }

    public static Specification<Car> hasYearBetween(Integer minYear, Integer maxYear) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("year"), minYear, maxYear);
    }

    public static Specification<Car> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("category")),
                        "%" + category.toUpperCase() + "%"
                );
    }
}
