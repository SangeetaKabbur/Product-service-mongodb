package com.example.productservice.mongodb.repository;

import com.example.productservice.mongodb.domain.ProductUser;
import com.example.productservice.mongodb.domain.User;

public interface ProductUserRepository {

	User findUserById(String userId);

	ProductUser addProduct(ProductUser productUser);

}
