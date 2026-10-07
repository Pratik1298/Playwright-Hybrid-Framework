
package com.pratik.utils;

import com.google.gson.*;

public class JsonUtil {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    public static JsonObject toJsonObject(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }
}