package com.nhom44.api.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nhom44.bean.*;
import com.nhom44.services.*;
import com.nhom44.util.PriceObjectHelper;
import com.nhom44.util.SearcherProjectUtil;
import com.nhom44.validator.NumberVallidator;
import com.nhom44.validator.SingleValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/api/project", "/api/project/search", "/api/project/search/length", "/api/post/project/*"})
public class ProjectController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        ResponseModel responseModel = new ResponseModel();
        if (url.equals("/api/project")) {
            List<Service> services = ServicesService.getInstance().getAllActive();
            List<Category> categories = CategoryService.getInstance().getAllActiveOrderByNumOfViews();
            List<Province> provinces = ProvinceService.getInstance().getAll();
            List<PriceObjectHelper> prices = SearcherProjectUtil.PRICE_SEARCHING;
            List<Integer> acreages = SearcherProjectUtil.ACREAGE;
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("services", new Gson().toJsonTree(services));
            jsonObject.add("categories", new Gson().toJsonTree(categories));
            jsonObject.add("provinces", new Gson().toJsonTree(provinces));
            jsonObject.add("prices", new Gson().toJsonTree(prices));
            jsonObject.add("acreages", new Gson().toJsonTree(acreages));
            responseModel.setStatus("200");
            responseModel.setMessage("get project search data");
            responseModel.setData(jsonObject.toString());
            resp.setStatus(200);
            resp.getWriter().println(new Gson().toJson(responseModel));
            resp.getWriter().flush();
            resp.getWriter().close();
            return;
        } else if (url.equals("/api/post/project")) {

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        System.out.println(url);
        System.out.println(req.getParameterMap().keySet().toString());
        List<PriceObjectHelper> prices = SearcherProjectUtil.PRICE_SEARCHING;
        req.setAttribute("prices", prices);
        if (url.equals("/api/project")) {
//            List<Project> projects = ProjectService.getInstance().getProjetAllActive();
        } else if (url.equals("/api/project/search") || url.equals("/api/project/search/length")) {
            User user = (User) req.getSession().getAttribute("auth")==null?new User():(User) req.getSession().getAttribute("auth");
            if (url.equals("/api/project/search")) {

                int userid = user != null ? user.getId() : 0;
                List<Project> projects = ProjectService.getInstance().getAllProjectActive(user.getId());
                System.out.println(new Gson().toJson(projects));
                resp.setStatus(200);
                resp.getWriter().print(new Gson().toJson(projects));
            } else if (url.equals("/api/project/search/length")) {
                int num= ProjectService.getInstance().getAllProjectActive(user.getId()).size();
                int size =num % 16 == 0 ? num / 16 : num / 16 + 1;
                resp.setStatus(200);
                resp.getWriter().print(new Gson().toJson(size));
            }
            resp.getWriter().flush();
            resp.getWriter().close();
            return;
        }
    }
}