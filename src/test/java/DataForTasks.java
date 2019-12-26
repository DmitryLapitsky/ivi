import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;

public class DataForTasks {

    public static final String URL_GOOGLE = "https://www.google.com/";
    public final int pages = 5;
    public final int ms = 10;


    public static final String PLAY_GOOGLE = "play.google.com";

    public static class GooglePage{
        public static By input = new By.ByCssSelector("input.gLFyf");
        public static By searchButton = new By.ByXPath(".//*[@class='FPdoLc tfB0Bf']/*/input[@aria-label='Поиск в Google']");
        public static By getElement(String s){
            return new By.ByXPath("//*[contains(text(),'"+ s +"')]");
        }
        public static By googleLogo = new By.ByXPath("//img[@alt='Google']");
        public static By iviLinks = new By.ByXPath("//img/../..//*[text()='ivi.ru']|//*[text()='ivi.ru']");

        public static By ratingText = new By.ByXPath("//*[contains(text(),'play.google')]/../../../..//*[contains(text(),'Рейтинг')]");
        public static By wikiIvi = new By.ByXPath("//*[contains(text(),'wikipedia.org')][contains(text(),'Ivi')]|//*[contains(text(),'wikipedia.org')][contains(text(),'ivi')]");
        public static By currentPageNumber = new By.ByXPath("//*[@class='cur']");
    }

    public static class WikiPage{
        public static By iviLink = new By.ByXPath("//*[@href='https://www.ivi.ru']");
    }

    public static class PlayMarket{
        public static By playRating =  new By.ByXPath("//div[@class][contains(@aria-label,'Средняя оценка: ')]");
    }
}
/*


*/