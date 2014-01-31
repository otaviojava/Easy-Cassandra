package org.easycassandra.bean.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
/**
 * the professional class.
 * @author otaviojava
 */
@Entity(name = "professional")
public class Professional {


    @Id
    private String name;

    @Column
    private Double salary;

    @Transient
    private Double salaryYear;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getSalaryYear() {
        return salaryYear;
    }

    public void setSalaryYear(Double salaryYear) {
        this.salaryYear = salaryYear;
    }
}
