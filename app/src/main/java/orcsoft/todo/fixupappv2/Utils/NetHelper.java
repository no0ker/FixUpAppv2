package orcsoft.todo.fixupappv2.Utils;

import android.content.Context;

import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import orcsoft.todo.fixupappv2.Entity.CloseOrderRequest;
import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.Entity.Product;
import orcsoft.todo.fixupappv2.Entity.Service;
import orcsoft.todo.fixupappv2.Entity.User;
import orcsoft.todo.fixupappv2.Exceptions.NetException;
import orcsoft.todo.fixupappv2.SharedPreferences.Prefs_;

@EBean
public class NetHelper {
    @Pref
    protected Prefs_ myPrefs;

    @RootContext
    protected Context context;

    private static Map<String, String> operationNames = new HashMap<String, String>() {{
        put("auth", "/auth");
        put("me", "/user/me");
        put("getActiveOrders", "/operation/active/");
        put("getFreeOrders", "/operation/free/");
        put("getDoneOrders", "/operation/done/");
        put("getArchiveOrders", "/operation/archive/");
        put("getServices", "/service/");
        put("getProducts", "/product/");
        put("getProductsById", "/operation/{0}/product/ ");
        put("getServicesById", "/operation/{0}/service/");
        put("setAccept", "/operation/{0}/accept/");
        put("closeOrder", "/operation/{0}/close/");
    }};

    private static OkHttpClient okHttpClient;

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    public String auth() throws IOException, NetException {
        return auth(getUserName(), getPassword());
    }

