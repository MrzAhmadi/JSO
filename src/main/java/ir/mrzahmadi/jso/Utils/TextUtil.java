package ir.mrzahmadi.jso.Utils;

public class TextUtil {

    public static boolean isEmpty(String text){
        return text==null || text.equals("");
    }

    public static boolean isNotEmpty(String text){
        return text!=null && !text.equals("");
    }
}
