package com.airlinereservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airlinereservation.entity.Airplane;

@Repository
public interface AirplaneDao extends JpaRepository<Airplane, Integer> {

}
