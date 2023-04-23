package net.javaguide.springboot.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {

	private Integer id;
	private String contact;
	private String email;
	private String first_name;
	private String last_name;
	private String status;
	public UserWrapper(Integer id, String contact, String email, String first_name, String last_name, String status) {

		this.id = id;
		this.contact = contact;
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
		this.status = status;
	}

	

}
