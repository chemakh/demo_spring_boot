package com.example.service;

import java.util.List;

import com.example.domain.News;

public interface NewsService {

	public List<News> getAllNews();

	public News createNews(News news);

}
