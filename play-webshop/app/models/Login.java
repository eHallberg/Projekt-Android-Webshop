package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class Login {
	@Id
	public int id;
	public String username;
	public String password;
}
