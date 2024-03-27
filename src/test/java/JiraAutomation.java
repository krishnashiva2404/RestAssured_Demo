import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraAutomation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI="http://localhost:8080";
		SessionFilter session=new SessionFilter();
		// login scenario
		String response=given().header("Content-Type","application/json").body("{ \r\n"
				+ "    \"username\": \"krishshiva468\", \r\n"
				+ "    \"password\": \"9533833814@s\" \r\n"
				+ "}").filter(session).when().log().all().post("/rest/auth/1/session").then().extract().response().asString();
		
		// add comment scenario
		
		String addcomment="hi this is ramarao";
		String commentresponse = given().pathParam("key", "10001").header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"body\": \""+addcomment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().log().all().post("/rest/api/2/issue/{key}/comment").then().assertThat().statusCode(201).extract().response().asString();
		

		JsonPath js1=new JsonPath(commentresponse);
		String commentId = js1.get("id");
		
		// add attachement
		
		given().header("X-Atlassian-Token","no-check").pathParam("key", "10001").header("Content-Type","multipart/form-data")
		.multiPart("file",new File("Jiraaa.txt")).filter(session).when().post("/rest/api/2/issue/{key}/attachments")
		.then().log().all().extract().response().asString();
		
		
		// get issue
		
		String getissue = given().filter(session).pathParam("key", "10001").queryParam("fields", "comment").when().log().all().get("/rest/api/2/issue/{key}")
		.then().log().all().extract().response().asString();
		System.out.println(getissue);
		
		JsonPath js=new JsonPath(getissue);
		int commentscount = js.getInt("fields.comment.comments.size()");
		System.out.println(commentscount);
		
		for(int i=0;i<commentscount;i++) {
			String IssuecommentsID = js.get("fields.comment.comments["+i+"].id").toString();
			
			if(IssuecommentsID.equalsIgnoreCase(commentId)) {
				String commentbody = js.get("fields.comment.comments["+i+"].body");
				Assert.assertEquals(commentbody, addcomment);
				
			}
			
		}
	}

}
