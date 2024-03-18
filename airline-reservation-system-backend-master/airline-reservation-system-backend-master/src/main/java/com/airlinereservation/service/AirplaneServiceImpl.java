package com.airlinereservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airlinereservation.dao.AirplaneDao;
import com.airlinereservation.entity.Airplane;

@Service
public class AirplaneServiceImpl implements AirplaneService {
	
	@Autowired
	private AirplaneDao airplaneDao;

	@Override
	public Airplane add(Airplane airplane) {
		// TODO Auto-generated method stub
		return airplaneDao.save(airplane);
	}

	@Override
	public Airplane getById(int id) {
		// TODO Auto-generated method stub
		return airplaneDao.findById(id).get();
	}

	@Override
	public List<Airplane> getAll() {
		// TODO Auto-generated method stub
		return airplaneDao.findAll();
	}

}
