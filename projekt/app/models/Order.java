package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "order_table")
public class Order {
	@Id
	public int product_id;
	public int quantity;
	public int users_id;

}
