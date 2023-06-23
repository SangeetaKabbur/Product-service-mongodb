package com.example.productservice.mongodb.repository;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.example.productservice.mongodb.domain.Item;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

	private final Logger logger=LogManager.getLogger(OrderRepositoryimpl.class);

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MongoOperations mongoOperations;

	@Override
	public void saveAll(List<Item> list, Class<Item> class1) {
		logger.info("ItemRepositoryImpl.saveAll()");
		mongoOperations.insert(list,class1);
	}

	@Override
	public List<Item> getSellerDocuments(List<String> itemNumbers, String sellerId) {
		logger.info("ItemRepositoryImpl.getSellerDocumentsByKeyValues()");
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("itemNumber").in(itemNumbers),
				Criteria.where("sellerId").is(sellerId));
		return mongoOperations.find(new Query(criteria), Item.class);
	}

	@Override
	public void save(Item item) {
		logger.info("ItemRepositoryImpl.save()");
		mongoOperations.save(item);

	}







}
