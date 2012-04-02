/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.bean.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import org.easycassandra.annotations.ColumnFamilyValue;
import org.easycassandra.annotations.ColumnValue;
import org.easycassandra.annotations.KeyValue;
import org.easycassandra.annotations.TimeStampValue;

/**
 *
 * @author otavio
 */
@ColumnFamilyValue(name = "autocount")
public class Count implements Serializable {

    private static final long serialVersionUID = 3L;
    
    @KeyValue(auto=true)
    private BigInteger id;
    
  
    @ColumnValue(name = "name")
    private String name;
    
    @TimeStampValue
    private Long timeStampLong;

    @TimeStampValue
    private Date timeStampDate;
    
    @TimeStampValue
    private Calendar timeStampCalendar;
    
    
	public BigInteger getId() {
		return id;
	}


	public void setId(BigInteger id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Long getTimeStampLong() {
		return timeStampLong;
	}


	public void setTimeStampLong(Long timeStampLong) {
		this.timeStampLong = timeStampLong;
	}


	public Date getTimeStampDate() {
		return timeStampDate;
	}


	public void setTimeStampDate(Date timeStampDate) {
		this.timeStampDate = timeStampDate;
	}


	public Calendar getTimeStampCalendar() {
		return timeStampCalendar;
	}


	public void setTimeStampCalendar(Calendar timeStampCalendar) {
		this.timeStampCalendar = timeStampCalendar;
	}



	
    
    
}
