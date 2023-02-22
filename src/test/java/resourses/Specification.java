package resourses;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specification {

	public static void installSpec(RequestSpecification request, ResponseSpecification response){
		RestAssured.requestSpecification = request;
		RestAssured.responseSpecification = response;
	}

	public static RequestSpecification requestSpec(String url){
		return new RequestSpecBuilder()
				.setBaseUri(url)
				.setContentType(ContentType.JSON)
				.build();
	}

	public static ResponseSpecification responseSpec200(){
		return new ResponseSpecBuilder()
				.expectStatusCode(200)
				.build();
	}

	public static ResponseSpecification responseSpec204(){
		return new ResponseSpecBuilder()
				.expectStatusCode(204)
				.build();
	}

	public static ResponseSpecification responseSpec400(){
		return new ResponseSpecBuilder()
				.expectStatusCode(400)
				.build();
	}

	public static ResponseSpecification responseSpec403(){
		return new ResponseSpecBuilder()
				.expectStatusCode(403)
				.build();
	}
}
