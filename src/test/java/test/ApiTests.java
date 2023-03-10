package test;

import resourses.*;
import resourses.pojo.*;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import static org.testng.Assert.*;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class ApiTests {
	Logger logger = Logger.getLogger(ApiTests.class.getName());
	private final static String URL = "http://3.68.165.45/";
	private final static String getPlayer = "player/get";
	private final static String getAllPlayers = "player/get/all";
	private final static String createNewPlayer = "player/create/supervisor";
	private final static String deletePlayer = "player/delete/supervisor";
	private final static String updatePlayer = "player/update/supervisor/";

	public Integer preConditionCreatePlayer() {
		Integer age = 30;
		String gender = "female";
		String login = "testLogin1";
		String password = "pass12345";
		String role = "user";
		String screenName = "testScreenName0";
		Specification.installSpec(Specification.requestSpec(URL), Specification.responseSpec200());
		String response = given()
				.when()
				.get(createNewPlayer+"?age="+age+"&gender="+gender+"&login="+login+"&password="+password+
						"&role="+role+"&screenName="+screenName)
				.then().extract().response().asString();
		PlayerItem player = JsonMaker.deserialize(response, PlayerItem.class);
		logger.info("\n"+"Created a new player by preCondition method with: player ID: " + player.getId() +
				 " and login: " + player.getLogin()+"\n");
		return player.getId();
	}

	public void postConditionDeletePlayer(Integer id) {
		PlayerId playerId = new PlayerId(id);
		String jsonRequest = JsonMaker.serialize(playerId);

		Specification.installSpec(Specification.requestSpec(URL), Specification.responseSpec204());
		String response = given()
				.body(jsonRequest)
				.when()
				.delete(deletePlayer)
				.then().extract().response().asString();
		logger.info("\n"+"Delete player by postCondition method with player ID " + id +"\n");
	}

	@Test(description = "Get player with valid ID using POST method")
	public void getPlayerItemByValidId() {
		Integer testPlayerId = preConditionCreatePlayer();

		PlayerId playerId = new PlayerId(testPlayerId);
		String jsonRequest = JsonMaker.serialize(playerId);

		logger.info("\n"+"Check expected status code - 200" +"\n");
		Specification.installSpec(Specification.requestSpec(URL), Specification.responseSpec200());
		String response = given()
				.body(jsonRequest)
				.when()
				.post(getPlayer)
				.then().extract().response().asString();
		PlayerItem player = JsonMaker.deserialize(response, PlayerItem.class);

		logger.info("\n"+"Check created player with ID " + testPlayerId + " with actual player ID " + player.getId()+"\n");
		assertEquals(testPlayerId, player.getId());

		postConditionDeletePlayer(player.getId());
	}

	@Test(description = "Get player with invalid ID using POST method")
	public void getPlayerItemByInvalidId() {
		Integer testPlayerId = 777777777;
		PlayerId playerId = new PlayerId(testPlayerId);
		String jsonRequest = JsonMaker.serialize(playerId);
		logger.info("\n"+"Check expected status code - 200"+"\n");
		Specification.installSpec(Specification.requestSpec(URL), Specification.responseSpec200());
		String response = given()
				.body(jsonRequest)
				.when()
				.post(getPlayer)
				.then().extract().response().asString();
		logger.info("\n"+"Check that response is empty: " + response +"\n");
		assertTrue(response.isEmpty());
	}

	@Test(description = "Check all players item for basic requirements using GET method")
	public void checkAllPlayersItem() {
		Specification.installSpec(Specification.requestSpec(URL), Specification.responseSpec200());
		logger.info("\n"+"Check expected status code - 200"+"\n");

		List<PlayerItem> players = get(getAllPlayers)
				.jsonPath().getList("players", PlayerItem.class);

		List<String> screenNames = new ArrayList<>();
		for (PlayerItem player : players) {
			screenNames.add(player.getScreenName());
			logger.info("\n"+"Check that player ID is not null. Actual ID: " + player.getId()+"\n");
			assertNotNull(player.getId());
			logger.info("\n"+"Check that player should be older than 16 and younger than 60 years old. Actual age: "
					+ player.getAge()+"\n");
			assertTrue(player.getAge() > 16 && player.getAge() < 60);
			logger.info("\n"+"Check that player`s gender can only be male or female. Actual gender: " + player.getGender()+"\n");
			assertTrue(player.getGender().contains("female") || player.getGender().contains("male"));
		}
		logger.info("\n"+"Check that screenName field is unique for each player"+"\n"+"Total unique screenNames: "
				+ screenNames.stream().distinct().count()+"\n");
		assertEquals(players.size(), screenNames.stream().distinct().count());
	}

	@Test(description = "Create player with basic requirements using GET method with parameters")
	public void createNewPlayerWithValidData() {
		Integer age = 31;
		String gender = "female";
		String login = "login666";
		String role = "user";
		String screenName = "TestLogin666";

		logger.info("\n"+"Check expected status code - 200"+"\n");
		Specification.installSpec(Specification.requestSpec(URL), Specification.responseSpec200());
		String response = given()
				.when()
				.get(createNewPlayer+"?age="+age+"&gender="+gender+"&login="+login+
						"&role="+role+"&screenName="+screenName)
				.then().extract().response().asString();
		PlayerItem player = JsonMaker.deserialize(response, PlayerItem.class);

		logger.info("\n"+"Check that new player login " + login + " the same as expected " + player.getLogin()+"\n");
		assertEquals(player.getLogin(), login);
		logger.info("\n"+"Check that player ID not null. Actual ID " + player.getId()+"\n");
		assertNotNull(player.getId());

		postConditionDeletePlayer(player.getId());
	}

	@Test(description = "Create a player without compliance of requirements using GET method with parameters")
	public void createNewPlayerWithInvalidData() {
		Integer age = 61;
		String gender = "male";
		String login = "testLogin5";
		String password = "pass12345";
		String role = "user";
		String screenName = "TestPlayer777";

		logger.info("\n"+"Check expected status code - 400"+"\n");
		Specification.installSpec(Specification.requestSpec(URL), Specification.responseSpec400());
		Response response = get(createNewPlayer+"?age="+age+"&gender="+gender+"&login="+login+
				"&password="+password+"&role="+role+"&screenName="+screenName);
	}

	@Test(description = "Delete player with valid ID using DELETE method")
	public void deletePlayerByValidId() {
		Integer testPlayerId = preConditionCreatePlayer();
		PlayerId playerId = new PlayerId(testPlayerId);
		String jsonRequest = JsonMaker.serialize(playerId);

		logger.info("\n"+"Delete player with ID " + testPlayerId+"\n");
		logger.info("\n"+"Check expected status code - 204"+"\n");
		Specification.installSpec(Specification.requestSpec(URL), Specification.responseSpec204());
		String response = given()
				.body(jsonRequest)
				.when()
				.delete(deletePlayer)
				.then().extract().response().asString();
	}

	@Test(description = "Delete player with invalid ID using DELETE method")
	public void deletePlayerByInvalidId() {
		Integer testPlayerId = 777;
		PlayerId playerId = new PlayerId(testPlayerId);
		logger.info("\n"+"Delete player with invalid ID " + testPlayerId+"\n");
		String jsonRequest = JsonMaker.serialize(playerId);

		logger.info("\n"+"Check expected status code - 403"+"\n");
		Specification.installSpec(Specification.requestSpec(URL), Specification.responseSpec403());
		String response = given()
				.body(jsonRequest)
				.when()
				.delete(deletePlayer)
				.then().extract().response().asString();
	}

	@Test(description = "Update player with valid ID using PATCH method")
	public void updatePlayerItemWithValidId() {
		Integer testPlayerId = preConditionCreatePlayer();
		Integer age = 55;
		String gender = "male";
		String login = "testLogin5";
		String password = "pass12345";
		String role = "user";
		String screenName = "TestPlayer777";
		PlayerItem player = new PlayerItem(login, password, screenName, gender, age, role);
		String jsonRequest = JsonMaker.serialize(player);

		logger.info("\n"+"Check expected status code - 200"+"\n");
		Specification.installSpec(Specification.requestSpec(URL), Specification.responseSpec200());
		String response = given()
				.body(jsonRequest)
				.when()
				.patch(updatePlayer+testPlayerId)
				.then().extract().response().asString();
		PlayerItem playerResponse = JsonMaker.deserialize(response, PlayerItem.class);

		logger.info("\n"+"Check that age was updated: " + playerResponse.getAge()+"\n");
		assertEquals(age, playerResponse.getAge());
		logger.info("\n"+"Check that gender was updated: " + playerResponse.getGender()+"\n");
		assertEquals(gender, playerResponse.getGender());
		logger.info("\n"+"Check that login was updated: " + playerResponse.getLogin()+"\n");
		assertEquals(login, playerResponse.getLogin());
		logger.info("\n"+"Check that screenName was updated: " + playerResponse.getScreenName()+"\n");
		assertEquals(screenName, playerResponse.getScreenName());

		postConditionDeletePlayer(testPlayerId);
	}

	@Test(description = "Update player with invalid ID using PATCH method")
	public void updatePlayerItemWithInvalidId() {
		Integer testPlayerId = 777;
		Integer age = 55;
		String gender = "male";
		String login = "testLogin5";
		String password = "pass12345";
		String role = "user";
		String screenName = "TestPlayer777";

		logger.info("\n"+"Check expected status code - 200"+"\n");
		Specification.installSpec(Specification.requestSpec(URL), Specification.responseSpec200());
		PlayerItem player = new PlayerItem(login, password, screenName, gender, age, role);
		String jsonRequest = JsonMaker.serialize(player);
		String response = given()
				.body(jsonRequest)
				.when()
				.patch(updatePlayer+testPlayerId)
				.then().extract().response().asString();

		logger.info("\n"+"Check that response is empty: " + response+"\n");
		assertTrue(response.isEmpty());
	}
}
