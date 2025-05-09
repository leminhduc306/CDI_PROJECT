package com.project.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.project.model.Employee;

@Named
@ConversationScoped
public class EmployeeService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
    private EmployeeDAO employeeDAO;
    
    public List<Employee> getAll() {
        return employeeDAO.getAll();
    }
    public Employee getById(int code) {
        return employeeDAO.getById(code);
    }

    public void insert(Employee employee) {
        employeeDAO.insert(employee);
    }

    public void update(Employee employee) {
        employeeDAO.update(employee);
    }
    
    public void delete(int code) {
        employeeDAO.delete(code);
    }
}
