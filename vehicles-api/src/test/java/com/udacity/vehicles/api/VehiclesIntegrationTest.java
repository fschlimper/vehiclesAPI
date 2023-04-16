package com.udacity.vehicles.api;

import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class VehiclesIntegrationTest {

    private final static Condition CAR_CONDITION = Condition.USED;
    private final static Location CAR_LOCATION = new Location(0d, 0d);
    private final static String CAR_BODY = "Sedan";
    private final static String CAR_MODEL = "Ibiza";
    private final static Integer CAR_NUMBER_OF_DOORS = 5;
    private final static String CAR_FUEL_TYPE = "unleaded";
    private final static String CAR_ENGINE = "engine";
    private final static Integer CAR_MILEAGE = 40432;
    private final static Integer CAR_MODEL_YEAR = 2019;
    private final static Integer CAR_PRODUCTION_YEAR = 2020;
    private final static String CAR_EXTERNAL_COLOR = "blue";
    private final static Integer CAR_MANUFACTURER_CODE = 3;
    private final static String CAR_MANUFACTURER_NAME = "Seat";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateVehicle() {
        ResponseEntity<Car> response = callCarCreationService(createCar());
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Car car = response.getBody();
        assertCarIsComplete(car);
        assertNotNull(car.getId());

        response = restTemplate.getForEntity("http://localhost:" + port + "/cars/" + car.getId(), Car.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        car = response.getBody();
        assertCarIsComplete(car);
        assertNotNull(car.getLocation().getAddress());
        assertNotNull(car.getPrice());
    }

    @Test
    public void testMultipleSelect() {
        IntStream.range(0, 10).forEach(i -> {
            ResponseEntity<Car> resp = callCarCreationService(createCar());
            assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        });
        ResponseEntity<Car> cars =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/cars",
                        Car.class);
        assertEquals(HttpStatus.OK, cars.getStatusCode());
    }

    @Test
    public void testDelete() {
        ResponseEntity<Car> resp = callCarCreationService(createCar());
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        Car car = resp.getBody();

        resp = restTemplate.getForEntity("http://localhost:" + port + "/cars/" + car.getId(), Car.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());

        restTemplate.delete("http://localhost:" + port + "/cars/" + car.getId());

        resp = restTemplate.getForEntity("http://localhost:" + port + "/cars/" + car.getId(), Car.class);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    private ResponseEntity<Car> callCarCreationService(Car car) {
        HttpEntity<Car> carEntity = new HttpEntity<>(createCar());
        ResponseEntity<Car> response = restTemplate.postForEntity("http://localhost:" + port + "/cars/newCar", carEntity, Car.class);
        return response;
    }

    private void assertCarIsComplete(Car car) {
        Details details = car.getDetails();
        assertEquals(CAR_CONDITION, car.getCondition());

        assertEquals(CAR_LOCATION.getLat(), car.getLocation().getLat());
        assertEquals(CAR_LOCATION.getLon(), car.getLocation().getLon());

        assertCarDetails(details);
    }

    private void assertCarDetails(Details details) {
        assertNotNull(details);
        assertEquals(CAR_BODY, details.getBody());
        assertEquals(CAR_MODEL, details.getModel());
        assertEquals(CAR_NUMBER_OF_DOORS, details.getNumberOfDoors());
        assertEquals(CAR_FUEL_TYPE, details.getFuelType());
        assertEquals(CAR_ENGINE, details.getEngine());
        assertEquals(CAR_MILEAGE, details.getMileage());
        assertEquals(CAR_MODEL_YEAR, details.getModelYear());
        assertEquals(CAR_EXTERNAL_COLOR, details.getExternalColor());

        Manufacturer manufacturer = details.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(CAR_MANUFACTURER_CODE, manufacturer.getCode());
        assertEquals(CAR_MANUFACTURER_NAME, manufacturer.getName());
    }

    private Car createCar() {
        Manufacturer manufacturer = new Manufacturer(CAR_MANUFACTURER_CODE, CAR_MANUFACTURER_NAME);

        Details details = new Details();
        details.setManufacturer(manufacturer);
        details.setBody(CAR_BODY);
        details.setModel(CAR_MODEL);
        details.setNumberOfDoors(CAR_NUMBER_OF_DOORS);
        details.setFuelType(CAR_FUEL_TYPE);
        details.setEngine(CAR_ENGINE);
        details.setMileage(CAR_MILEAGE);
        details.setModelYear(CAR_MODEL_YEAR);
        details.setProductionYear(CAR_PRODUCTION_YEAR);
        details.setExternalColor(CAR_EXTERNAL_COLOR);

        Car car = new Car();
        car.setDetails(details);
        car.setCondition(CAR_CONDITION);
        car.setCreatedAt(LocalDateTime.now());
        car.setLocation(CAR_LOCATION);

        return car;
    }

}
