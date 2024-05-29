package com.nhom44.api.auth;

import com.google.gson.Gson;
import com.nhom44.bean.User;
import com.nhom44.services.MailService;
import com.nhom44.services.UserService;
import com.nhom44.services.VerifyService;
import com.nhom44.util.StringUtil;
import com.nhom44.validator.EmailSingleValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@WebServlet(urlPatterns = {"/api/login", "/api/logout"})
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = new EmailSingleValidator().validator(req.getParameter("email")) ? req.getParameter("email") : "";
        String password = req.getParameter("password") != null && !req.getParameter("password").isEmpty() ? req.getParameter("password") : "";
        User user = UserService.getInstance().login(email, password);
        System.out.println(user.toString());
        if (user != null && Objects.equals(user.getEmail(), email)
                && Objects.equals(user.getPassword(), StringUtil.hash(password))) {
            if (user.getStatus() == 2) {
                resp.setStatus(403);
                resp.getWriter().print(new Gson().toJson("Tài khoản của bạn đã bị khóa"));
                resp.getWriter().flush();
                return;
            }
            if (user.getStatus() == 0) {
                String token = UUID.randomUUID().toString();
                VerifyService.getInstance().insert(token, user.getId());
                MailService.getInstance().sendMailToAGaig(null, user.getEmail(), token);
//                req.setAttribute("error", "Tài khoản của bạn chưa được kích hoạt vui lòng kiểm tra email để kích hoạt tài khoản");
                resp.setStatus(403);
                resp.getWriter().print(new Gson().toJson("Tài khoản của bạn chưa được kích hoạt vui lòng kiểm tra email để kích hoạt tài khoản"));
                resp.getWriter().flush();
                return;
            }
            req.getSession().setAttribute("auth", user);
            if (user.getRole() == 1) {
                resp.setStatus(200);
                resp.getWriter().print(new Gson().toJson(req.getContextPath() + "/admin"));
                resp.getWriter().flush();
                return;
            }
            if (user.getRole() == 0) {
                resp.setStatus(200);
                resp.getWriter().print(new Gson().toJson(req.getContextPath() + "/user"));
                resp.getWriter().flush();
                return;
            }
        }
        resp.setStatus(403);
        resp.getWriter().print(new Gson().toJson("Sai thông tin đăng nhập hoặc mật khẩu"));
        resp.getWriter().flush();
    }
}
