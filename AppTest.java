package assignment.com.qait.mavenTatoc;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTest
{
	WebDriver driver;
	@BeforeTest
	public void pageload()
	{
		String exepath="/home/qainfotech/chromedriver";
		System.setProperty("webdriver.chrome.driver", exepath);
		driver=new ChromeDriver();
	}
	@Test
	public void firstpage()
	{
		driver.get("http://10.0.1.86/tatoc");
		/*Assert.assertEquals("Welcome - T.A.T.O.C",driver.getTitle());
		driver.findElement(By.linkText("Basic Course")).click();
		*/
		verifyPageAddress("Welcome - T.A.T.O.C" );

	}
	@Test
	public void tatocpage()
	{
		driver.findElement(By.linkText("Basic Course")).click();
		verifyPageAddress("Grid Gate - Basic Course - T.A.T.O.C");
	}
	
	@Test(dependsOnMethods= {"tatocpage"})
	public void greenBox()
	{
		WebElement greenbox= driver.findElement(By.className("greenbox"));
		Assert.assertEquals(true, greenbox.isDisplayed());
		greenbox.click();
		verifyPageAddress("Frame Dungeon - Basic Course - T.A.T.O.C");
	}
	@Test(dependsOnMethods={"greenBox"})
	public void MatchColour()
	{
//boolean condition =true;
//driver.findElement(By.tagName("iframe"));

		driver.switchTo().frame("main");
		String boxcolor = 
				driver.findElement(By.id("answer")).getAttribute("class");
		String tomatchbox;
		while(true)
		{
			driver.switchTo().defaultContent();
			driver.switchTo().frame("main");

			driver.findElement(By.linkText("Repaint Box 2")).click();
			driver.switchTo().frame("child");
			tomatchbox=driver.findElement(By.id("answer")).getAttribute("class");
//driver.switchTo().defaultContent();
			driver.switchTo().parentFrame();

			if(boxcolor.equals(tomatchbox))
			{
				 Assert.assertEquals(boxcolor,tomatchbox);
				 driver.findElement(By.linkText("Proceed")).click();
			
				break;

			}

		}
	 verifyPageAddress("Drag - Basic Course - T.A.T.O.C");
	}
	
	@Test(dependsOnMethods={"MatchColour"})
	public void DragNDrop()
	{
	    Assert.assertEquals(driver.findElement(By.id("dragbox")).isDisplayed(),true);

	    WebElement source = driver.findElement(By.id("dragbox"));
	    Assert.assertEquals(driver.findElement(By.id("dropbox")).isDisplayed(),true);
	    WebElement target = driver.findElement(By.id("dropbox"));
	    Actions act=new Actions(driver);
	    act.dragAndDrop(source, target).build().perform();

	    driver.findElement(By.cssSelector("a")).click();
	    
	    verifyPageAddress("Windows - Basic Course - T.A.T.O.C");
	    
	//  driver.findElement(By.cssSelector("a")).click();

	}
	@Test(dependsOnMethods= {"DragNDrop"})
	public void popUpWindows()
	{
		driver.findElement(By.cssSelector("a")).click();
		    ArrayList handle= new ArrayList(driver.getWindowHandles());
		    String window1=(String)handle.get(1);
		    driver.switchTo().window(window1);
		    verifyPageAddress("Popup - Basic Course - T.A.T.O.C");
		    WebElement inputfield= driver.findElement(By.id("name"));
		    inputfield.sendKeys("Shalini");
		// Assert.assertEquals(inputfield.getText(),"Shalini");
		    Assert.assertEquals(true, driver.findElement(By.id("submit")).isDisplayed());
		    driver.findElement(By.id("submit")).click();
		    String window2=(String)handle.get(0);
		    driver.switchTo().window(window2);
		    verifyPageAddress("Windows - Basic Course - T.A.T.O.C");
		    driver.findElements(By.cssSelector("a")).get(1).click();
		    verifyPageAddress("Cookie Handling - Basic Course - T.A.T.O.C");
		    
	}
	@Test(dependsOnMethods= {"popUpWindows"})
	public void cookie()
	{

		driver.findElement(By.cssSelector("a")).click();
	    Assert.assertEquals(true, driver.findElement(By.id("token")).isDisplayed());

	    String Token=driver.findElement(By.id("token")).getText();
	    System.out.println(Token);
	    String tokendata=Token.substring(7);
	    Cookie name=new Cookie("Token", tokendata);
	    driver.manage().addCookie(name);
	    
	    driver.findElements(By.cssSelector("a")).get(1).click();
	    verifyPageAddress("End - T.A.T.O.C");
	    
	}
	



	public void verifyPageAddress(String expectedTitle)
	{
		String actualTitle= driver.getTitle();
		try
		{
			Assert.assertEquals(expectedTitle, actualTitle);
			System.out.println("Test Passed");
		}
		catch(Throwable e)
		{
			System.out.println("Test Failed");
		}
	}
	
	@AfterTest
	public void terminate()
	{
		driver.close();
	}
}





