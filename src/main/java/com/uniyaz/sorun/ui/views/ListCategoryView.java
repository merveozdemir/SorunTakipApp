package com.uniyaz.sorun.ui.views;

import com.uniyaz.sorun.dao.CategoryDao;
import com.uniyaz.sorun.domain.Category;
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

import java.util.List;

/**
 * Created by AKARTAL on 18.12.2019.
 */
public class ListCategoryView extends VerticalLayout {

    private Table table;
    private IndexedContainer indexedContainer;
    private AddCategoryView addCategoryView;
    private DeleteButton deleteButton;
    Category category;

    public ListCategoryView() {
        setSpacing(true);

        buildTableContainer();
        buildTable();
        addComponent(table);
        fillTable();

        deleteButton = new DeleteButton();
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                CategoryDao categoryDao = new CategoryDao();
                categoryDao.deleteCategory(category);
                updateTable();
            }
        });
        addComponent(deleteButton);

        addCategoryView = new AddCategoryView();
        addComponent(addCategoryView);
    }

    private void updateTable(){
        indexedContainer.removeAllItems();
        fillTable();
    }

    private void fillTable() {
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categoryList = categoryDao.findAllCategory();
        for (Category category : categoryList) {
            Item item = indexedContainer.addItem(category);
            item.getItemProperty("id").setValue(category.getId());
            item.getItemProperty("name").setValue(category.getName());
        }
    }

    private void buildTableContainer() {

        indexedContainer = new IndexedContainer();
        indexedContainer.addContainerProperty("id", Long.class, null);
        indexedContainer.addContainerProperty("name", String.class, null);
    }

    private void buildTable() {
        table = new Table();
        table.setHeight("250px");
        table.setContainerDataSource(indexedContainer);
        table.setColumnHeaders("NO", "İSİM");
        table.setSelectable(true);
        table.setMultiSelectMode(MultiSelectMode.SIMPLE);
        table.setMultiSelect(false);
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                category = (Category) itemClickEvent.getItemId();
                addCategoryView.fillViewByCategory(category);
                addCategoryView.updateTableBySaveButton(table);
            }
        });
    }


}
