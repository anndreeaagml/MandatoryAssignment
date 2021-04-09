package com.example.mandatoryassignment;

public class ApiUtils {
    private ApiUtils() {
    }

    private static final String BASE_URL = "https://anbo-restmessages.azurewebsites.net/api";

    public static MessageService getBookStoreService() {
        return RetrofitClient.getClient(BASE_URL).create(MessageService.class);
    }
}
