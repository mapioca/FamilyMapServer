package json;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class JSonConverter {

    public static <T> T JSConvert(String pathName, Class<T> returnType) throws FileNotFoundException {
        File file = new File(pathName);
        Reader reader = new FileReader(file);
        return (new Gson().fromJson(reader, returnType));
    }




}
