package com.example.productservice.mongodb.repository;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import com.example.productservice.mongodb.domain.Company;
import com.example.productservice.mongodb.domain.User;
import com.example.productservice.mongodb.dto.AggregationCount;
import com.example.productservice.mongodb.dto.UserInfoDto;


@Repository
public class UserRepositoryImpl implements UserRepository {

	private final Logger logger = LogManager.getLogger(UserRepositoryImpl.class);

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MongoOperations mongoOperations;
	@Override
	public User addUser(User user) {
		logger.info("UserRepositoryImpl.addUser()");
		return mongoTemplate.save(user);
	}

	@Override
	public boolean existsByEmail(String email) {
		logger.info("UserRepositoryImpl.existsByEmail()");
		Query query=new Query();
		query.addCriteria(Criteria.where("email").is(email));
		return mongoTemplate.exists(query,User.class);
	}
	@Override
	public boolean existsByMobileNumber(long mobileNumber) {
		logger.info("UserRepositoryImpl.existsByMobileNumber()");
		Query query=new Query();
		query.addCriteria(Criteria.where("mobileNumber").is(mobileNumber));
		return mongoTemplate.exists(query,User.class);
	}

	@Override
	public Company addCompany(Company company) {
		logger.info("UserRepositoryImpl.addCompany()");
		return mongoTemplate.save(company);
	}

	@Override
	public Page<UserInfoDto> getUserList(String search, Pageable pageable) {
		logger.info("UserRepositoryImpl.getUserList()");
		try {
			Criteria criteria = new Criteria();
			List<Criteria> criteriaList = new ArrayList<>();
			List<AggregationOperation> aggregationOperations = new ArrayList<>();
		
			if (StringUtils.isNotBlank(search)) {
				Criteria searchCriteria = new Criteria();
				searchCriteria.orOperator(Criteria.where("name").regex(search, "i"),
						Criteria.where("companyName").regex(search, "i"));
				criteriaList.add(searchCriteria);
			}
			
			aggregationOperations.add(Aggregation.sort(Sort.Direction.ASC, "name"));
			aggregationOperations.add(new AggregationOperation() {
				@Override
				public Document toDocument(AggregationOperationContext context) {
					return new Document("$addFields",
							new Document("userId", new Document("$toString", "$_id")));
				}
			});
			aggregationOperations.add(Aggregation.lookup("company", "userId", "userId", "company"));
			aggregationOperations.add(Aggregation.unwind("$company"));
			aggregationOperations.add(Aggregation
					.project( "name", "mobileNumber", "email","userId")
					.and("company._id").as("companyId")
					.and("company.companyName").as("companyName")
					.and("company.totalEmployees").as("totalEmployees"));

			if(!criteriaList.isEmpty()) {
				criteria.andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
				aggregationOperations.add(Aggregation.match(criteria));
			}

			List<AggregationOperation> countAggregationOperations = new ArrayList<>(aggregationOperations);
			aggregationOperations.add(new SkipOperation((long) pageable.getPageNumber() * pageable.getPageSize()));  
			aggregationOperations.add(Aggregation.limit(pageable.getPageSize()));
			Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

			List<UserInfoDto> user = mongoOperations.aggregate(aggregation,User.class,UserInfoDto.class)
					.getMappedResults();

			countAggregationOperations.add(Aggregation.count().as("count"));
			Aggregation countAggregation = Aggregation.newAggregation(countAggregationOperations);
			AggregationCount aggregationCount = mongoOperations.aggregate(countAggregation, User.class, AggregationCount.class).getUniqueMappedResult();
			long totalproducts=0;
			if(aggregationCount!=null) {
				totalproducts=aggregationCount.getCount();
			}
			return new PageImpl<>(user, pageable, totalproducts);
		}catch (Exception e) {
			logger.error("Exception while fetching products {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		}
	}

	@Override
	public User findByEmail(String email) {
		logger.info("UserRepositoryImpl.findByEmail()");
		Query query=new Query();
		query.addCriteria(Criteria.where("email").is(email));
		return mongoTemplate.findOne(query,User.class);
	}

	@Override
	public UserInfoDto getUserById(String userId) {
		logger.info("UserRepositoryImpl.getUserById()");
		Criteria criteria=Criteria.where("_id").is(userId);
		List<AggregationOperation> aggregationOperations = new ArrayList<>();
		aggregationOperations.add(Aggregation.match(criteria));
		aggregationOperations.add(new AggregationOperation() {
			@Override
			public Document toDocument(AggregationOperationContext context) {
				return new Document("$addFields",
						new Document("userId", new Document("$toString", "$_id")));
			}
		});
		aggregationOperations.add(Aggregation.lookup("company", "userId", "userId", "company"));
		aggregationOperations.add(Aggregation.unwind("$company"));
		aggregationOperations.add(Aggregation
				.project( "name", "mobileNumber", "email","userId")
				.and("company._id").as("companyId")
				.and("company.companyName").as("companyName")
				.and("company.totalEmployees").as("totalEmployees"));
		Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

		AggregationResults<UserInfoDto> aggregationResults = mongoOperations.aggregate(
				aggregation,User.class, UserInfoDto.class);

		return aggregationResults.getUniqueMappedResult();
	}

	@Override
	public User findById(String userId) {
		logger.info("UserRepositoryImpl.findById()");
		Query query=new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		return mongoTemplate.findOne(query,User.class);
	}
}