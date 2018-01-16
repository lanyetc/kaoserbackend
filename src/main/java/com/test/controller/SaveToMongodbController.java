package com.test.controller;

import com.test.User.KaoserFile;
import com.test.Dao.KaoserFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class SaveToMongodbController {
    @Autowired
    private KaoserFileRepository kaoserFileRepository;
    @Autowired
    freemarker.template.Configuration configuration;
    @Autowired
    MongoTemplate mongoTemplate;
    @RequestMapping(value = "/save" ,method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request,
                                     HttpServletResponse response , Model model)
            throws ServletException, IOException{
        String jsonName=request.getParameter("jsname");
        String jsonGet=request.getParameter("jsonStr");
        System.out.println(jsonName);
        System.out.println(jsonGet);
        KaoserFile jsoninfo=new KaoserFile();
        jsoninfo.setName(jsonName);
        jsoninfo.setJsonStr(jsonGet);
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response. setCharacterEncoding("UTF-8");
        String resp="";
        if(kaoserFileRepository.findByName(jsonName)!=null){
            resp="{\"name\":\"fail\"}";
        }
        else{
            kaoserFileRepository.save(jsoninfo);
            resp="{\"name\":\"success\"}";
        }
        try {
                response.getWriter().print(resp);
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        System.out.println(resp);
    }

    public KaoserFile findByName(String name){
        return kaoserFileRepository.findByName(name);
    }
}
