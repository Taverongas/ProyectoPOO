package poo.tienda.dao.impl;

import java.util.List;

public interface CrudDAO<T, ID> {
    List<T> findAll();
    T findById(ID id);
    void save(T entity);
    void delete(ID id);
}