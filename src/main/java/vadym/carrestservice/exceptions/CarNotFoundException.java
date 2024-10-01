package vadym.carrestservice.exceptions;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(Long carId) {
        super("Car not found with ID: " + carId);
    }

    public CarNotFoundException(String make) { super ("Car not found with make: " + make); }
}
