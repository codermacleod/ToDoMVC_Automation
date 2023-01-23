package com.elsevier;


import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
public class ToDoMVCTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    Locator addItem, addedItem;

    @Test
    void shouldCheckToDoItemsAdded(){
        var toDoPage = new ToDoPage(page);
        toDoPage.navigate("react");
        assertThat(page).hasURL("https://todomvc.com/examples/react/#/");
        page.pause();
        toDoPage.addToDoItem("Testing...");
        assertThat(page.locator("span.todo-count")).hasText("1 item left");
        toDoPage.addToDoItem("");
        toDoPage.addToDoItem(" ");
        assertThat(page.locator("span.todo-count")).hasText("1 item left");
        toDoPage.addToDoItem("A");
        assertThat(page.locator("span.todo-count")).hasText("2 items left");
        toDoPage.addToDoItem("\uD83D\uDE42");
        assertThat(page.locator("span.todo-count")).hasText("3 items left");
    }
    @Test
    void shouldCheckToDoItemsDeletedRegardlessOfStatus(){
        var toDoPage = new ToDoPage(page);
        toDoPage.navigate("react");
        assertThat(page).hasURL("https://todomvc.com/examples/react/#/");
        page.pause();
        toDoPage.addToDoItem("Zeus");
        toDoPage.completeItem("Zeus");
        toDoPage.addToDoItem("Apollo");
        toDoPage.addToDoItem("Jesus");
        toDoPage.destroyItem("Zeus");
        toDoPage.destroyItem("Apollo");
        assertThat(page.locator("span.todo-count")).hasText("1 item left");
    }

    @Test
    void shouldCheckIfAllItemsAreCompletedAfterClickingArrow(){
        var toDoPage = new ToDoPage(page);
        toDoPage.navigate("react");
        assertThat(page).hasURL("https://todomvc.com/examples/react/#/");
        page.pause();
        toDoPage.addToDoItem("New Item");
        toDoPage.addToDoItem("New Item 2");
        toDoPage.addToDoItem("New Item 3");
        page.locator("label").first();
    }

    @Test
    void shouldCheckItemsCanBeModified(){
        var toDoPage = new ToDoPage(page);
        toDoPage.navigate("react");
        assertThat(page).hasURL("https://todomvc.com/examples/react/#/");
        page.pause();
        toDoPage.addToDoItem("Op74!3d%U2GQl2u@lknOB5WqN0A4V7");
        toDoPage.itemToEdit("Op74!3d%U2GQl2u@lknOB5WqN0A4V7");
        page.keyboard().press("Enter");
        assertThat(page.getByText("Op74!3d%U2GQl2u@lknOB5WqN0A4V", new Page.GetByTextOptions().setExact(true))).isVisible();
        toDoPage.itemToEdit("Op74!3d%U2GQl2u@lknOB5WqN0A4V");
        page.keyboard().press("Escape");
        assertThat(page.getByText("Op74!3d%U2GQl2u@lknOB5WqN0A4V", new Page.GetByTextOptions().setExact(true))).isVisible();
    }

    @Test
    void shouldCheckThatToDoListItemIsEditable() {
        page.navigate("https://todomvc.com/examples/react/#/");
        assertThat(page).hasTitle("React • TodoMVC");
        page.pause();
        addItem = page.getByPlaceholder("What needs to be done?");
        addItem.fill("AAAA");
        addItem.press("Enter");
        addedItem = page.locator("div.view");
        addedItem.dblclick();
        addedItem.press("Backspace");
        addedItem.press("Enter");
        assertThat(page.locator("div.view")).hasText("AAA");
        assertThat(page.locator("span.todo-count")).hasText("1 item left");
    }

    @Test
    void shouldCheckThatToDoListAcceptsAccentedCharacter(){
        page.navigate("https://todomvc.com/examples/react/#/");
        assertThat(page).hasTitle("React • TodoMVC");
        addItem = page.getByPlaceholder("What needs to be done?");
        addItem.fill("é è ê ë ç ñ ø ð å æ œ ē č ŭ");
        addItem.press("Enter");
        assertThat(page.locator("div.view")).hasText("é è ê ë ç ñ ø ð å æ œ ē č ŭ");
        assertThat(page.locator("span.todo-count")).hasText("1 item left");
    }

    @Test
    void shouldCheckThatToDoListAcceptsEmojis() {
        page.navigate("https://todomvc.com/examples/react/#/");
        assertThat(page).hasTitle("React • TodoMVC");
        addItem = page.getByPlaceholder("What needs to be done?");
        addItem.fill("\uD83D\uDE42");
        addItem.press("Enter");
        assertThat(page.locator("div.view")).hasText("\uD83D\uDE42");
        assertThat(page.locator("span.todo-count")).hasText("1 item left");
    }


//    @Test
//    void shouldCheckItemIsDraggable() {
//        page.navigate("https://codepen.io/PJCHENder/pen/PKBVRO/");
//        assertThat(page).hasURL("https://codepen.io/PJCHENder/pen/PKBVRO/");
//        page.pause();
//        page.frameLocator("#result").getByText("One").dragTo(page.frameLocator("#result").getByText("Two"));
//    }

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions() // or firefox, webkit
                .setHeadless(false)
                .setSlowMo(500));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }
}

