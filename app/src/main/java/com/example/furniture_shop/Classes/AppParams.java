package com.example.furniture_shop.Classes;

public class AppParams {
    public static final String BASE_URL = "http://whiteavangard.hldns.ru";
    public static final String API_SIGNIN = BASE_URL + "/api/signin";
    public static final String API_SIGNUP = BASE_URL + "/api/signup";
    public static final String API_USER = BASE_URL + "/api/user";
    public static final String API_CATEGORIES = BASE_URL + "/api/getcategories";
    public static final String API_ADD_CATEGORY = BASE_URL + "/api/addcategory";
    public static final String API_PRODUCTS = BASE_URL + "/api/getproducts";
    public static final String API_PRODUCT = BASE_URL + "/api/getproduct";
    public static final String API_ADD_PRODUCT = BASE_URL + "/api/addproduct";
    public static final String API_GET_CART_PRODUCTS = BASE_URL + "/api/getcartproducts";
    public static final String API_ORDER = BASE_URL + "/api/addorder";
    public static final String API_ADD_CART_PRODUCT = BASE_URL + "/api/addcartproduct";
    public static final String API_REMOVE_CART_PRODUCT = BASE_URL + "/api/removecartproducts";
    public static final String API_REMOVE_PRODUCT = BASE_URL + "/api/deleteproduct";

    // SHARED PREFERENSES
    public static String SHARED_PREFS = "FurnitureShop";
    public static String SP_USER_IS_LOGINED = "UserIsLogined";
    public static String SP_BEARER_TOKEN = "BearerToken";

    private static String user_token;
    private static Boolean user_is_logined = false;

    public static Boolean getUserIsLogined() {
        return user_is_logined;
    }

    public static void setUserIsLogined(Boolean user_is_logined) {
        AppParams.user_is_logined = user_is_logined;
    }

    public static String getUserToken() {
        return user_token;
    }

    public static void setUserToken(String user_token) {
        AppParams.user_token = user_token;
    }


}
