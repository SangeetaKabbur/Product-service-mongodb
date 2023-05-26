package com.example.productservice.mongodb.dto;

import java.util.Collection;

import org.springframework.hateoas.Links;

import lombok.Data;

@Data
public class CustomPagination {

    private Collection<?> content;

    private int pageSize;

    private Long totalElements;

    private int pageNumber;

    private int totalPages;

    private Links links;

	public CustomPagination(Collection<?> content, int pageSize, Long totalElements, int pageNumber, int totalPages, Links links) {
		super();
		this.content = content;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.pageNumber = pageNumber;
		this.totalPages = totalPages;
		this.links = links;
	}

	public CustomPagination() {
		super();
	}
    
    

}
