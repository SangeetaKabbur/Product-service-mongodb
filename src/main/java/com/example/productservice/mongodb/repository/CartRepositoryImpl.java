package com.example.productservice.mongodb.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BsonNull;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import com.example.productservice.mongodb.domain.Cart;
import com.example.productservice.mongodb.dto.AggregationCount;
import com.example.productservice.mongodb.dto.CartInfo;


@Repository
public class CartRepositoryImpl implements CartRepository{

	private final Logger logger=LogManager.getLogger(CartRepositoryImpl.class);
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MongoOperations mongoOperations;

	@Override
	public Cart findByUserId(String userId) {
		logger.info("CartRepositoryImpl.findByUserId()");
		Query query=new Query();
		query.addCriteria(Criteria.where("customerId").is(userId));
		return mongoTemplate.findOne(query,Cart.class);
	}

	@Override
	public void save(Cart cart) {
		logger.info("CartRepositoryImpl.save()");
		mongoTemplate.save(cart);
	}

	@Override
	public CartInfo getCartByUserId(String userId) {
		logger.info("CartRepositoryImpl.getCartByUserId()");
		List<AggregationOperation> aggregationOperations = new ArrayList<>();
		List<Document> documents = Arrays.asList(new Document("$match", new Document("customerId",userId)), 
				new Document("$addFields", new Document("productsArray", new Document("$objectToArray", "$products"))),
				new Document("$unwind", new Document("path", "$productsArray")), 
				new Document("$addFields", new Document("productId", 
						new Document("$toObjectId", "$productsArray.k"))), new Document("$lookup",new Document("from", "product")
								.append("localField", "productId").append("foreignField", "_id")
								.append("as", "product")), new Document("$unwind", 
										new Document("path", "$product")), new Document("$addFields",  new Document("productId", 
												new Document("$toString", "$productId"))), new Document("$group", new Document("_id", 
														new BsonNull())
														.append("cartItems", new Document("$push", 
																new Document("productId", "$productId")
																.append("productName", "$product.name")
																.append("quantity", "$productsArray.v")))
														.append("size", 
																new Document("$sum", 1L))), new Document("$project", 
																		new Document("_id", 0L)
																		.append("cartProducts", "$cartItems")
																		.append("size", 1L)));
		documents.forEach(doc -> aggregationOperations.add(input -> doc));
		Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
		AggregationResults<CartInfo> aggregationResults = mongoOperations.aggregate(
				aggregation, Cart.class, CartInfo.class);

		CartInfo cartInfo = aggregationResults.getUniqueMappedResult();
		if (cartInfo == null) {
			cartInfo = new CartInfo();
		}
		return cartInfo;
	}

	@Override
	public Page<CartInfo> getCartList(Pageable pageable,String userId) {
		logger.info("CartRepositoryImpl.getCartList()");
		List<AggregationOperation> aggregationOperations = new ArrayList<>();
		aggregationOperations.add(input->new Document("$match", 
				new Document("customerId", userId)));
		List<AggregationOperation> countAggregationOperations = new ArrayList<>(aggregationOperations);
		countAggregationOperations.add(i -> new Document("$count", "count"));
		AggregationCount aggregationCount = mongoOperations.aggregate(Aggregation.newAggregation(countAggregationOperations), Cart.class, AggregationCount.class).getUniqueMappedResult();
		aggregationOperations.add(i -> new Document("$sort", new Document("createdDate", 1L)));
		aggregationOperations.add(i -> new Document("$skip", (long) pageable.getPageNumber() * pageable.getPageSize()));
		aggregationOperations.add(i -> new Document("$limit", pageable.getPageSize()));
		aggregationOperations.add(i -> new Document("$addFields", new Document("productsArray", new Document("$objectToArray", "$products"))));
		aggregationOperations.add(i -> new Document("$unwind", new Document("path", "$productsArray")));
		aggregationOperations.add(i -> new Document("$addFields", new Document("productId", new Document("$toObjectId", "$productsArray.k"))));
		aggregationOperations.add(i -> new Document("$lookup",new Document("from", "product")
				.append("localField", "productId").append("foreignField", "_id")
				.append("as", "product"))); 
		aggregationOperations.add(i -> new Document("$unwind", new Document("path", "$product")));
		aggregationOperations.add(i -> new Document("$addFields",  new Document("productId", new Document("$toString", "$productId"))));
		aggregationOperations.add(i -> new Document("$group", new Document("_id", new BsonNull()).append("cartItems", new Document("$push", 
				new Document("productId", "$productId")
				.append("productName", "$product.name")
				.append("quantity", "$productsArray.v"))).append("size", 
						new Document("$sum", 1L))));
		aggregationOperations.add(i -> new Document("$project", new Document("_id", 0L)
				.append("cartProducts", "$cartItems")
				.append("size", 1L)));
		Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
		AggregationResults<CartInfo> aggregationResults = mongoOperations.aggregate(
				aggregation, Cart.class, CartInfo.class);
		long totalProducts = 0;
		if (aggregationCount != null) {
			totalProducts = aggregationCount.getCount();
		}
		List<CartInfo> cartInfos = aggregationResults.getMappedResults();
		Page<CartInfo> pageResult = new PageImpl<>(cartInfos, pageable, totalProducts);
		return pageResult;
	}
}