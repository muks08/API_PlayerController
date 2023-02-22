package resourses;
import com.google.gson.Gson;

public class JsonMaker {
	public static String serialize(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	public static <T> T deserialize(String json, Class<T> classOfT) {
		Gson gson = new Gson();
		return gson.fromJson(json, classOfT);
	}
}
