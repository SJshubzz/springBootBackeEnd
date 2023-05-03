package net.javaguide.springboot.POJO;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "bill")
public class Bill implements Serializable {
	private static final long serialVersionUID = -3945028194641151335L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "uuid")
	private String uuid;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "contactnumber")
	private String contactNumber;
	@Column(name = "paymentmethod")
	private String paymentMethod;
	@Column(name = "total")
	private String total;
	@Column(name = "productdetail",columnDefinition = "json")
	private String productDetail;
	@Column(name = "createdby")
	private String createdBy;




	
	
	

}
