package esd.scos.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oracle.tools.packager.Log;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Creator: hfang
 * Date: 2018/11/07 13:13
 * Description:
 **/

@WebServlet("/FoodUpdateService")
public class FoodUpdateService extends HttpServlet {
    private static final int NEED_UPDATE = 1;
    private static final int TEST_JSON = 2;
    private static final int TEST_XML = 3;
    private static final int FOOD_TYPE_NUM = 4;
    private static final int MAX_STOCK_NUM = 5;
    private String[][] newFoodNames = {{"蓝莓山药", "酱黄瓜"}, {"水煮鱼", "干锅花菜"}, {"龙虾", "鲍鱼"}, {"可乐", "雪碧"}};

    private String TAG = "esd.scos.servlet/FoodUpdateService:";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int requireUpdate = 0;
        request.setCharacterEncoding("utf-8");
        requireUpdate = Integer.parseInt(request.getParameter("needUpdate"));

        switch (requireUpdate) {
            case NEED_UPDATE: {
                JSONObject foodUpdateInfo = randomGenerateNewFoodJSON(NEED_UPDATE);
//             response.setCharacterEncoding("utf-16");
                response.setContentType("application/json;charset=utf-8");
                response.setCharacterEncoding("utf-8");
                response.getWriter().print(foodUpdateInfo);
                break;
            }
            case TEST_JSON: {
                JSONArray foodInfoArray = new JSONArray();
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < 10000; i++) {
                    foodInfoArray.add(randomGenerateNewFoodJSON(TEST_JSON));
                }
                long finishTime = System.currentTimeMillis();
                System.out.println(TAG+"generate JSONArray cost "+(finishTime-startTime)+"ms"+",size = "+foodInfoArray.size());
                System.out.println(TAG+"generate JSONArray size =  "+foodInfoArray.toString().length());
                response.setContentType("application/json;charset=utf-8");
                response.setCharacterEncoding("utf-8");
                response.getWriter().print(foodInfoArray);
                break;
            }
            case TEST_XML: {
                long startTime = System.currentTimeMillis();
                Document document = DocumentHelper.createDocument();
                Element root = document.addElement("root");
                for(int i=0; i<10000; i++) {
                    FoodInfo foodInfo = randomGenerateFoodInfo();
                    Element food = root.addElement("food");
                    food.addElement("NAME").addText(foodInfo.name);
                    food.addElement("PRICE").addText(String.valueOf(foodInfo.price));
                    food.addElement("TYPE").addText(String.valueOf(foodInfo.type));
                    food.addElement("STOCK").addText(String.valueOf(foodInfo.stock));
                }

                long finishTime = System.currentTimeMillis();
                System.out.println(TAG+"generate XML cost "+(finishTime-startTime)+"ms"+",size = "+100);
                System.out.println(TAG+"generate XML size = "+ document.asXML().length());
                response.setContentType("application/xml;charset=utf-8");
                response.setCharacterEncoding("utf-8");
                response.getWriter().print(document.asXML());
            }
        }
    }

    // mode is used to control the mode of the data generate
    private JSONObject randomGenerateNewFoodJSON(int mode) {
        double hasUpdate = Math.random();
        JSONObject foodUpdateInfo = new JSONObject();

        FoodInfo foodInfo = randomGenerateFoodInfo();

        foodUpdateInfo.put("NAME", foodInfo.name);
        foodUpdateInfo.put("TYPE", foodInfo.type);
        foodUpdateInfo.put("PRICE", Math.round(foodInfo.price));
        foodUpdateInfo.put("STOCK", foodInfo.stock);

        switch (mode) {
            case NEED_UPDATE: {
                if (hasUpdate < 0.6 && hasUpdate > 0.4) {
                    return foodUpdateInfo;
                } else {
                    return null;
                }
            }
            case TEST_JSON: {
                return foodUpdateInfo;
            }
        }

        return foodUpdateInfo;
    }

    private FoodInfo randomGenerateFoodInfo() {
        int randomType = (int) (Math.random() * 100) % FOOD_TYPE_NUM;
        int randomStock = (int) (Math.random() * 100) % MAX_STOCK_NUM;
        int randomPosition = (int) (Math.random() * 100) % newFoodNames[randomType].length;
        double randomPrice = Math.round(Math.random() * 200);
        String randomName = newFoodNames[randomType][randomPosition];
        System.out.println(randomName);

        FoodInfo foodInfo = new FoodInfo(randomName, randomPrice, randomType, randomStock);

        return foodInfo;
    }

    private class FoodInfo {
         double price;
         String name;
         int type;
         int stock;

         public FoodInfo(String name, double price, int type, int stock) {
             this.name = name;
             this.price = price;
             this.type = type;
             this.stock = stock;
         }
    }
}
