package com.jhoysbou.TBot.storages;

import java.util.List;
import java.util.Set;

public interface IdStorage<T> {
    Set<T> getAllIds();

    void addId(T id);

    void addAll(List<T> ids);

}
