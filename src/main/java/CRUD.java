/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.List;

/**
 *
 * @param <T> 
 * @author otaviojava - otaviojava@java.net
 */
public interface CRUD<T> extends Serializable {

    public boolean insert(T bean);

    public boolean remove(T bean);
    
    public boolean removeFromRowKey(Object rowKey);

    public boolean update(T bean);

    public T retrieve(Object id);

    public List<T> listAll();

    public List<T> listByIndex(Object index);
}
