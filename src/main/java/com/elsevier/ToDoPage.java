package com.elsevier;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ToDoPage {
    private final Page page;
    private Locator toDoField, listItems, item, editableItem, destroyItem, checkbox, toDoList;
    private String editedItem;


    public ToDoPage(Page page) {
        this.page = page;
        this.toDoField = page.getByPlaceholder("What needs to be done?");
    }

    public void navigate(String framework) {
        framework.toLowerCase();
        switch (framework) {
            case "react" -> page.navigate("https://todomvc.com/examples/react/#/");
            case "angular" -> page.navigate("https://todomvc.com/examples/angularjs/#/");
            case "vue" -> page.navigate("https://todomvc.com/examples/vue/");
            default -> throw new IllegalArgumentException("Unsupported web browser: " + framework);
        }
    }
    public Locator getToDoField() {
        return toDoField;
    }
    public void addToDoItem(String toDoItem){
        toDoField.fill(toDoItem);
        toDoField.press("Enter");
    }

    public void destroyItem(String itemToDestroy){
        //Bear in mind that hasText functionality will use substrings as inputs and destroy that item
        //e.g: if itemDestroy was 'Test' it would destroy 'Testing...'
        listItems = page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText(itemToDestroy));
        destroyItem = listItems.locator("button");
        listItems.hover();
        destroyItem.click();
        //        listItems = page.locator(String.format("text=%s", itemToDestroy));
//        for (int i = 0; i < listItems.count(); i++) {
//            String text = listItems.nth(i).textContent();
//            if (text.contentEquals(itemToDestroy)){
//                listItems.nth(i).hover();
//                listItems.nth(i).locator(":scope", new Locator.LocatorOptions().setHasText(itemToDestroy)).locator(".destroy").click();
//                break;
//            }
//        }
    }

    public void completeItem(String itemToComplete){
        toDoList = page.locator("ul.todo-list li");
        item = toDoList.locator(":scope", new Locator.LocatorOptions().setHasText(itemToComplete)).locator(".toggle");
        item.click();
    }

    public void itemToEdit(String itemToEdit){
        editableItem = page.getByText(itemToEdit);
        editableItem.dblclick();
        editableItem.press("Backspace");
    }

}
