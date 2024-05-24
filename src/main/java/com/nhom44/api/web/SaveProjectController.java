package com.nhom44.api.web;

import com.google.gson.Gson;
import com.nhom44.bean.Project;
import com.nhom44.bean.ResponseModel;
import com.nhom44.bean.User;
import com.nhom44.services.ProjectService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/save_project/delete", "/api/save_project"})
public class SaveProjectController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
