package com.nhom44.api.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nhom44.bean.*;
import com.nhom44.services.CategoryService;
import com.nhom44.services.ProjectService;
import com.nhom44.services.SliderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = {"/api/home", "/api/home/projects/*", "/api/home/slides", "/api/home/categories", "/api/home/contact"})
public class HomeController extends HttpServlet {
    private ResponseModel responseModel;
    private HttpServletRequest req;
    private HttpServletResponse resp;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.req = req;
        this.resp = resp;
//      2.1.1 lấy ra đường dẫn gốc đã gửi yêu cầu
        String url = req.getServletPath();
//      2.1.2 tạo ra đối tượng phản hồi
        responseModel = new ResponseModel();
//      3.1 selectSuitableHandleForThePath
        selectSuitableHandleForThePath(url, req, resp, responseModel);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", responseModel.getName());
        jsonObject.add("data", new Gson().toJsonTree(responseModel.getData()));
        PrintWriter printWriter = resp.getWriter();
        printWriter.print(jsonObject.toString());
        printWriter.flush();
        printWriter.close();
        return;
    }


    //phương thức đùng để kiểm tra đường dẫn gốc và thực hiện hành vi xử lý tương ứng
    private void selectSuitableHandleForThePath(String path, HttpServletRequest req, HttpServletResponse resp, ResponseModel responseModel) throws
            IOException {
//       tìm quy trình sử lý phù hợp với đường dẫn gốc
        switch (path) {
            //tồn tài đường dấn gốc tương ứng
            case "/api/home/projects":
                int categoryId;
//                3.1.1 lấy ra mã loại dự án từ đường dẫn yêu cầu
                String data = req.getPathInfo().substring(1);
//               4 kiểm tra mã có tồn tại va hợp lệ không
                if (data != null && !data.equals("undefined")) {
//                   4.1.1 thực hiện lưu mã dự án vào session
                    req.getSession().setAttribute("HomeFindingcategoryId", data);
                } else {
//                   4.2.1 thực hiện lấy ra mã của loại dự án trước đó được lưu trên session
                    data = (String) req.getSession().getAttribute("HomeFindingcategoryId");
                }
//              5 getUserId
                int userId = getUserId(req);
//                6 get8ActiveProjectHighestView
                List<Project> projects = ProjectService.getInstance().get8ActiveProjectHighestView(Integer.parseInt(data), userId);
//                8 updateResponseModel
                updateResponseModel(200, "success", projects);
//                responseModel.setName("success");
//                responseModel.setData(projects);
                break;
            case "/api/home/slides":
                System.out.println("get slides");
                List<Slider> sliders = SliderService.getInstance().getAllActive();
                updateResponseModel(200, "success", sliders);
//                responseModel.setName("success");
//                responseModel.setData(sliders);
                break;
            case "/api/home/categories":
                List<Category> categories = CategoryService.getInstance().getAllActiveOrderByNumOfViews();
                updateResponseModel(200, "success", categories);
//                responseModel.setName("success");
//                responseModel.setData(categories);
                break;
            default:
//                3.2 updateResponseModel
                updateResponseModel(400, "error", "url not found");
                break;
        }
    }

    private void updateResponseModel(int status, String name, Object data) {
        resp.setStatus(status);
        responseModel.setName(name);
        responseModel.setData(data);
    }

    private int getUserId(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("auth");
//        5.1.1 người dùng đã đăng nhập nên lấy ra id người dùng
//        5.2.1 người dùng chưa đăng nhập nên trả về id bằng 0
        return user == null ? 0 : user.getId();
    }
}
