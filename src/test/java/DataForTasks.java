import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;

public class DataForTasks {

    public static final String URL_GOOGLE = "https://www.google.com/";
    public final int pages = 5; //количество страниц, которое надо обойти
    public final int ms = 10; //максимальное время, когда браузер ожидает появления элемента


    public static final String PLAY_GOOGLE = "play.google.com";

    public static class GooglePage{//класс страница гугла, где ниже элементы для поиска информации, кнопок и полей ввода
        public static By input = new By.ByCssSelector("input.gLFyf");//строка поиска
        public static By searchButton = new By.ByXPath(".//*[@class='FPdoLc tfB0Bf']/*/input[@aria-label='Поиск в Google']");//кнопка найти в гугл
        public static By getElement(String s){//метод возрата значения By по внутреннему названию элемента
            return new By.ByXPath("//*[contains(text(),'"+ s +"')]");
        }
        public static By googleLogo = new By.ByXPath("//img[@alt='Google']");//лого гугла
        public static By iviLinks = new By.ByXPath("//img/../..//*[text()='ivi.ru']|//*[text()='ivi.ru']");//ссылки на ivi в картинках

        public static By ratingText = new By.ByXPath("//*[contains(text(),'play.google')]/../../../..//*[contains(text(),'Рейтинг')]");//рейтинг приложения, потом парсится
        public static By wikiIvi = new By.ByXPath("//*[contains(text(),'wikipedia.org')][contains(text(),'Ivi')]|//*[contains(text(),'wikipedia.org')][contains(text(),'ivi')]");// ссылка на wiki+ivi
        public static By currentPageNumber = new By.ByXPath("//*[@class='cur']");//показывает текущую страницу поиска, для красоты
    }

    public static class WikiPage{//класс страница википедии
        public static By iviLink = new By.ByXPath("//*[@href='https://www.ivi.ru']");//ссылка на ivi
    }

    public static class PlayMarket{//класс страница гугл-плея
        public static By playRating =  new By.ByXPath("//div[@class][contains(@aria-label,'Средняя оценка: ')]");//рейтинг
    }
}
/*


*/