package com.springboot.tennant;

import org.springframework.data.mongodb.repository.MongoRepository;

import model.Answers;

public interface UserRepository extends MongoRepository<Answers, String>{
	
	
	public Answers findByEmail(String email);
	
	public Answers findBy_id(String _id);

}
