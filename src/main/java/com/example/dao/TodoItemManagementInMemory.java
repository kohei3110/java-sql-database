package com.example.dao;

import com.example.model.*;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.example.controller.*;
import java.util.logging.*;
import java.sql.*;
import java.util.*;

@ApplicationScoped
public class TodoItemManagementInMemory implements ItemManagement{

    Logger log = Logger.getLogger(TodoListController.class.getName());

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %5$s %n");        
    }    

    private CopyOnWriteArrayList<TodoItem> todoItems  = new CopyOnWriteArrayList<TodoItem>();

    public  CopyOnWriteArrayList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public  void setTodoItems(CopyOnWriteArrayList<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    public void addTodoItem(TodoItem item) {
        synchronized (this){
            int size = todoItems.size();
            long id = 0;
            if(size != 0){
                id = todoItems.get(size - 1).getId();
                id++;
            }
            item.setId(id);
            todoItems.add(item);
        }
    }

    public void updateTodoItem(List<TodoItem> items){
        items.stream().forEach(item -> {
            item.setComplete(true);
            synchronized (this) {
                todoItems.set(item.getId().intValue(), item);
            }
        });
    }

    public void test() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        log.info("Loading application properties");
        Properties properties = new Properties();
        properties.load(TodoListController.class.getClassLoader().getResourceAsStream("application.properties"));

        log.info("Connecting to the database");
        Connection conn = DriverManager.getConnection(properties.getProperty("url"), properties);
        log.info("Database connection test: " + conn.getCatalog());

        log.info("Create database schema");
        Scanner scanner = new Scanner(TodoListController.class.getClassLoader().getResourceAsStream("schema.sql"));
        Statement stmt = conn.createStatement();
        while (scanner.hasNextLine()) {
            stmt.executeQuery(scanner.nextLine());
        }
    }
}