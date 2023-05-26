package com.example.productservice.mongodb.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;


import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.domain.Product;
import com.example.productservice.mongodb.dto.AggregationCount;
import com.example.productservice.mongodb.dto.ProductDto;
import com.mongodb.client.result.DeleteResult;

@Repository
public class ProductRepositoryImpl implements ProductRepository{

	private final Logger logger = LogManager.getLogger(ProductRepositoryImpl.class);

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MongoOperations mongoOperations;

	@Override
	public Product addProducts(Product product) {
		logger.info("ProductRepositoryImpl.addProducts()");
		return mongoTemplate.save(product);

	}

	@Override
	public List<Product> find() {
		logger.info("ProductRepositoryImpl.find()");
		return mongoTemplate.findAll(Product.class);
	}

	@Override
	public Product update(Product product) {
		logger.info("ProductController.update()");
		return (Product) mongoTemplate.findAll(Product.class);
	}

	@Override
	public Product delete(Product product) {
		logger.info("ProductRepositoryImpl.delete()");
		return (Product) mongoTemplate.remove(Product.class);
	}

	//	@Override
	//	public Product findByName(String name,String key,String value) {
	//		Query query=new Query();
	//		query.addCriteria(Criteria.where("name").is(name));
	//		
	//		Update update   =new Update();
	//		update.set(key,value);
	//		return mongoTemplate.upsert(query, update,Product.class);
	//		
	//	}

	@Override
	public Product save(Product product) {
		logger.info("ProductRepositoryImpl.save()");
		return mongoTemplate.save(product);
	}

	@Override
	public List<Product> getByPrice(BigDecimal price) {
		logger.info("ProductRepositoryImpl.getByPrice()");
		Query query=new Query();
		query.addCriteria(Criteria.where("price").gt(price));
		return mongoTemplate.find(query,Product.class);
	}

	@Override
	public void updateProduct(String name, ProductDto productDto) {
		logger.info("ProductRepositoryImpl.updateProduct()");

		Query query = new Query(Criteria.where("name").is(name));
		Update update = new Update();
		update.set("name", productDto.getName());
		update.set("price", productDto.getPrice());
		mongoTemplate.upsert(query, update, Product.class);

	}

