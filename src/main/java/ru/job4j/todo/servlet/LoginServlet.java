package ru.job4j.todo.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    JsonObject json = new JsonObject();
    String email = req.getParameter("username");
    String password = req.getParameter("password");
    User user = HbmStore.instOf().findUserByUsername(email);
    if (user != null && user.getPassword().equals(password)) {
        req.getSession().setAttribute("user", user);
        json.addProperty("result", true);
    } else {
        json.addProperty("msg", "Неверный email или пароль");
        json.addProperty("result", false);
    }
    PrintWriter writer = new PrintWriter(resp.getOutputStream());
    writer.println(json);
    writer.flush();
}
}
