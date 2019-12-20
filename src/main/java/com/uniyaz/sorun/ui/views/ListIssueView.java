package com.uniyaz.sorun.ui.views;

import com.uniyaz.sorun.dao.CategoryDao;
import com.uniyaz.sorun.dao.IssueDao;
import com.uniyaz.sorun.domain.Category;
import com.uniyaz.sorun.domain.EnumIssueState;
import com.uniyaz.sorun.domain.Issue;
import com.uniyaz.sorun.ui.components.DeleteButton;
import com.uniyaz.sorun.ui.components.SaveButton;
import com.uniyaz.sorun.utils.HibernateUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class ListIssueView extends VerticalLayout {
    private Table table;
    private IndexedContainer indexedContainer;
    private AddIssueView addIssueView;
    private DeleteButton deleteButton;
    Issue issue;

    public ListIssueView() {
        setSpacing(true);

        buildTableContainer();
        buildTable();
        addComponent(table);
        fillTable();

        deleteButton = new DeleteButton();
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                IssueDao issueDao = new IssueDao();
                issueDao.deleteIssue(issue);
                updateTable();

            }
        });
        addComponent(deleteButton);

        addIssueView = new AddIssueView();
        addComponent(addIssueView);
    }

    private void updateTable(){
        indexedContainer.removeAllItems();
        fillTable();
    }

    private void buildTableContainer() {
        indexedContainer = new IndexedContainer();
        indexedContainer.addContainerProperty("id", Long.class, null);
        indexedContainer.addContainerProperty("topic", String.class, null);
        indexedContainer.addContainerProperty("category_name", Category.class, null);
        indexedContainer.addContainerProperty("content", String.class, null);
        indexedContainer.addContainerProperty("date", Date.class, null);
        indexedContainer.addContainerProperty("address", String.class, null);
        indexedContainer.addContainerProperty("issue_state", EnumIssueState.class, null);

    }


    private void fillTable() {
        IssueDao issueDao = new IssueDao();
        List<Issue> issueList = issueDao.findAllIssue();
        for (Issue issue : issueList) {
                Item item = indexedContainer.addItem(issue);
                item.getItemProperty("id").setValue(issue.getId());
                item.getItemProperty("topic").setValue(issue.getTopic());
                item.getItemProperty("category_name").setValue(issue.getCategory());
                item.getItemProperty("content").setValue(issue.getContent());
                item.getItemProperty("date").setValue(issue.getDate());
                item.getItemProperty("address").setValue(issue.getAddress());
                item.getItemProperty("issue_state").setValue(issue.getIssueState());
            }
    }

    private void buildTable() {
        table = new Table();
        table.setHeight("250px");
        table.setContainerDataSource(indexedContainer);
        table.setColumnHeaders("NO", "BAŞLIK", "KATEGORİ", "İÇERİK", "TARİH", "ADRES", "SORUN DURUMU");
        table.setSelectable(true);
        table.setMultiSelectMode(MultiSelectMode.SIMPLE);
        table.setMultiSelect(false);
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                issue = (Issue) itemClickEvent.getItemId();
                addIssueView.fillViewByIssue(issue);
                addIssueView.updateTableBySaveButton(table);

            }
        });
    }

    private List<Category> getCategoryList() {
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categoryList = categoryDao.findAllCategory();
        return categoryList;
    }

}