	@Override
	public Product findById(String id) {
		logger.info("ProductRepositoryImpl.findById()");
		return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), Product.class);
	}

	@Override
	public Product findByName(String name, String key, String value) {
		logger.info("ProductRepositoryImpl.findByName()");
		return null;
	}

	@Override
	public Product findByKeyNameAndValue(String key, String value) {
		logger.info("ProductRepositoryImpl.findByKeyNameAndValue()");
		return mongoTemplate.findOne(new Query(Criteria.where(key).is(value)), Product.class);
	}

	@Override
	public List<Product> findProductsByKeyNameAndValue(String key, String value) {
		logger.info("ProductRepositoryImpl.findProductsByKeyNameAndValue()");
		return mongoTemplate.find(new Query(Criteria.where(key).is(value)), Product.class);
	}

	@Override
	public List<Product> getPaginatedProducts(int page, int size) {
		logger.info("ProductRepositoryImpl.getPaginatedProducts()");
		final Pageable pageableRequest=PageRequest.of(page, size);
		Query query=new Query();
		query.with(pageableRequest);
		return mongoTemplate.find(query,Product.class);
	}

	@Override
	public void deleteById(String key, String value) {
		logger.info("ProductRepositoryImpl.deleteById()");
		Query query=new Query();
		query.addCriteria(Criteria.where(key).is(value));
		mongoTemplate.remove(query,Product.class);
	}

	@Override
	public Product getProductByName(String key, String value) {
		logger.info("ProductRepositoryImpl.getProductByName()");
		Query query=new Query();
		query.addCriteria(Criteria.where(key).is(value));
		return mongoTemplate.findOne(new Query(Criteria.where(key).is(value)), Product.class);
	}

	//	@Override
	//	public List<Product> findByName(String name) {
	//		logger.info("ProductController.findByName()");
	//		if (name != null && !name.isEmpty()) {
	//			String trimmedPattern =name.trim();
	//			Query query = new Query(Criteria.where("name").regex(trimmedPattern));
	//			return mongoTemplate.find(query, Product.class);
	//		} else {
	//			return find();
	//		}
	//	}

	@Override
	public List<Product> findByName(String name) {
		logger.info("ProductRepositoryImpl.findByName()");
		if (name != null && !name.isEmpty()) {
			String searchTerm =name;
			//			String trimmedSearchTerm = searchTerm.replaceAll("\\s+", "").replaceAll("\\.", "");
			//			String regexPattern = "(?i)" + Pattern.quote(trimmedSearchTerm); 
			String trimmedSearchTerm = searchTerm.trim().replaceAll("\\s+", "\\\\s+").replaceAll("\\.", "");
			String regexPattern = "(?i).*" + trimmedSearchTerm + ".*";
			Query query = new Query(Criteria.where("name").regex(regexPattern));
			return mongoTemplate.find(query, Product.class);
		} else {
			return find();     
		}
	}

	@Override
	public DeleteResult deleteById(String id) {
		logger.info("ProductRepositoryImpl.deleteById()");
		Query query=new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		return mongoTemplate.remove(query, Product.class);
	}



	@Override
	public Page<ProductDto> getProductList( String search, Status status, Pageable pageable) {
		logger.info("ProductRepositoryImpl.getProductList()");
		try {
			Criteria criteria = new Criteria();
			List<Criteria> criteriaList = new ArrayList<>();
			List<AggregationOperation> aggregationOperations = new ArrayList<>();
			if (StringUtils.isNotBlank(search)) {
				criteriaList.add(Criteria.where("name").regex(search, "i"));
			}

			if (status != null)
				criteriaList.add(Criteria.where("status").is(status));
			if(!criteriaList.isEmpty()) {
				criteria.andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
				aggregationOperations.add(Aggregation.match(criteria));
			}
			aggregationOperations.add(Aggregation.project("id", "name","price").and("inventry").as("inventory"));
			//			aggregationOperations.add(Aggregation.match(Criteria.where("inventory").gt(70)));
			List<AggregationOperation> countAggregationOperations = new ArrayList<>(aggregationOperations);

			aggregationOperations.add(new SkipOperation((long) pageable.getPageNumber() * pageable.getPageSize()));  
			aggregationOperations.add(Aggregation.limit(pageable.getPageSize()));
			Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
			List<ProductDto> products = mongoOperations.aggregate(aggregation, Product.class, ProductDto.class)
					.getMappedResults();
			//					Query countQuery = new Query();
			//					countQuery.addCriteria(criteria);
			//					long totalproducts = mongoOperations.count(countQuery, Product.class);
			countAggregationOperations.add(Aggregation.count().as("count"));
			Aggregation countAggregation = Aggregation.newAggregation(countAggregationOperations);
			AggregationCount aggregationCount = mongoOperations.aggregate(countAggregation, Product.class, AggregationCount.class).getUniqueMappedResult();
			long totalproducts=0;
			if(aggregationCount!=null) {
				totalproducts=aggregationCount.getCount();
			}
			return new PageImpl<>(products, pageable, totalproducts);
		}catch (Exception e) {
			logger.error("Exception while fetching products {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		}
	}

	@Override
	public List<Product> findByStatus(Status active) {
		logger.info("ProductRepositoryImpl.findByStatus()");
		Criteria criteria = Criteria.where("status").is(active);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, Product.class);
	}

	@Override
	public List<Product> findByStatusAndPaginated(Status status, int page, int size) {
		Query query = new Query(Criteria.where("status").is(status))
				.with(Sort.by(Sort.Direction.DESC, "createdDate"))
				.with(PageRequest.of(page, size));
		return mongoTemplate.find(query, Product.class);
	}
}