    private String auth(String email, String password) throws IOException, NetException {
        OkHttpClient okHttpClient = getOkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(getUrl() + operationNames.get("auth"))
                .post(formBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println("auth " + responseBody);

        CommonResponseMessage requestMessage = new Gson().fromJson(responseBody, CommonResponseMessage.class);
        Integer code = requestMessage.code;
        if (code != 0) {
            if (requestMessage.message != null && requestMessage.message.trim().length() > 0) {
                throw new NetException(code, requestMessage.message);
            } else {
                throw new NetException(code);
            }
        }

        String accessToken = requestMessage.data.get("access_token");
        setAccessToken(accessToken);

        return accessToken;
    }

    public User me() throws IOException, NetException {
        okHttpClient = getOkHttpClient();
        if (getAccessToken() == null) {
            auth();
        }

        Request request = new Request.Builder()
                .url(getUrl() + operationNames.get("me"))
                .addHeader("Cookie", " access_token=" + getAccessToken())
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println("me " + responseBody);

        UserResponseMessage requestMessage = new Gson().fromJson(responseBody, UserResponseMessage.class);
        Integer code = requestMessage.code;
        if (code != 0) {
            if (requestMessage.message != null && requestMessage.message.trim().length() > 0) {
                throw new NetException(code, requestMessage.message);
            } else {
                throw new NetException(code);
            }
        }

        return requestMessage.data.get("user");
    }

    private List<Order> getOrders(String operationName) throws IOException, NetException {
        okHttpClient = getOkHttpClient();
        if (getAccessToken() == null) {
            auth();
        }

        Request request = new Request.Builder()
                .url(getUrl() + operationNames.get(operationName))
                .addHeader("Cookie", " access_token=" + getAccessToken())
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(operationName + " " + responseBody);

        OrdersResponseMessage requestMessage = new Gson().fromJson(responseBody, OrdersResponseMessage.class);
        Integer code = requestMessage.code;
        if (code != 0) {
            if (requestMessage.message != null && requestMessage.message.trim().length() > 0) {
                throw new NetException(code, requestMessage.message);
            } else {
                throw new NetException(code);
            }
        }

        return requestMessage.data.get("operation_list");
    }

    public List<Order> getActiveOrders() throws IOException, NetException {
        return getOrders("getActiveOrders");
    }

    public List<Order> getFreeOrders() throws IOException, NetException {
        return getOrders("getFreeOrders");
    }

    public List<Order> getDoneOrders() throws IOException, NetException {
        return getOrders("getDoneOrders");
    }

    public List<Order> getArchiveOrders() throws IOException, NetException {
        return getOrders("getArchiveOrders");
    }

    public List<Service> getServices(Integer id) throws IOException, NetException {

        okHttpClient = getOkHttpClient();
        if (getAccessToken() == null) {
            auth();
        }
        String currentUrl;
        if (id == null) {
            currentUrl = getUrl() + operationNames.get("getServices");
        } else {
            currentUrl = getUrl() + MessageFormat.format(operationNames.get("getServicesById"), id);
        }
        Request request = new Request.Builder()
                .url(currentUrl)
                .addHeader("Cookie", " access_token=" + getAccessToken())
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println("getServices " + " " + responseBody);

        ServicesResponseMessage requestMessage = new Gson().fromJson(responseBody, ServicesResponseMessage.class);
        Integer code = requestMessage.code;
        if (code != 0) {
            if (requestMessage.message != null && requestMessage.message.trim().length() > 0) {
                throw new NetException(code, requestMessage.message);
            } else {
                throw new NetException(code);
            }
        }

        return requestMessage.data.get("service_list");
    }


    public List<Service> getServices() throws IOException, NetException {
        return getServices(null);
    }

    public List<Product> getProducts() throws IOException, NetException {
        return getProducts(null);
    }

    public List<Product> getProducts(Integer id) throws IOException, NetException {
        okHttpClient = getOkHttpClient();
        if (getAccessToken() == null) {
            auth();
        }

        String currentUrl;
        if (id == null) {
            currentUrl = getUrl() + operationNames.get("getProducts");
        } else {
            currentUrl = getUrl() + MessageFormat.format(operationNames.get("getProductsById"), id);
        }

        Request request = new Request.Builder()
                .url(currentUrl)
                .addHeader("Cookie", " access_token=" + getAccessToken())
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println("getProducts " + " " + responseBody);

        ProductsResponseMessage requestMessage = new Gson().fromJson(responseBody, ProductsResponseMessage.class);
        Integer code = requestMessage.code;
        if (code != 0) {
            if (requestMessage.message != null && requestMessage.message.trim().length() > 0) {
                throw new NetException(code, requestMessage.message);
            } else {
                throw new NetException(code);
            }
        }

        return requestMessage.data.get("product_list");
    }

    public Integer setAccept(Integer orderId, String hh_ss) throws IOException, NetException {
        okHttpClient = getOkHttpClient();
        if (getAccessToken() == null) {
            auth();
        }

        String url = MessageFormat.format(operationNames.get("setAccept"), orderId);

        RequestBody formBody = new FormBody.Builder()
                .add("time", hh_ss)
                .build();

        Request request = new Request.Builder()
                .url(getUrl() + url)
                .addHeader("Cookie", " access_token=" + getAccessToken())
                .post(formBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println("setAccept" + " " + responseBody);

        CommonResponseMessage requestMessage = new Gson().fromJson(responseBody, CommonResponseMessage.class);
        Integer code = requestMessage.code;
        if (code != 0) {
            if (requestMessage.message != null && requestMessage.message.trim().length() > 0) {
                throw new NetException(code, requestMessage.message);
            } else {
                throw new NetException(code);
            }
        }
        return requestMessage.code;
    }

    public CommonResponseMessage closeOrder(Order currentOrder, CloseOrderRequest closeOrderRequest) throws IOException, NetException {
        okHttpClient = getOkHttpClient();
        if (getAccessToken() == null) {
            auth();
        }

        String url = MessageFormat.format(operationNames.get("closeOrder"), currentOrder.getId());

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        int[] serivices = closeOrderRequest.getService_list();
        for (int i = 0; i < serivices.length; i++) {
            if(serivices[i]>0){
                formBodyBuilder.add("service_list[" + i + "]", String.valueOf(serivices[i]));
            }
        }

        int[] products = closeOrderRequest.getProduct_list();
        for (int i = 0; i < products.length; i++) {
            if(products[i]>0){
                formBodyBuilder.add("product_list[" + i + "]", String.valueOf(products[i]));
            }
        }

        RequestBody formBody = formBodyBuilder.build();

        Request request = new Request.Builder()
                .url(getUrl() + url)
                .addHeader("Cookie", " access_token=" + getAccessToken())
                .post(formBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println("closeOrder" + " " + responseBody);

        CommonResponseMessage requestMessage = new Gson().fromJson(responseBody, CommonResponseMessage.class);
        Integer code = requestMessage.code;
        if (code != 0) {
            if (requestMessage.message != null && requestMessage.message.trim().length() > 0) {
                throw new NetException(code, requestMessage.message);
            } else {
                throw new NetException(code);
            }
        }
        return requestMessage;
    }


    public class CommonResponseMessage {
        public Integer code;
        public String message;
        public Map<String, String> data;
    }

    private class UserResponseMessage {
        public Integer code;
        public String message;
        public Map<String, User> data;
    }

    private class OrdersResponseMessage {
        public Integer code;
        public String message;
        public Map<String, List<Order>> data;
    }

    private class ServicesResponseMessage {
        public Integer code;
        public String message;
        public Map<String, List<Service>> data;
    }

    private class ProductsResponseMessage {
        public Integer code;
        public String message;
        public Map<String, List<Product>> data;
    }

    private String getUserName() {
        return myPrefs.login().get();
    }

    private String getPassword() {
        return myPrefs.password().get();
    }

    private String getUrl() {
        return myPrefs.server().get();
    }

    private String getAccessToken() {
        String accessToken = myPrefs.accessToken().get().trim();
        if (StringUtils.isEmpty(accessToken)) {
            return null;
        } else {
            return accessToken;
        }
    }

    private void setAccessToken(String accessToken) {
        myPrefs.accessToken().put(accessToken);
    }
}
