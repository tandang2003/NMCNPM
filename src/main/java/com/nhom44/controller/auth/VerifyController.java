package com.nhom44.controller.auth;

import com.nhom44.services.CartService;
import com.nhom44.services.UserService;
import com.nhom44.services.VerifyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/verify", "/verify/cart"})
public class VerifyController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get token from url
        String token = req.getParameter("code");
        String url = req.getRequestURI();
//        if (token != null) {
        //valid data
        if (url.equals("/verify/cart")) {
            int cartId = VerifyService.getInstance().getCartsIdByCode(token);
//                System.out.println(cartId);
            if (CartService.getInstance().updateSuccessVerifyCart(cartId)) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }
        } else if (url.equals("/verify")) {
            int userId = VerifyService.getInstance().getUserIdByCode(token);
            UserService.getInstance().updateSuccessVerify(userId);
            resp.sendRedirect(req.getContextPath() + "/login");
        }
//            }
        resp.sendRedirect(req.getContextPath() + "404");
    }
}
