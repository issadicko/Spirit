/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager.outils;

import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author genius
 */
public abstract class AbstractManager<T> {
    
    private Connection connect;
    
    public AbstractManager(Connection connection){
        this.connect =  connection;
    }
    
    public Connection getConnection(){
        return connect;
    }
    
    public abstract int create(T object);
    public abstract ArrayList<T> findAll();
}
