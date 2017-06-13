package com.example.domain;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.example.controller.DemoApplicationController;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class News implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	@NotNull
	private String value;

	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date creationDate;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "NewsTagsLink", joinColumns = {
			@javax.persistence.JoinColumn(name = "News_ID", referencedColumnName = "id") }, inverseJoinColumns = {
					@javax.persistence.JoinColumn(name = "Tags_ID", referencedColumnName = "id") })
	private Set<Tags> tags = new HashSet<Tags>();

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<Long> tagIds;

	private String filePath;

	public News() {
		super();
	}

	public News(String value, Date creationDate, Set<Tags> tags, String path) {
		super();
		this.value = value;
		this.creationDate = creationDate;
		this.tags = tags;
		this.filePath = path;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Set<Tags> getTags() {
		return tags;
	}

	public void setTags(Set<Tags> tags) {
		this.tags = tags;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<Long> getTagIds() {
		return tagIds;
	}

	public void setTagIds(List<Long> tagIds) {
		this.tagIds = tagIds;
	}

	public boolean isImage() {
		if (filePath != null && !filePath.isEmpty()) {
			File f = new File(DemoApplicationController.directoryPath + filePath);
			if (f.exists()) {

				try {
					Image image = ImageIO.read(f);
					if (image == null) {
						return false;
					} else
						return true;
				} catch (IOException ex) {
					return false;
				}
			} else
				return false;

		} else
			return false;

	}

	public boolean isPdf() {
		if (filePath != null && !filePath.isEmpty()) {
			File f = new File(DemoApplicationController.directoryPath + filePath);
			if (f.exists()) {
				return filePath.contains("pdf");

			} else
				return false;

		} else
			return false;

	}

}
