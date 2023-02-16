package gson;
import com.google.gson.Gson;
import pojo.PlayerId;
import pojo.PlayerItem;

public class JsonMaker {
	public static String serialize(PlayerItem data) {
		Gson gson = new Gson();
		String result = gson.toJson(data);
		return result;
	}

	public static String serialize(PlayerId data) {
		Gson gson = new Gson();
		String result = gson.toJson(data);
		return result;
	}

	public static PlayerItem deserialize(String response) {
		Gson gson = new Gson();
		PlayerItem player = gson.fromJson(response, PlayerItem.class);
		return player;
	}
}
