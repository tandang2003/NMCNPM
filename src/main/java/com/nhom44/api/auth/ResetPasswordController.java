package com.nhom44.api.auth;

import com.google.gson.Gson;
import com.nhom44.bean.ResponseModel;
import com.nhom44.bean.User;
import com.nhom44.services.MailService;
import com.nhom44.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet(
        urlPatterns = {"/api/reset-password"}
)
public class ResetPasswordController extends HttpServlet {


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameterMap().keySet().toString());
        String email = req.getParameter("email");
        System.out.println(email);
        ResponseModel responseModel;
        PrintWriter out = resp.getWriter();
        if (email == null || email.isEmpty()) {
            resp.setStatus(400);
            responseModel = new ResponseModel();
            responseModel.setMessage("Email không được để trống");
            responseModel.setName("email");
            out.println(new Gson().toJson(responseModel));
            out.flush();
            out.close();
            return;
        }
        User user = UserService.getInstance().getUserByEmailForCustomer(email);
        if (user == null) {
            resp.setStatus(400);
            responseModel = new ResponseModel();
            responseModel.setMessage("Tài khoản không tồn tại");
            responseModel.setName("email");
            out.println(new Gson().toJson(responseModel));
            out.flush();
            out.close();
            return;
        }
        String newPw = UUID.randomUUID().toString().substring(0, 9).trim();
        System.out.println(newPw);
        int i=UserService.getInstance().update(user.getEmail(), user.getEmail(), newPw, user.getFullName(), user.getBirthday(), user.getPhone(), user.getProvinceId(), user.getGender() + "", user.getStatus() + "", user.getRole() + "");
        System.out.println(i);
        MailService.getInstance().sendMailToResetPassword(user.getEmail(), newPw);
        resp.setStatus(200);
        responseModel = new ResponseModel();
        responseModel.setMessage("Vui lòng kiểm tra email để lấy lại mật khẩu");
        responseModel.setData("/login");
        responseModel.setName("success");
        out.println(new Gson().toJson(responseModel));
        out.flush();
        out.close();
        return;
    }
}
