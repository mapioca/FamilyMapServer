package json;

import com.google.gson.Gson;

public class JSonSerializer {

    public static <T> T deserialize(String value, Class<T> returnType){
        return (new Gson()).fromJson(value, returnType);
    }

    public static <T> String serialize(T returnType){
        return (new Gson()).toJson(returnType);
    }

}
