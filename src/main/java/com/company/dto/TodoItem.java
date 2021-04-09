package com.company.dto;

import com.company.enums.TodoItemType;

import java.util.Date;
import java.util.Objects;

public class TodoItem {
    private String id;
    private String title;
    private String content;
    private Date date;
    private Long userId;
    private TodoItemType type;

    public TodoItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public TodoItemType getType() {
        return type;
    }

    public void setType(TodoItemType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return Objects.equals(id, todoItem.id) &&
                Objects.equals(title, todoItem.title) &&
                Objects.equals(content, todoItem.content) &&
                Objects.equals(userId, todoItem.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, userId);
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", userId=" + userId +
                ", type=" + type +
                '}';
    }
}
