package controllers;

import java.util.List;
import java.util.Map;

import models.Categories;
import models.Products;
import play.libs.Json;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.categoryEdit;
import views.html.categoryShowOne;
import play.mvc.Security;

public class Category extends Controller {
	@Transactional
	public static Result categoryShowOne(int id) {
		List<Products> products = JPA
				.em()
				.createQuery(
						"SELECT a from Products a WHERE a.category_id = " + id,
						Products.class).getResultList();

		return ok(categoryShowOne.render(products));
	}

	@Transactional
	@Security.Authenticated
	public static Result categoryEdit() {

		List<Categories> categories = JPA.em()
				.createQuery("SELECT a from Categories AS a", Categories.class)
				.getResultList();

		return ok(categoryEdit.render(categories));

	}

	@Transactional
	public static Result categoryListAll() {

		List<Categories> categories = JPA.em()
				.createQuery("SELECT a from Categories AS a", Categories.class)
				.getResultList();

		return ok(Json.toJson(categories));

	}

	@Security.Authenticated
	@Transactional
	public static Result categoryCreateNew() {
		Map<String, String[]> form = request().body().asFormUrlEncoded();

		String name = form.get("name")[0];

		Categories categories = new Categories();
		categories.name = name;
		JPA.em().persist(categories);

		return redirect(routes.Application.startpage());

	}
}
