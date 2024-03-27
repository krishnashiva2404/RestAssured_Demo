import org.testng.Assert;

import Files.Paylod;
import io.restassured.path.json.JsonPath;

public class ComplexJsonPath {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JsonPath js = new JsonPath(Paylod.courseprice());

		System.out.println("1. Print No of courses returned by API");
		int coursecount = js.getInt("courses.size()");
		System.out.println(coursecount);
		
		System.out.println("2. Print Purchase Amount");
		int billamt=js.getInt("dashboard.purchaseAmount");
		System.out.println(billamt);
		
		System.out.println("3. Print Title of the first course");
		System.out.println(js.get("courses[0].title"));
		
		System.out.println("4. Print All course titles and their respective Prices");
		for(int i=0;i<coursecount;i++) {
			System.out.print(js.get("courses["+i+"].title")+" : ");
			System.out.println(js.getInt("courses["+i+"].price"));
		}
		
		System.out.println("5. Print no of copies sold by RPA Course");
		for(int i=0;i<coursecount;i++) {
			String coursetitle = js.get("courses["+i+"].title");
			if(coursetitle.equalsIgnoreCase("RPA")) {
				System.out.println(js.getInt("courses["+i+"].copies"));
			}
		}
		
		System.out.println("6. Verify if Sum of all Course prices matches with Purchase Amount");
		int sum=0;
		for(int i=0;i<coursecount;i++) {
			int amt = js.getInt("courses["+i+"].copies")*js.getInt("courses["+i+"].price");
			sum=sum+amt;
		}
		if(sum==billamt) {
			System.out.println("The Sum of all Course prices matches with Purchase Amount ");
		}else {
			System.out.println("not match");
		}
		
		Assert.assertEquals(sum, billamt);
		

	}

}
