package com.company.Repository;

import com.company.dto.TodoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoRepository {
    private Map<Long, List<TodoItem>> todoMap = new HashMap<>();

    public int add(Long userId, TodoItem todoItem) {
        if (todoMap.containsKey(userId)) {
            todoMap.get(userId).add(todoItem);
            return todoMap.get(userId).size();
        } else {
            List<TodoItem> todoItems = new ArrayList<>();

            todoItems.add(todoItem);
            todoMap.put(userId, todoItems);
            return 1;
        }
    }

    public boolean delete(Long userId, TodoItem todoItem){
        if(!todoMap.containsKey(userId)){
            return false;
        }
        todoMap.get(userId).remove(todoItem);
        return true;
    }

    public List<TodoItem> getTodoList(Long userId) {
        return todoMap.get(userId);
    }

    public TodoItem getItem(Long userId, String id) {
        if (todoMap.containsKey(userId)) {
            for (TodoItem todoItem : todoMap.get(userId)) {
                if (todoItem.getId().equals(id)) {
                    return todoItem;
                }
            }
        }
        return null;
    }

//    public boolean setItem(Long userId, String id, TodoItem newTodoItem) {
//        if (todoMap.containsKey(userId)) {
//            List<TodoItem> list = todoMap.get(userId);
//
//            if (list.set(list.indexOf(newTodoItem), newTodoItem) != null) {
//                todoMap.put(userId, list);
//                return true;
//            }
//        }
//        return false;
//    }
}
