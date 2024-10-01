package vadym.carrestservice.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import vadym.carrestservice.dto.CarDto;
import vadym.carrestservice.dto.CarSearchCriteria;
import vadym.carrestservice.entity.Car;
import vadym.carrestservice.exceptions.CarNotFoundException;
import vadym.carrestservice.security.SecurityConfig;
import vadym.carrestservice.service.CarService;

import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.List;

@WebMvcTest(CarController.class)
@Import(SecurityConfig.class)
class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarService carService;

    @Test
    @WithAnonymousUser
    void whenGetCars_thenReturnJsonArray() throws Exception {
        Car car = new Car();
        List<Car> carList = Collections.singletonList(car);

        given(carService.findAll(any(Pageable.class))).willReturn(carList);

        mvc.perform(get("/api/v1/cars/manufacturers")
                        .param("page", String.valueOf(1))
                        .param("pageSize", String.valueOf(1))
                        .param("sortBy", "no")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithAnonymousUser
    void whenSearchCars_thenReturnJsonArray() throws Exception {
        Car car = new Car();
        List<Car> carList = Collections.singletonList(car);

        given(carService.searchCars(any(CarSearchCriteria.class), any(Pageable.class))).willReturn(carList);

        mvc.perform(get("/api/v1//cars")
                        .param("make", "test")
                        .param("model", "test")
                        .param("minYear", String.valueOf(1))
                        .param("maxYear", String.valueOf(1))
                        .param("category", "test")
                        .param("page", String.valueOf(1))
                        .param("pageSize", String.valueOf(1))
                        .param("sortBy", "no")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "testUser")
    void whenCreateCar_thenReturnJsonCar() throws Exception {
        Car car = new Car();
        car.setMake("toyota");
        car.setModel("corolla");
        car.setYear(2010);
        car.setCategory("Sedan");

        given(carService.save(any(CarDto.class))).willReturn(car);

        mvc.perform(post("/api/v1/cars/manufacturers/toyota/models/corolla/year/2010/category/Sedan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("toyota"))
                .andExpect(jsonPath("$.model").value("corolla"))
                .andExpect(jsonPath("$.year").value(2010))
                .andExpect(jsonPath("$.category").value("Sedan"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void whenUpdateCar_thenReturnJsonCar() throws Exception {
        Car car = new Car();
        car.setMake("toyota");
        car.setModel("corolla");
        car.setYear(2010);
        car.setCategory("Sedan");

        given(carService.update(any(Long.class), any(CarDto.class))).willReturn(car);

        mvc.perform(put("/api/v1/cars/manufacturers/toyota/models/corolla/year/2010/category/Sedan/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("toyota"))
                .andExpect(jsonPath("$.model").value("corolla"))
                .andExpect(jsonPath("$.year").value(2010))
                .andExpect(jsonPath("$.category").value("Sedan"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void whenDeleteCar_thenReturnString() throws Exception {
        doNothing().when(carService).delete(any(Long.class));

        mvc.perform(delete("/api/v1/cars/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Car with ID 1 deleted Successfully"));
    }

    @Test
    @WithAnonymousUser
    void whenDeleteCarWithAnonymousUser_thenReturnUnauthorized() throws Exception {
        doNothing().when(carService).delete(any(Long.class));

        mvc.perform(delete("/api/v1/cars/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenDeleteCarWithWrongId_thenGetAnError() throws Exception {
        Mockito.doThrow(CarNotFoundException.class).when(carService).delete(any(Long.class));

        mvc.perform(delete("/api/v1/cars/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
