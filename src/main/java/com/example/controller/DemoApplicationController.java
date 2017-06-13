package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.domain.News;
import com.example.domain.Tags;
import com.example.domain.User;
import com.example.service.NewsService;
import com.example.service.TagsService;
import com.example.service.UserService;

@RestController
public class DemoApplicationController {

	public final static String directoryPath = "C:" + File.separator + "Users" + File.separator + "User"
			+ File.separator + "workspace" + File.separator + "demo" + File.separator + "src" + File.separator + "main"
			+ File.separator + "resources" + File.separator + "static" + File.separator + "files" + File.separator;

	@Resource
	private UserService userService;

	@Resource
	private JavaMailSender javaMailSender;

	@Resource
	private NewsService newsService;

	@Resource
	private TagsService tagsService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}

	public void setTagsService(TagsService tagsService) {
		this.tagsService = tagsService;
	}

	@RequestMapping(value = "news", method = RequestMethod.GET)
	public List<News> getAllNews() {

		List<News> news = this.newsService.getAllNews();
		return news;
	}

	@RequestMapping(value = "tags", method = RequestMethod.GET)
	public List<Tags> getAllTags() {

		List<Tags> tags = this.tagsService.getAllTags();
		return tags;
	}

	@Secured("ADMIN")
	@RequestMapping(value = "addTags", method = RequestMethod.POST)
	public ResponseEntity<Tags> createTag(@Valid @RequestBody Tags tag) {

		if (tag != null) {
			tag = this.tagsService.createTags(tag);
			return ResponseEntity.ok(tag);

		} else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/addNews", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<News> createNews(@Valid @RequestPart(required = false) MultipartFile file,
			@Valid @RequestPart News news) throws IllegalStateException, IOException {

		if (file != null) {

			File directory = new File(directoryPath + file.getOriginalFilename());
			file.transferTo(directory);
			news.setFilePath(file.getOriginalFilename());
		}

		// from set Id to set tags in one instruction
		news.setTags(news.getTagIds().stream().map(id -> {
			
				try {
					return tagsService.findById(id).orElseThrow(()-> new Exception("The tag with id "+id+" not found"));
				} catch (Exception e) {					
					e.printStackTrace();
					return null;
				}
			

		}).collect(Collectors.toSet()));

		if (news != null) {

			news.setCreationDate(new Date());
			news = this.newsService.createNews(news);
			sendMessageTo(news.getValue());
			sendMail(news.getValue(), (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			return ResponseEntity.ok(news);

		} else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

		if (user != null && user.getName() != null && user.getPassword() != null
				&& this.userService.getUserByName(user.getName()) == null) {
			return ResponseEntity.ok(this.userService.addUser(user));
		} else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	@Resource
	private SimpMessagingTemplate template;

	public void setTemplate(SimpMessagingTemplate template) {
		this.template = template;
	}

	private void sendMail(String newsValue, User userFrom) {

		List<String> to = new ArrayList<String>();
		for (User user : userService.getAllUsers()) {
			if (!userFrom.getName().equals(user.getName()))
				to.add(user.getName() + "@localhost");
		}

		String[] toArray = new String[to.size()];
		to.toArray(toArray);

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(toArray);
		mailMessage.setReplyTo(userFrom.getName() + "@localhost");
		mailMessage.setFrom(userFrom.getName() + "@localhost");
		mailMessage.setSubject("A new Info is added");
		mailMessage.setText("Info Text: " + newsValue);
		javaMailSender.send(mailMessage);
	}

	private void sendMessageTo(String message) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(dateFormatter.format(new Date()));
		builder.append("] ");
		builder.append(message);

		this.template.convertAndSend("/topic/message", builder.toString());
	}
}
