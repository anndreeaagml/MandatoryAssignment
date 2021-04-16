package com.example.mandatoryassignment;

public class ApiUtils {
    private ApiUtils() {
    }

    private static final String BASE_URL = "https://anbo-restmessages.azurewebsites.net/api/";


    public static MessageService getMessageService() {
        return RetrofitClient.getClient(BASE_URL).create(MessageService.class);
    }
    public static CommentsService getCommentsService( int idOfTheMessage) {
        return RetrofitClient.getClient((String)(BASE_URL+"Messages/" +idOfTheMessage+"/")).create(CommentsService.class);
    }
}
