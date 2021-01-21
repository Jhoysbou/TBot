package com.jhoysbou.TBot.models.vkmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDAO {
    private long id;
    private String first_name;
    private String last_name;
    private String deactivated;
    private boolean is_closed;
    private boolean can_access_closed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(String deactivated) {
        this.deactivated = deactivated;
    }

    public boolean isIs_closed() {
        return is_closed;
    }

    public void setIs_closed(boolean is_closed) {
        this.is_closed = is_closed;
    }

    public boolean isCan_access_closed() {
        return can_access_closed;
    }

    public void setCan_access_closed(boolean can_access_closed) {
        this.can_access_closed = can_access_closed;
    }
}
