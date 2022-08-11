package com.tc.execution.data;

import org.springframework.data.domain.Page;

import java.util.List;

public interface DozerMapper {
    <T> T create(Class<T> target, Object source);

    <T> T create(Class<T> target, Object source, String mappingId);

    <T, S> List<T> createList(Class<T> target, Iterable<S> source);

    <T, S> List<T> createList(Class<T> target, Iterable<S> source, String mappingId);


    void transfer(Object source, Object target);
}
