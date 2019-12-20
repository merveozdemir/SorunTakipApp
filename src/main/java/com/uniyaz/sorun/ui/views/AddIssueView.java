package com.uniyaz.sorun.ui.views;

import com.uniyaz.sorun.dao.CategoryDao;
import com.uniyaz.sorun.dao.IssueDao;
import com.uniyaz.sorun.domain.Category;
import com.uniyaz.sorun.domain.EnumIssueState;
import com.uniyaz.sorun.domain.Issue;
import com.uniyaz.sorun.ui.components.SaveButton;
import com.uniyaz.sorun.ui.components.StTextField;
import com.uniyaz.sorun.utils.HibernateUtil;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class AddIssueView extends VerticalLayout {
    private FormLayout mainLayout;
    StTextField idField;
    StTextField topicField;
    TextArea contentField;
    TextArea addressField;
    DateField dateField;
    ComboBox categoryComboBox;
    ComboBox labelComboBox;

    private Table table;

    public AddIssueView() {
        buildMainLayout();
    }

    public void updateTableBySaveButton(Table table){
        this.table = table;

    }

    private void buildMainLayout() {
        mainLayout = new FormLayout();
        addComponent(mainLayout);

        idField = new StTextField("Id");
        idField.setEnabled(false);
        mainLayout.addComponent(idField);

        topicField = new StTextField("Konu");
        mainLayout.addComponent(topicField);

        contentField = new TextArea("İçerik");
        mainLayout.addComponent(contentField);

        addressField = new TextArea("Adres");
        mainLayout.addComponent(addressField);

        dateField = new DateField("Tarih");
        mainLayout.addComponent(dateField);

        categoryComboBox = new ComboBox("Sorun kategorisi");
        List<Category> categoryList = getCategoryList();
        for (Category category : categoryList) {
            categoryComboBox.addItem(category);
        }
        mainLayout.addComponent(categoryComboBox);

        labelComboBox = new ComboBox("Sorun işlem durumunu");
        for (EnumIssueState value : EnumIssueState.values()) {
            labelComboBox.addItem(value);
        }
        mainLayout.addComponent(labelComboBox);

        SaveButton saveButton = new SaveButton();
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                Long idFieldValue = null;
                if (idField.getValue() != "") {
                    idFieldValue = Long.parseLong(idField.getValue());
                }

                String topicFieldValue = topicField.getValue();
                String contentFieldValue = contentField.getValue();
                String addressFieldValue = addressField.getValue();
                Date dateFieldValue = dateField.getValue();
                EnumIssueState enumIssueState = (EnumIssueState) labelComboBox.getValue();
                Category category = (Category) categoryComboBox.getValue();

                Issue issue = new Issue();
                issue.setId(idFieldValue);
                issue.setAddress(addressFieldValue);
                issue.setCategory(category);
                issue.setIssueState(enumIssueState);
                issue.setDate(dateFieldValue);
                issue.setContent(contentFieldValue);
                issue.setTopic(topicFieldValue);

                IssueDao issueDao = new IssueDao();
                issue = issueDao.saveIssue(issue);
                idField.setValue(issue.getId().toString());

                if(table != null){
                    updateAndFillTable(table);
                }

            }
        });
        mainLayout.addComponent(saveButton);
    }

    private List<Category> getCategoryList() {
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categoryList = categoryDao.findAllCategory();
        return categoryList;
    }

    public void fillViewByIssue(Issue issue) {
        idField.setValue(issue.getId().toString());
        topicField.setValue(issue.getTopic());
        addressField.setValue(issue.getAddress());
        contentField.setValue(issue.getContent());
        addressField.setValue(issue.getAddress());
        dateField.setValue(issue.getDate());
        contentField.setValue(issue.getContent());
        topicField.setValue(issue.getTopic());
        System.out.println(issue.getCategory());
        categoryComboBox.select(issue.getCategory());
        labelComboBox.select(issue.getIssueState());
    }

    private void updateAndFillTable(Table table) {
        IssueDao issueDao = new IssueDao();
        List<Issue> issueList = issueDao.findAllIssue();

        Container indexedContainer = table.getContainerDataSource();
        indexedContainer.removeAllItems();
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
}
