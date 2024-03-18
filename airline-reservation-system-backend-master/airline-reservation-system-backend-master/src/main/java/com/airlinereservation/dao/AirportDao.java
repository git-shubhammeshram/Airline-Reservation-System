package com.airlinereservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airlinereservation.entity.Airport;

@Repository
public interface AirportDao  extends JpaRepository<Airport, Integer>{

}
