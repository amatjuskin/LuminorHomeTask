package tests;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.*;
import io.qameta.allure.Step;
import com.codeborne.selenide.Configuration;
import org.testng.Assert;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Selenide.*;
import io.qameta.allure.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/*
2. Automate below test steps as you would automate a regression test case:

+Open imdb.com
+Search for "QA" with the search bar
+When dropdown opens, save the name of the first title
+Click on the first title
+Verify that page title matches the one saved from the dropdown
+Verify there are more than 3 members in the "top cast section"
+Click on the 3rd profile in the "top cast section"
+Verify that correct profile have opened

Use: Gradle, Selenide, java 17, TestNG, Allure-report

Create public repository of your choice, attach URL when submitting your homework.
*/

@Epic("Epic Name")
@Feature("Feature Name")
public class IMBDtask {

        @Test(description = "Type QA in search, select first suggestion, select 3rd actor from top cast")
        @Story("Story")
        @Description("Type QA in search, select first suggestion, select 3rd actor from top cast")
        public void searchAndNavigate() {
            openWebsite("https://www.imdb.com");
            inputValue("#suggestion-search", "QA");
            // max movie/actor count on suggestion - 7
            selectFromSeach(1);
            //max top cast per page 18
            selectActorFromMoviePage(3);
        }

        // Possible improvements:
        // 1) Add validation, so the selected suggestion is a movie, and not an actor. If no year - actor.
        // 2) Move steps to their own location + sort by pages that they work on


        //DEFINITIONS

    private String savedTitle;
    private Integer actorCountVar;
    private String selectedActor;
    private String pageTitle;


    // Open url
    @Step("Open the website")
    public void openWebsite(String url) {
        open(url);
    }

    // Select input field + enter value
    @Step("Enter value into field")
    public void inputValue(String field, String value) {
        SelenideElement selectedField = $(field);
        selectedField.setValue(value);
    }

    //From suggestion list select suggestion by its order
    @Step("Click movie from list (by order)")
    public void selectFromSeach(int order) {
        Assert.assertTrue(order<8,"Max suggestions on page is 7, but selected order was "+order );
        SelenideElement selectedTitle = $$("a[data-testid=\"search-result--const\"] > div:nth-child(2) > div:nth-child(1)").get(order);
        savedTitle = selectedTitle.getText();
        selectedTitle.scrollIntoView(true).shouldBe(Condition.visible);
        sleep(1000);
        selectedTitle.click();
        pageTitle = title();
        Assert.assertTrue(pageTitle.contains(savedTitle),
                "Page title does not contain the expected title. Expected: " + savedTitle + ", Actual: " + pageTitle);
    }

    // Select actor from list of "TOP CAST" by order
    @Step("Click actor on movie page(by order) IF > 3")
    public void selectActorFromMoviePage(int order) {
        Assert.assertTrue(order<19,"Max TOP CAST actors on page is 18, but selected position was "+order );
        SelenideElement actorCount = $(".title-cast > .ipc-title > .ipc-title__wrapper > .ipc-title-link-wrapper > .ipc-title__text > .ipc-title__subtext").shouldBe(Condition.visible);
        actorCountVar = Integer.parseInt(actorCount.getText().replaceAll("[^0-9.]", ""));
        Assert.assertTrue(actorCountVar > 3, "Actor count is smaller than 3");
        SelenideElement actorFromList = $(".title-cast__grid > div.ipc-sub-grid > div:nth-child("+order+") > .vCane > a").shouldBe(Condition.visible);
        selectedActor = actorFromList.getText();
        actorFromList.scrollIntoView(true).shouldBe(Condition.visible);
        sleep(1000);
        actorFromList.shouldBe(Condition.visible).click();
        pageTitle = title();
        Assert.assertTrue(pageTitle.contains(selectedActor),
                "Page title does not contain the expected title. Expected: " + selectedActor + ", Actual: " + pageTitle);
    }

    //Browser settings
    @BeforeClass
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
    }

    // Close webdriver
    @AfterClass
    public void tearDown() {
        closeWebDriver();
    }

}