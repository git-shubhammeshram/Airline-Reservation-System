package com.airlinereservation.service;

import java.util.List;

import com.airlinereservation.entity.Airplane;
import com.airlinereservation.entity.AirplaneSeatNo;

public interface AirplaneSeatNoService {
	
	List<AirplaneSeatNo> addAllSeat(List<AirplaneSeatNo> seats);
	List<AirplaneSeatNo> getByAirplane(Airplane airplane);
	

}
