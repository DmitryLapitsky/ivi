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

    WebDriver driver;//завожу переменную типа вебдрайвер

    @BeforeSuite// аннотация testng для выполнения следующего метода перед началом тестирования (иерархия соответствует файлу local.xml)
    public void getDriver(){
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");//указываю, где искать браузер
        driver = new ChromeDriver();//инициализация браузера
        driver.manage().timeouts().implicitlyWait(ms, TimeUnit.SECONDS);//автопопытки найти элементы на странице
        driver.manage().timeouts().pageLoadTimeout(ms, TimeUnit.SECONDS);//ожидание загрузки страницы
        driver.manage().timeouts().setScriptTimeout(ms, TimeUnit.SECONDS);//ожидание отработки скриптов
    }

    @BeforeMethod// аннотация testng для выполнения следующего метода перед каждым тестом
    public void getSeach(){//первые шаги тестов одинаковы, поэтому в отдельный метод
        driver.get(URL_GOOGLE);//открытие сайта google
        driver.findElement(GooglePage.input).sendKeys("ivi");//ввод в строку поика ivi
        click(GooglePage.googleLogo);//так как выпадающий список скрывает кнопку поиска, то жму по логотипу гугла, чтобы список пропал
        click(GooglePage.searchButton);//нажимаю кнопку поиска
    }

    @BeforeMethod//после каждого теста возвращаюсь к исходной странице гугла
    public void getBack(){
        driver.get(URL_GOOGLE);
    }

    @AfterSuite//после тестов закрываю хром
    public void closeDriver(){
        if(driver!=null)
            driver.close();
    }

    @Test(groups = "homeWork",description = "проверка русской локали браузера")//первый тест на проверку локали браузера, если она английская, то тесты дальше не идут (зависимость по groups = "homeWork")
    public void browserLocale() {
        driver.findElement(GooglePage.getElement("Картинки"));//если находится элемент с текстом картинки, то все хорошо
    }

    @Test(dependsOnGroups = "homeWork.*", description = "проверка, что не менее 3 больших картинок в выдаче google ведут на сайт ivi.ru")//зависит от выполнения теста browserLocale, если он провалится, то этот не запустится
    public void imagesOfIviInGooglePictures(){
        click(GooglePage.getElement("Картинки"));//перехожу в раздел картинок
        click(GooglePage.getElement("Инструменты"));//открываю инструменты
        wait(GooglePage.getElement("Размер"));//ожидаю появления по раскрывающегося списка "размер" - появляется не всегда, поэтому на всякий
        click(GooglePage.getElement("Размер"));//щелкаю по нему
        click(GooglePage.getElement("Больш"));//нажимаю по большим картинкам. Вставил без окончания, т.к. иногда гугл склоняет это слово:(
        Assert.assertTrue(driver.findElements(GooglePage.iviLinks).size()>2, "всего " + driver.findElements(GooglePage.iviLinks).size() + " картинок ведут на ivi.ru");//ассерт не выполнении условия и при ошибке пишет, что не так
    }

    @Test(dependsOnGroups = "homeWork.*", description = "проверка совпадения рейтинга приложения в результатах поиска с рейтингом в play-маркете")
    public void playIvi(){
        SoftAssert asert = new SoftAssert();//так как google.play-ев может найтись много и где-то будут ошибки, ввожу софт-ассерт, чтобы тест нашел все ошибки и выдал их скопом, а не прекратил выполнение на первой ошибке
        for(int i = 0; i < pages; i++) {//цикл по количеству страниц
            try {
                int plays = finds(GooglePage.getElement(PLAY_GOOGLE)).size();//находит массив элементов с гугл-плеем (может быть нулевым-ничего не найдено)
                if(plays>0) {//если что-то найдено
                    for (int j = 0; j < plays; j++) {//в цикле по найденному
                        String googleRating = finds(GooglePage.ratingText).get(j).getText().split(" ")[1];//сохраняю рейтинг на странице поиска на соответствующей позиции, где был найден гугл-плей
                        String url = driver.getCurrentUrl();//сохраняю url
                        finds(GooglePage.getElement(PLAY_GOOGLE)).get(j).click();//перехожу на страницу гугл-плея
                        String playRating = driver.findElement(PlayMarket.playRating).getText();//сохраняю рейтинг гугл-плея
                        asert.assertEquals(googleRating.equals(playRating),true,"рейтинг play в поиске " + googleRating + " не совпадает c рейтингом в магазине play " + playRating);//сравниваю оценки, ошибка, если не одинаковы
                        driver.get(url);// возвращаюсь туда, откуда пришел
                    }
                }
                else{
                    System.out.println("на странице " + driver.findElement(GooglePage.currentPageNumber).getText() + " не найден элемент play.google.com");//если элементов гугл-плея нет, так и пишу
                }
            } catch (Exception e) { System.out.println(e);}//тут можно что-нибудь полезное сделать, но я не придумал
            finally {
                click(GooglePage.getElement("Следующая"));//в любом случае перехожу на следующую страницу
            }
        }
        asert.assertAll();//вывод накопившихся ошибок
    }

    @Test(dependsOnGroups = "homeWork.*", description = "проверка, что в статье ivi на wiki есть ссылка на официальный сайт ivi.ru")
    public void wikiIvi() {
        SoftAssert asert = new SoftAssert();//так как wiki-ссылок может найтись много и где-то будут ошибки, ввожу софт-ассерт, чтобы тест нашел все ошибки и выдал их скопом, а не прекратил выполнение на первой ошибке
        for (int i = 0; i < pages; i++) {//по заданному количеству страниц прохожу
            int wikis = finds(GooglePage.wikiIvi).size();//далее аналогично гугл-плею, только без try/catch
            if (wikis > 0) {
                for (int j = 0; j < wikis; j++) {
                    String url = driver.getCurrentUrl();
                    finds(GooglePage.wikiIvi).get(j).click();
                    asert.assertEquals(finds(WikiPage.iviLink).size()>0, true,"на странице ivi в wiki нет ссылки на ivi");//количество ссылок на ivi должно быть больше 0, ошибка, если 0
                    driver.get(url);
                }
                click(GooglePage.getElement("Следующая"));
            } else {
                System.out.println("на странице " + driver.findElement(GooglePage.currentPageNumber).getText() + " не найдена ссылка на wikipedia");
                click(GooglePage.getElement("Следующая"));//если не найдено wiki элементов, то перехожу на следующую страницу
            }
        }
        asert.assertAll();//вывод накопившихся ошибок
    }

    //небольшая оболочка над selenium-ом

    public void wait(By by){//метод явного ожидания элемента, на всякий
        try{
            (new WebDriverWait(driver, ms)).until(ExpectedConditions.presenceOfElementLocated(by));//ожидание появления элемента
        }catch(Exception e){System.out.println("элемент " + by + " не найден");}//если не найден, так и пишем
    }

    public List<WebElement> finds(By by){//метод поиска множества элементов, реализовывает вначале ожидание появления какого-либо первого элемента, а потом и выдачу всего массива элементов
        wait(by);
        return driver.findElements(by);
    }

    public void click(By by){//метод нажатия на элемент с предварительным ожиданием его появления
        try {
            WebElement el = (new WebDriverWait(driver, ms)).until(ExpectedConditions.presenceOfElementLocated(by));
            el.click();
        }catch(Exception e){Assert.fail("не найден элемент " + by);}
    }
}
