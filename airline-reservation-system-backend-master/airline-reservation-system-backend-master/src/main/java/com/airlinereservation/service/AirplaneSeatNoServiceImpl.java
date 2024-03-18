package com.airlinereservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airlinereservation.dao.AirplaneSeatNoDao;
import com.airlinereservation.entity.Airplane;
import com.airlinereservation.entity.AirplaneSeatNo;

@Service
public class AirplaneSeatNoServiceImpl implements AirplaneSeatNoService {

	@Autowired
	private AirplaneSeatNoDao airplaneSeatNoDao;
	
	@Override
	public List<AirplaneSeatNo> addAllSeat(List<AirplaneSeatNo> seats) {
		// TODO Auto-generated method stub
		return airplaneSeatNoDao.saveAll(seats);	
	}

	@Override
	public List<AirplaneSeatNo> getByAirplane(Airplane airplane) {
		// TODO Auto-generated method stub
		return airplaneSeatNoDao.findByAirplane(airplane);
	}

}
