package com.example.productservice.mongodb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.example.productservice.mongodb.domain.ProductUser;
import com.example.productservice.mongodb.domain.User;

@Repository
public class ProductUserRepositoryImpl implements ProductUserRepository{

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public User findUserById(String userId) {
		Query query=new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		return mongoTemplate.findOne(query,User.class);
	}

	@Override
	public ProductUser addProduct(ProductUser productUser) {
		return mongoTemplate.save(productUser);
	}
	
}
