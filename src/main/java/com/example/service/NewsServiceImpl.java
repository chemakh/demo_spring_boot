package com.example.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.dao.NewsRepository;
import com.example.domain.News;

@Service(value = "newsService")
public class NewsServiceImpl implements NewsService {

	@Resource
	private NewsRepository newsRepository;

	public NewsRepository getNewsRepository() {
		return newsRepository;
	}

	public void setNewsRepository(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	@Override
	public List<News> getAllNews() {

		List<News> list = new ArrayList<News>();
		this.newsRepository.findAll().iterator().forEachRemaining(list::add);

		Collections.sort(list, new Comparator<News>() {

			@Override
			public int compare(News arg0, News arg1) {
				return arg1.getId().compareTo(arg0.getId());
			}
		});

		return list;
	}

	@Override
	public News createNews(News news) {
		return this.newsRepository.save(news);
	}

}
