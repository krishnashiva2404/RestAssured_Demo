import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import Files.Paylod;
import Files.ReusableMethod;

public class Basic {

	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		
		// PlaceID-->update placeId with new address ---> Get placeId validate if new address is present in response
		// Handle static Json means using external json file  to do post method for this
		// body() method accept only string so first we convert file content into ->Bytes the Bytes->String
		// for this in java we Files.AddByte
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String responsebody=given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\shiva\\OneDrive\\Desktop\\Addplace.json")))).when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.41 (Ubuntu)")
		.extract().response().asString();
		
		//response store in one variable by using extract().response().asString();
		// variable name is responsebody
		
		System.out.println(responsebody);
		
		
		// by using jasonpath extract the  string  input and convert to json data 
		
		JsonPath js=new JsonPath(responsebody);
		String placeid = js.getString("place_id");
		
		System.out.println(placeid);
		
		// update the address using PUT method by PlaceId
		
		String newaddress="60 summer walk UK";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeid+",\r\n"
				+ "\"address\":\""+newaddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("/maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200);
				
		
		
		String getplaceresponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id",placeid)
		.when().get("/maps/api/place/get/json")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println(getplaceresponse);
		
		JsonPath js1=ReusableMethod.rawToJson(getplaceresponse);
		String actualaddress = js1.getString("address");
		
		System.out.println(actualaddress);
		
		Assert.assertEquals(actualaddress, newaddress);
		
		
		

	}

}
