package com.nhom44.api.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nhom44.bean.*;
import com.nhom44.services.*;
import com.nhom44.util.StringUtil;
import com.nhom44.util.Upload;
import com.nhom44.validator.EmailSingleValidator;
import com.nhom44.validator.NumberVallidator;
import com.nhom44.validator.SingleValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/api/advise")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 10 * 5,
        fileSizeThreshold = 1024 * 1024 * 10)
public class AdviseController extends HttpServlet {
    private ResponseModel responseModel;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private Advise advise;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Province> provinces = ProvinceService.getInstance().getAll();
        List<Category> categories = CategoryService.getInstance().getAllActive();
        List<Service> services = ServicesService.getInstance().getAllActive();
        List<Integer> projects = ProjectService.getInstance().getAllProjectActiveId();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("provinces", new com.google.gson.Gson().toJsonTree(provinces));
        jsonObject.add("categories", new com.google.gson.Gson().toJsonTree(categories));
        jsonObject.add("services", new com.google.gson.Gson().toJsonTree(services));
        jsonObject.add("projects", new com.google.gson.Gson().toJsonTree(projects));
        resp.setStatus(200);
        resp.getWriter().println(jsonObject.toString());
        resp.getWriter().flush();
        resp.getWriter().close();
    }
//1.gửi thông tin biểu mẩu về server(doPost(req, resp))
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        this.req = req;
        this.resp = resp;
        advise = new Advise();
        responseModel = new ResponseModel();
//        2.kiểm tra dữ liệu trông biểu mẫu người dùng có hợp lệ không(checkValidation())
        if (!checkValidation()) {
//            2.2.1 gửi thông báo cho người dùng biết về trường thông tin không hợp lệ
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("name", new Gson().toJsonTree(responseModel.getName()));
            jsonObject.add("message", new Gson().toJsonTree(responseModel.getMessage()));
            resp.setStatus(400);
            resp.getWriter().println(jsonObject.toString());
            resp.getWriter().flush();
            resp.getWriter().close();
            return;
        }
//       2.1.1 lưu yêu cầu của người dùng vào CSDL(add(advise))
//      trả về trạng thái thành công hay thất bại của việc lưu yêu cầu
        boolean status = AdviseService.getInstance().add(advise);
        JsonObject jsonObject = new JsonObject();
//        3. thực hiện kiểm tra trạng thành công hay thất bại của việc lưu yêu cầu tư vấn vào cơ sở dữ liệu
        if (status) {
//            3.1.1 cập nhập thông tin phải hồi luu yêu cầu thành công
            jsonObject.add("message", new Gson().toJsonTree("Yêu cầu của bạn đã gửi thành công. chúng tôi sẽ cố gắn liên hệ với bạn sớm nhất có thể"));
            jsonObject.add("name", new Gson().toJsonTree("success"));
            resp.setStatus(200);
        }else{
//            3.1.2 cập nhập thông tin phải hồi luu yêu cầu thất bại
            jsonObject.add("name", new Gson().toJsonTree("Lỗi hệ thống"));
            jsonObject.add("message", new Gson().toJsonTree("Hệ thống đang gặp trục trặc vui lòng thực hiện lại sau ít phút"));
            resp.setStatus(400);
        }
//        4.1.1 gửi phản hồi thông báo cho người dùng
        resp.getWriter().println(jsonObject.toString());
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    private void saveImage(Advise advise) throws ServletException, IOException {

        List<String> image = Upload.uploadFile(Upload.UPLOAD_CART + "\\" + advise.getId() + "_" + advise.getEmail(), "image", req);
        advise.setImages(image);

        for (String s : image
        ) {
            Image img = StringUtil.getImage(s);
            int imageId = ImageService.getInstance().add(img);
            AdviseService.getInstance().addImage(advise.getId(), imageId);
        }
    }

    private boolean checkValidation() {
        SingleValidator singleValidator = new EmailSingleValidator();
        String email = req.getParameter("email");
        if (!singleValidator.validator(email)) {
            // create notify
            updateResponseModel("email", "Vui lòng nhập email.", null);
            return false;
        } else {
            advise.setEmail(email);
        }
        singleValidator = new NumberVallidator();
        String categoryId = req.getParameter("category");
        if (!singleValidator.validator(categoryId)) {
            updateResponseModel("category", "Vui lòng chọn loại dự án.", null);
            return false;
        } else {
            advise.setCategoryId(Integer.parseInt(categoryId));
        }
        String provinceId = req.getParameter("address");
        if (!singleValidator.validator(provinceId)) {
            updateResponseModel("address", "vui lòng chọn tỉnh thành", null);
            return false;
        } else {
            advise.setProvinceId(Integer.parseInt(provinceId));
        }
        String representProjectId = req.getParameter("representProjectId");
        if (!singleValidator.validator(representProjectId)) {
            updateResponseModel("representProjectId", "vui lòng chọn dự án mẫu", null);
            return false;
        } else {
            advise.setRepresentProjectId(Integer.parseInt(representProjectId));
        }
        String width = req.getParameter("width");
        if (!singleValidator.validator(width)) {
            updateResponseModel("width", "vui lòng nhập chiều rộng", null);
            return false;
        } else {
            advise.setWidth(Double.parseDouble(width));
        }
        String height = req.getParameter("height");
        if (!singleValidator.validator(height)) {
            updateResponseModel("height", "vui lòng nhập chiều dài", null);
            return false;
        } else {
            advise.setHeight(Double.parseDouble(height));
        }
        String services = req.getParameter("services");
        if (services == null || services.isEmpty()) {
            updateResponseModel("services", "vui lòng chọn dịch vụ", null);
            return false;
        }
        String[] arrservices = services.split(",");
        List<Integer> serviceIds = new ArrayList<>();
        singleValidator = new NumberVallidator();
        for (String serviceId : arrservices) {
            if (singleValidator.validator(serviceId))
                serviceIds.add(Integer.parseInt(serviceId));
        }
        advise.setServices(serviceIds);
        return true;
    }

    private void updateResponseModel(String name, String message, Object data) {
        responseModel = new ResponseModel();
        responseModel.setName(name);
        responseModel.setMessage(message);
        responseModel.setData(data);
    }

}
