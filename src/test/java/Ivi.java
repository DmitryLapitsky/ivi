import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import org.testng.reporters.jq.Main;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ivi extends DataForTasks{

    WebDriver driver;

    @BeforeSuite
    public void getDriver(){
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(ms, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(ms, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(ms, TimeUnit.SECONDS);
    }

    @BeforeMethod
    public void getSeach(){
        driver.get(URL_GOOGLE);
        driver.findElement(GooglePage.input).sendKeys("ivi");
        click(GooglePage.input);
        click(GooglePage.googleLogo);
        click(GooglePage.searchButton);
    }

    @BeforeMethod
    public void getBack(){
        driver.get(URL_GOOGLE);
    }

    @AfterSuite
    public void closeDriver(){
        if(driver!=null)
            driver.close();
    }

    @Test(groups = "homeWork",description = "проверка русской локали браузера")
    public void browserLocale() {
        driver.findElement(GooglePage.getElement("Картинки"));
    }

    @Test(dependsOnGroups = "homeWork.*", description = "проверка, что не менее 3 больших картинок в выдаче google ведут на сайт ivi.ru")
    public void imagesOfIviInGooglePictures(){
        click(GooglePage.getElement("Картинки"));
        click(GooglePage.getElement("Инструменты"));
        wait(GooglePage.getElement("Размер"));
        click(GooglePage.getElement("Размер"));
        click(GooglePage.getElement("Больш"));
        Assert.assertTrue(driver.findElements(GooglePage.iviLinks).size()>2, "всего " + driver.findElements(GooglePage.iviLinks).size() + " картинок ведут на ivi.ru");
    }

    @Test(dependsOnGroups = "homeWork.*", description = "проверка совпадения рейтинга приложения в результатах поиска с рейтингом в play-маркете")
    public void playIvi(){
        SoftAssert asert = new SoftAssert();
        for(int i = 0; i < pages; i++) {
            try {
                int plays = finds(GooglePage.getElement(PLAY_GOOGLE)).size();
                if(plays>0) {
                    for (int j = 0; j < plays; j++) {
                        String googleRating = finds(GooglePage.ratingText).get(j).getText().split(" ")[1];
                        String url = driver.getCurrentUrl();
                        finds(GooglePage.getElement(PLAY_GOOGLE)).get(j).click();
                        String playRating = driver.findElement(PlayMarket.playRating).getText();
                        asert.assertEquals(googleRating.equals(playRating),true,"рейтинг play в поиске " + googleRating + " не совпадает c рейтингом в магазине play " + playRating);
                        driver.get(url);
                    }
                }
            } catch (Exception e) {
                System.out.println("на странице " + driver.findElement(GooglePage.currentPageNumber).getText() + " не найден элемент play.google.com");
            } finally {
                click(GooglePage.getElement("Следующая"));
            }
        }
        asert.assertAll();
    }

    @Test(dependsOnGroups = "homeWork.*", description = "проверка, что в статье ivi на wiki есть ссылка на официальный сайт ivi.ru")
    public void wikiIvi() {
        SoftAssert asert = new SoftAssert();
        for (int i = 0; i < pages; i++) {
            int wikis = finds(GooglePage.wikiIvi).size();
            if (wikis > 0) {
                for (int j = 0; j < wikis; j++) {
                    String url = driver.getCurrentUrl();
                    finds(GooglePage.wikiIvi).get(j).click();
                    asert.assertEquals(finds(WikiPage.iviLink).size()>0, true,"на странице ivi в wiki нет ссылки на ivi");//.assertTrue(finds(GooglePage.getElement(URL_IVI)).size()>0, "на странице ivi в wiki нет ссылки на ivi");
                    driver.get(url);
                }
                click(GooglePage.getElement("Следующая"));
            } else {
                System.out.println("на странице " + driver.findElement(GooglePage.currentPageNumber).getText() + " не найдена ссылка на wikipedia");
                click(GooglePage.getElement("Следующая"));
            }
        }
        asert.assertAll();
    }

    public void wait(By by){
        try{
            (new WebDriverWait(driver, ms)).until(ExpectedConditions.presenceOfElementLocated(by));
        }catch(Exception e){System.out.println("элемент " + by + " не найден");}
    }

    public List<WebElement> finds(By by){
        wait(by);
        return driver.findElements(by);
    }

    public void click(By by){
        try {
            WebElement el = (new WebDriverWait(driver, ms)).until(ExpectedConditions.presenceOfElementLocated(by));
            el.click();
        }catch(Exception e){Assert.fail("не найден элемент " + by);}
    }
}
