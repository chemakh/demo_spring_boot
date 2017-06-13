package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.dao.TagsRepository;
import com.example.domain.Tags;

@Service(value = "tagsService")
public class TagsServiceImpl implements TagsService {

	@Resource
	private TagsRepository tagsRepository;
	

	public void setTagsRepository(TagsRepository tagsRepository) {
		this.tagsRepository = tagsRepository;
	}

	@Override
	public List<Tags> getAllTags() {
		List<Tags> list = new ArrayList<Tags>();
		this.tagsRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public Tags createTags(Tags tag) {
		return this.tagsRepository.save(tag);
	}

	@Override
	public Optional<Tags> findById(Long id) {
		
		 return tagsRepository.findOneById(id);
	}

}
