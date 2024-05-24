package com.nhom44.api.web;

import com.google.gson.Gson;
import com.nhom44.bean.*;
import com.nhom44.services.CategoryService;
import com.nhom44.services.ProjectService;
import com.nhom44.services.SliderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = {"/api/home", "/api/home/projects/*", "/api/home/slides", "/api/home/categories", "/api/home/contact"})
public class HomeController extends HttpServlet {
    private ResponseModel responseModel;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        //create ResponseModel
        responseModel = new ResponseModel();

        selectSuitableHandleForThePath(url, req, resp, responseModel);
        PrintWriter printWriter = resp.getWriter();
        printWriter.print(new Gson().toJson(responseModel));
        printWriter.flush();
        printWriter.close();
        return;
    }

    private boolean dataExist(String data) {
        return data != null && !data.equals("undefined");
    }

    private void selectSuitableHandleForThePath(String path, HttpServletRequest req, HttpServletResponse resp, ResponseModel responseModel) throws
            IOException {
        switch (path) {
            case "/api/home/projects":
                int categoryId;
                //get data from url
                String data = getData(req);
                //validate data
                if (dataExist(data)) {
                    //data is exist
                    categoryId = Integer.parseInt(data);
                    //set data to session
                    req.getSession().setAttribute("HomeFindingcategoryId", categoryId);
                } else {
                    //data is unexist
                    categoryId = (int) req.getSession().getAttribute("HomeFindingcategoryId");
                }
                //get 8 project highest view
                User user = (User) req.getSession().getAttribute("auth");
                List<Project> projects = ProjectService.getInstance().get8ActiveProjectHighestView(categoryId, user == null ? 0 : user.getId());
                //updateResponseModel
                updateResponseModel("success", projects);
//                responseModel.setName("success");
//                responseModel.setData(projects);
                resp.setStatus(200);
                break;
            case "/api/home/slides":
                System.out.println("get slides");
                List<Slider> sliders = SliderService.getInstance().getAllActive();
                updateResponseModel("success", sliders);
//                responseModel.setName("success");
//                responseModel.setData(sliders);
                resp.setStatus(200);
                break;
            case "/api/home/categories":
                List<Category> categories = CategoryService.getInstance().getAllActiveOrderByNumOfViews();
                updateResponseModel("success", categories);
//                responseModel.setName("success");
//                responseModel.setData(categories);
                resp.setStatus(200);
                break;
            default:
                resp.setStatus(400);
                updateResponseModel("error", "url not found");
                break;
        }
    }

    private void updateResponseModel(String name, Object data) {
        responseModel.setName(name);
        responseModel.setData(data);
    }
    private String getData(HttpServletRequest req) {
        return req.getPathInfo().substring(1);
    }
}
