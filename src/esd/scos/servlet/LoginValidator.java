package esd.scos.servlet;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Creator: hfang
 * Date: 2018/11/04 21:41
 * Description: This servlet is used to verify the username and password
 *              from the Android APP.
 **/

@WebServlet("/LoginValidator")
public class LoginValidator extends HttpServlet {
    private String[] nameList = {"zhangsan","wangwu","lisi"};
    private String[] passwordList = {"zhangsan","wangwu","lisi"};
    HashMap<String,String> userInfo = new HashMap<>();

    public void init() {
        for(int i=0; i<nameList.length; i++) {
            userInfo.put(nameList[i],passwordList[i]);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name;
        String password;
        int result = 0;

        request.setCharacterEncoding("utf-8");
        name = request.getParameter("name");
        password = request.getParameter("password");

        for(int i=0; i<userInfo.size(); i++) {
            if (userInfo.get(name).equals(password)) {
                result = 1;
                break;
            }
        }

        JSONObject returnResult = new JSONObject();
        returnResult.put("RESULTCODE",result);
        response.getWriter().print(returnResult);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

}
