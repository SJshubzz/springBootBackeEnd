package net.javaguide.springboot.POJO;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name = "category.getAllCategory",query = "select c from category c")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "category")
@ConfigurationProperties
public class category implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "image")
	private String image;
	
	
	
	

}
