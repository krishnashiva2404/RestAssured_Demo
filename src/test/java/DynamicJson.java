import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Files.Paylod;
import Files.ReusableMethod;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;

public class DynamicJson {
	
	
	@Test(dataProvider="Booksdata")
	public void addbook(String isbn,String aisle) {
		
		RestAssured.baseURI="http://216.10.245.166";
		
		String responsebody = given().header("Content-Type","application/json")
		.body(Paylod.libraryAddbook(isbn,aisle))
		.when().post("Library/Addbook.php").then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1=ReusableMethod.rawToJson(responsebody);
		String id=js1.get("ID");
		System.out.println("The book ID is : "+id);
		
	}
	
	@Test(dataProvider="Deletedata")
	public void deletebook(String bookid) {
		RestAssured.baseURI="http://216.10.245.166";
		String deleteresponsebody=given().header("Content-Type","application/json")
		.body(Paylod.libraryDeletebook(bookid))
		.when().post("/Library/DeleteBook.php")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println(deleteresponsebody);
	}
	
	
	@DataProvider(name="Booksdata")
	public Object[][] getdata() {
		
		
		// creating multi dimentional array for multiple sets of data
		// multidimentional array= collection of arrays
		
		return new Object[][] {{"krisna","33814"},{"sathya","99638"},{"maha","77316"}};
	}
	
	@DataProvider(name="Deletedata")
	public Object[] getdeletedata() {
		return new Object[] {"krishna33814","sathya99638","maha77316"};
	}
	

}
