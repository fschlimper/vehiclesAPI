package com.udacity.pricing.domain.price;

import com.udacity.pricing.service.PriceException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public interface PriceRepository extends CrudRepository<Price, Long> {

}
