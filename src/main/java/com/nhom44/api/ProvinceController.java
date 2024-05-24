package com.nhom44.api;

import com.google.gson.Gson;
import com.nhom44.bean.Province;
import com.nhom44.services.ProvinceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/api/province/*"})
public class ProvinceController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Province> getAll = ProvinceService.getInstance().getAll();
        resp.getWriter().println(new Gson().toJson(getAll));
        resp.getWriter().flush();
    }
}
