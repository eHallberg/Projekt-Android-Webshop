package controllers;

import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import models.Login;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.login;

public class Application extends Controller {

	public static Result startpage() {
		return ok(index.render("Menu"));
	}

	public static Result showLoginForm() {
		return ok(login.render());
	}

	@Transactional
	public static Result login() {
		Map<String, String[]> form = request().body().asFormUrlEncoded();
		String username = form.get("username")[0];
		String password = form.get("password")[0];

		boolean usernameIsEmpty = "".equals(username);
		boolean passwordIsEmpty = "".equals(password);

		if (usernameIsEmpty || passwordIsEmpty) {
			if (usernameIsEmpty) {
				flash().put("username-empty", "yes");
			}
			if (passwordIsEmpty) {
				flash().put("password-empty", "yes");
			}

			return redirect(routes.Application.showLoginForm());
		}

		TypedQuery<Login> query = JPA
				.em()
				.createQuery(
						"SELECT c FROM Login c WHERE c.username = :username AND c.password = :password",
						Login.class);
		query.setParameter("username", username);
		query.setParameter("password", password);

		List<Login> matchingUsers = query.getResultList();

		if (matchingUsers.size() == 1) {
			session().put("username", username);

			return redirect(routes.Application.startpage());
		} else {
			flash().put("no-username-password-match", "yes");

			return unauthorized();
		}
	}

	@Transactional
	public static Result endSession() {
		session().clear();
		return ok("Bye");
	}

}
