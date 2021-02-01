package com.jhoysbou.TBot.models;

public class MenuItemStorageDto {
    private MenuItem menuItem;
    private long id;

    public MenuItemStorageDto() {
    }

    public MenuItemStorageDto(MenuItem menuItem, long id) {
        this.menuItem = menuItem;
        this.id = id;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
