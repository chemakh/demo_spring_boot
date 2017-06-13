package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.domain.Tags;

public interface TagsService {
	
	public List<Tags> getAllTags();

	public Tags createTags(Tags tag);
	
	public Optional<Tags> findById(Long id);
	
}
