package com.test.controller;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DownLoadController {
    @Autowired
    freemarker.template.Configuration configuration;
    @RequestMapping(value = "/template" ,method = RequestMethod.POST)
    public void getResults(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException, TemplateException {
        String jsonName=request.getParameter("jsname");
        String jsonGet=request.getParameter("jsonStr");
        String myjson=jsonGet.substring(1,jsonGet.length()-1);
        String jsonTemplate=myjson.replace("\\n","");
        String jsonTemplate2=jsonTemplate.replace("\\","");
        JSONObject jsonObject=JSONObject.fromObject(jsonTemplate2);
        JSONArray myjsonArray=JSONArray.fromObject(jsonObject.getJSONObject("mxGraphModel").getJSONObject("root").getJSONArray("mxCell"));

        System.out.println(myjsonArray);
        int length=myjsonArray.size();
        //System.out.println(length);
        JSONArray jsonArrayTemplate=new JSONArray();
        for(int i=2;i<length;i++){
            JSONObject job=myjsonArray.getJSONObject(i);
            jsonArrayTemplate.add(job);
        }
        System.out.println(jsonArrayTemplate);
        int lengthTemplate=jsonArrayTemplate.size();
        String results="";
        String content;
        for(int j=0;j<lengthTemplate;j++){
            JSONObject job=jsonArrayTemplate.getJSONObject(j);
            System.out.println(job);
            String flag;
            if(job.has("-flag")) {
                flag=job.getString("-flag");
                if (flag.equals("goal")) {
                    Map<String, Object> model = new HashMap<String, Object>();
                    String value = job.getString("-value");
                    if (job.has("-usecaseDiscription")) {
                        String usecaseDiscription = job.getString("-usecaseDiscription");
                        model.put("usecaseDiscription", usecaseDiscription);
                    }
                    if (!job.has("-usecaseDiscription")) {
                        model.put("usecaseDiscription", "未定义");
                    }
                    if (job.has("-participant")) {
                        String participant = job.getString("-participant");
                        model.put("participant", participant);
                    }
                    if (!job.has("-participant")) {
                        model.put("participant", "未定义");
                    }
                    if (job.has("-preCondition")) {
                        String preCondition = job.getString("-preCondition");
                        model.put("preCondition", preCondition);
                    }
                    if (!job.has("-preCondition")) {
                        model.put("preCondition", "未定义");
                    }
                    if (job.has("-aftCondition")) {
                        String aftCondition = job.getString("-aftCondition");
                        model.put("aftCondition", aftCondition);
                    }
                    if (!job.has("-aftCondition")) {
                        model.put("aftCondition", "未定义");
                    }
                    model.put("value", value);
                    Template t = configuration.getTemplate("goal.ftl","UTF-8");
                    content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                    //System.out.println(content);
                } else if (flag.equals("requirement")) {
                    Map<String, Object> model = new HashMap<String, Object>();
                    String value = job.getString("-value");
                    if (job.has("-basicEventFlow")) {
                        String basicEventFlow = job.getString("-basicEventFlow");
                        model.put("basicEventFlow", basicEventFlow);
                    }
                    if (!job.has("-basicEventFlow")) {
                        model.put("basicEventFlow", "未定义");
                    }
                    if (job.has("-addtionEventFlow")) {
                        String addtionEventFlow = job.getString("-addtionEventFlow");
                        model.put("addtionEventFlow", addtionEventFlow);
                    }
                    if (!job.has("-addtionEventFlow")) {
                        model.put("addtionEventFlow", "未定义");
                    }
                    if (job.has("-businessRule")) {
                        String businessRule = job.getString("-businessRule");
                        model.put("businessRule", businessRule);
                    }
                    if (!job.has("-businessRule")) {
                        model.put("businessRule", "未定义");
                    }
                    if (job.has("-nonFunctionalRule")) {
                        String nonFunctionalRule = job.getString("-nonFunctionalRule");
                        model.put("nonFunctionalRule", nonFunctionalRule);
                    }
                    if (!job.has("-nonFunctionalRule")) {
                        model.put("nonFunctionalRule", "未定义");
                    }
                    model.put("value", value);
                    Template t = configuration.getTemplate("requirement.ftl","UTF-8");
                    content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                    //System.out.println(content);
                } else {
                    String value = job.getString("-value");
                    Map<String, Object> model = new HashMap<String, Object>();
                    model.put("value", value);
                    model.put("flag", flag);
                    Template t = configuration.getTemplate("others.ftl","UTF-8");
                    content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                    //System.out.println(content);
                }
                results += content;
            }
        }
        //System.out.println(results);
        WriteStringToFile(jsonName,results);
        //String filename=jsonName+".md";
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response. setCharacterEncoding("UTF-8");
        //response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.replaceAll(" ", "").getBytes("utf-8"),"iso8859-1"));
        System.out.println(results);

        //String resp="{\"name\":\"success\",\"content\":\""+t+"\"}";
        //System.out.println(resp);
        //JSONObject jsonObject1=JSONObject.fromObject(resp);
        //System.out.println(jsonObject1.getString("content"));
        response.getWriter().print(results);
        response.getWriter().flush();
        response.getWriter().close();
    }
    public void WriteStringToFile(String name,String result) {
        PrintStream ps=null;
        try {
            File file = new File("F:\\新建文件夹\\demo\\src\\main\\resources\\templates\\"+name+".md");
            ps = new PrintStream(new FileOutputStream(file));
            ps.println(result);// 往文件里写入字符串

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            ps.close();
        }
    }
}
