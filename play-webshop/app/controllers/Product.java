package controllers;

import java.util.List;
import java.util.Map;

import models.Order;
import models.Categories;
import models.Products;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.productEdit;
import views.html.productShowOne;

public class Product extends Controller {

	static int product_id;

	@Transactional
	public static Result productShowOne(int id) {
		product_id = id;
		List<Products> products = JPA
				.em()
				.createQuery("SELECT a from Products a WHERE a.id =" + id,
						Products.class).getResultList();

		return ok(productShowOne.render(products));
	}

	@Transactional
	@Security.Authenticated
	public static Result productEdit() {

		List<Categories> categories = JPA.em()
				.createQuery("SELECT a from Categories AS a", Categories.class)
				.getResultList();

		return ok(productEdit.render(categories));

	}

	@Transactional
	public static Result productListAll() {

		List<Products> products = JPA.em()
				.createQuery("SELECT a from Products AS a", Products.class)
				.getResultList();

		return ok(Json.toJson(products));
		// return ok(productListAll.render(products));

	}

	@Transactional
	@Security.Authenticated
	public static Result productCreateNew() {
		Map<String, String[]> form = request().body().asFormUrlEncoded();

		String name = form.get("name")[0];
		String description = form.get("description")[0];
		Integer price = Integer.parseInt(form.get("price")[0]);
		Integer category_id = Integer.parseInt(form.get("category-id")[0]);

		Products products = new Products();
		products.name = name;
		products.description = description;
		products.price = price;
		products.category_id = category_id;
		JPA.em().persist(products);

		return redirect(routes.Application.startpage());
	}

	@Transactional
	public static Result productAdd() {
		// ..
		Map<String, String[]> form = request().body().asFormUrlEncoded();
		Integer quantity = Integer.parseInt(form.get("quantity")[0]);
		Integer product_id = Integer.parseInt(form.get("product-id")[0]);

		Order basket = new Order();
		basket.quantity = quantity;
		basket.product_id = product_id;
		basket.users_id = 1;
		JPA.em().persist(basket);

		return redirect(routes.Application.startpage());
	}

}
