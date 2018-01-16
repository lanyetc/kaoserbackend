package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
@Controller
public class ServletDownload extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ServletDownload(){
        super();
    }

    @RequestMapping(value = "/ServletDownload",method = RequestMethod.POST)
    public void doDownload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ajax download file");
        String fileName = request.getParameter("fileName");
        File file = new File("F:\\新建文件夹\\demo\\src\\main\\resources\\templates\\"+fileName);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment;filename=" + fileName);
        response.setContentLength((int) file.length());

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[128];
            int count = 0;
            while ((count = fis.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.getOutputStream().flush();
            response.getOutputStream().close();
            if(fis!=null){
                fis.close();
            }
        }

        file.delete();
    }
}
