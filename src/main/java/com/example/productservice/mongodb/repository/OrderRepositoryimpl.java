package com.example.productservice.mongodb.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import com.example.productservice.mongodb.constants.OrderStatus;
import com.example.productservice.mongodb.domain.Item;
import com.example.productservice.mongodb.domain.Order;
import com.example.productservice.mongodb.dto.AggregationCount;
import com.example.productservice.mongodb.dto.CustomerOrder;
import com.example.productservice.mongodb.dto.OrderDto;
import com.example.productservice.mongodb.dto.SellerItemsDto;
import com.example.productservice.mongodb.dto.SellerOrder;

import lombok.extern.java.Log;

@Repository
@Log
public class OrderRepositoryimpl implements OrderRepository {

	private final Logger logger=LogManager.getLogger(OrderRepositoryimpl.class);

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MongoOperations mongoOperations;

	@Override
	public Order save(Order order) {
		log.info("OrderRepositoryimpl.save()");
		return mongoTemplate.save(order);
	}

	@Override
	public Page<CustomerOrder> getCustomerOrders(String customerId, String search, Pageable pageable) {
		log.info("OrderRepositoryimpl.getCustomerOrders()");
		try {
			List<AggregationOperation> aggregationOperations = new ArrayList<>();
			List<Document> conditions = new ArrayList<>();
			conditions.add(new Document("customerId",customerId));

			if (StringUtils.isNotBlank(search)) {
				conditions.add(new Document("$or", Arrays.asList(new Document("orderNumber",
						new Document("$regex", search).append("$options", "i")),
						new Document("itemNumber",
								new Document("$regex", search).append("$options", "i")))));
			}

			aggregationOperations.add(input -> new Document("$match",
					new Document("$and", conditions)));

			aggregationOperations.add(i ->  new Document("$sort", new Document("createdDate", 1L)));
			aggregationOperations.add(input ->new Document("$project",new Document("orderNumber", 1L)
					.append("productId", 1L)
					.append("productName", 1L)
					.append("quantity", 1L)
					.append("sellerId", 1L)
					.append("itemNumber", 1L)
					.append("itemStatus", 1L)
					.append("createdDate", 1L)));	
			List<AggregationOperation> countAggregationOperations = new ArrayList<>(aggregationOperations);
			aggregationOperations.add(i -> new Document("$skip", (long) pageable.getPageNumber() * pageable.getPageSize()));
			aggregationOperations.add(i -> new Document("$limit", pageable.getPageSize()));
			countAggregationOperations.add(i -> new Document("$count", "count"));
			AggregationCount aggregationCount = mongoOperations.aggregate(Aggregation.
					newAggregation(countAggregationOperations), Item.class, AggregationCount.class).getUniqueMappedResult();
			Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
			List<CustomerOrder> orders = mongoOperations.aggregate(aggregation, Item.class, CustomerOrder.class)
					.getMappedResults();
			long totalproducts=0;
			if(aggregationCount!=null) {
				totalproducts=aggregationCount.getCount();
			}
			return new PageImpl<>(orders, pageable, totalproducts);
		}catch (Exception e) {
			logger.error("Exception while fetching products {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		}
	}

	@Override
	public Page<SellerOrder> getSellerOrders(String sellerId, String search, Pageable pageable, OrderStatus status) {
		log.info("OrderRepositoryimpl.getSellerOrders()");
		try {
			List<Criteria> criteriaList=new ArrayList<>();
			List<AggregationOperation> aggregationOperations=new ArrayList<>();
			Criteria criteria=new Criteria();
			criteriaList.add(Criteria.where("sellerId").is(sellerId));

			if(StringUtils.isNotBlank(search)) {
				Criteria searchCriteria=new Criteria();
				searchCriteria.orOperator(Criteria.where("orderNumber").regex(search,"i"),
						Criteria.where("customerName").regex(search,"i"));
				criteriaList.add(searchCriteria);
			}

			if (status != null)
				criteriaList.add(Criteria.where("itemStatus").is(status));
			if(!criteriaList.isEmpty()) {
				criteria.andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
				aggregationOperations.add(Aggregation.match(criteria));
			}
			aggregationOperations.add(Aggregation.group("customerId").count().as("numberOfItems")
					.first("customerId").as("customerId").first("customerName").as("customerName")
					.first("customerMobile").as("customerMobile").first("createdDate").as("createdDate"));
			aggregationOperations.add(Aggregation.sort(Direction.ASC,"createdDate"));
			aggregationOperations.add(Aggregation.project("customerId","customerName","customerMobile","numberOfItems",
					"createdDate"));
			List<AggregationOperation> countAggregationOperations=new ArrayList<>(aggregationOperations);
			countAggregationOperations.add(Aggregation.count().as("count"));
			AggregationCount aggregationCount = mongoOperations.aggregate(Aggregation.
					newAggregation(countAggregationOperations), Item.class, AggregationCount.class)
					.getUniqueMappedResult();
			aggregationOperations.add(new SkipOperation((long) pageable.getPageNumber() * pageable.getPageSize()));  
			aggregationOperations.add(Aggregation.limit(pageable.getPageSize()));
			Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
			List<SellerOrder> orders = mongoOperations.aggregate(aggregation, Item.class, SellerOrder.class)
					.getMappedResults();
			long totalproducts=0;
			if(aggregationCount!=null) {
				totalproducts=aggregationCount.getCount();
			}
			return new PageImpl<>(orders, pageable, totalproducts);
		}catch (Exception e) {
			logger.error("Exception while fetching products {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		}
	}

	@Override
	public Page<SellerItemsDto> getSellerItems(String customerId, String search, Pageable pageable, OrderStatus status) {
		try {
			List<AggregationOperation> aggregationOperations = new ArrayList<>();
			List<Document> conditions = new ArrayList<>();
			conditions.add(new Document("customerId",customerId));

			if (StringUtils.isNotBlank(search)) {
				conditions.add(new Document("$or", Arrays.asList(new Document("orderNumber",
						new Document("$regex", search).append("$options", "i")),
						new Document("itemNumber",
								new Document("$regex", search).append("$options", "i")))));
			}
			if (status != null ) {
			    conditions.add(new Document("itemStatus", status));
			}
			aggregationOperations.add(input -> new Document("$match",
					new Document("$and", conditions)));

			aggregationOperations.add(i ->  new Document("$sort", new Document("createdDate", 1L)));
			aggregationOperations.add(input ->new Document("$project",new Document("orderNumber", 1L)
					.append("productId", 1L)
					.append("productName", 1L)
					.append("quantity", 1L)
					.append("sellerId", 1L)
					.append("itemNumber", 1L)
					.append("itemStatus", 1L)
					.append("customerName",1L)
					.append("createdDate", 1L)));	
			List<AggregationOperation> countAggregationOperations = new ArrayList<>(aggregationOperations);
			aggregationOperations.add(i -> new Document("$skip", (long) pageable.getPageNumber() * pageable.getPageSize()));
			aggregationOperations.add(i -> new Document("$limit", pageable.getPageSize()));
			countAggregationOperations.add(i -> new Document("$count", "count"));
			AggregationCount aggregationCount = mongoOperations.aggregate(Aggregation.
					newAggregation(countAggregationOperations), Item.class, AggregationCount.class).getUniqueMappedResult();
			Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
			List<SellerItemsDto> orders = mongoOperations.aggregate(aggregation, Item.class, SellerItemsDto.class)
					.getMappedResults();
			long totalproducts=0;
			if(aggregationCount!=null) {
				totalproducts=aggregationCount.getCount();
			}
			return new PageImpl<>(orders, pageable, totalproducts);
		}catch (Exception e) {
			logger.error("Exception while fetching products {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		}
	} 
}