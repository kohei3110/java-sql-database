package com.example.controller;

import com.example.dao.*;
import com.example.model.*;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

@Named("todocontroller")
@ViewScoped
public class TodoListController implements Serializable {

    Logger log = Logger.getLogger(TodoListController.class.getName());

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %5$s %n");        
    }    

	private static final long serialVersionUID = 1L;

	@Inject
    TodoItemManagementInMemory todoManagement;
    
    @Setter @Getter
    private List<TodoItem> todoItems;

    @Setter @Getter
    private TodoItem selectedItem;

    @Setter @Getter
    private List<TodoItem> selectedItems;

    @Setter @Getter
    private String name;

    @Setter @Getter
    private String category;

    @PostConstruct
    public void init() throws Exception {
        TodoItem item1 = new TodoItem("App Services","Azure",false);
        TodoItem item2 = new TodoItem("Azure Kubernetes Service","Azure",false);
        TodoItem item3 = new TodoItem("JEP 359","Java",false);
        TodoItem item4 = new TodoItem("JEP 368","Java",false);
        TodoItem item5 = new TodoItem("MicroProfile","Java",false);
        TodoItem item6 = new TodoItem("Spring Boot","Java",false);
        TodoItem item7 = new TodoItem("Jakarta EE","Java",false);

        todoItems = new ArrayList<>();
        todoItems.add(item1);
        todoItems.add(item2);
        todoItems.add(item3);
        todoItems.add(item4);
        todoItems.add(item5);
        todoItems.add(item6);
        todoItems.add(item7);
    }

    public void buttonSQLAction() throws Exception{
        todoManagement.test();
    }

    public List<TodoItem> getTodoItems() {
        return todoManagement.getTodoItems();
    }

    public void buttonUpdateAction(){
        todoManagement.updateTodoItem(selectedItems);
    }

    public void buttonAddAction(){
        TodoItem addItem = new TodoItem(name, category, false);
        todoManagement.addTodoItem(addItem);
    }


    public void setSelectedItem(TodoItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public List<TodoItem> getSelectedItem() {
        return selectedItems;
    }

    public void setSelectedItems(List<TodoItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<TodoItem> getSelectedItems(){
        return selectedItems;
    }

    public void onRowSelect(SelectEvent<TodoItem> event) {
        FacesMessage msg = new FacesMessage("TodoItem Selected", event.getObject().getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent<TodoItem> event) {
        FacesMessage msg = new FacesMessage("Car Unselected", event.getObject().getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void insertData (TodoItem todo, Connection conn) throws SQLException {
        log.info("insert data");
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO todo (id, category, name, complete) VALUES (?, ?, ?, ?);");

        pstmt.setLong(1, todo.getId());
        pstmt.setString(2, todo.getCategory());
        pstmt.setString(3, todo.getName());
        pstmt.executeUpdate();
    }
}
