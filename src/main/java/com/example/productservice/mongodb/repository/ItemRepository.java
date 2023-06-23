package com.example.productservice.mongodb.repository;

import java.util.List;

import com.example.productservice.mongodb.domain.Item;

public interface ItemRepository {

	public void saveAll(List<Item> list, Class<Item> class1);

	public void save(Item item);

	List<Item> getSellerDocuments(List<String> itemNumbers, String sellerId);
}