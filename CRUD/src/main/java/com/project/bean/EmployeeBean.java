package com.project.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import com.project.model.Employee;
import com.project.service.EmployeeService;

@Named 
@ConversationScoped
public class EmployeeBean implements Serializable{
	
    private static final long serialVersionUID = 1L;
    
    private boolean show = false;
    private boolean showTable = false;
    @Inject
    private EmployeeService employeeService;
    
    @Inject
    private Conversation conversation;
    
    
    private List<Employee> employees; 
        
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    private Employee employee;
    
    
    @PostConstruct
    public void init() {
        System.out.println("employee bean initiated");
//        employees = employeeService.getAll();
    }
    
    @PreDestroy
    public void destroy() {
        System.out.println("employee bean is destroyed");
    }
    
    public void beginConversation() {
    	if(conversation.isTransient()) {
    		conversation.begin();
            showTable = true;  
    	}
        System.out.println("Start bean");
    }
    
    public void destroyConversation() {
    	if(!conversation.isTransient()) {
    		conversation.end();
    	}
        showTable = false; 
        System.out.println("End bean");
    }
    
    public void showForm(Employee emp) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().visitTree(VisitContext.createVisitContext(context), new VisitCallback() {
			@Override
			public VisitResult visit(VisitContext context1, UIComponent component) {
			    if (component instanceof EditableValueHolder) {
			        ((EditableValueHolder) component).resetValue();
			    }
			    return VisitResult.ACCEPT;
			}
		});

        if (emp != null) {
            this.employee = new Employee(emp.getCode(), emp.getName(), emp.getAge(), emp.getDob());
        } else {
            this.employee = new Employee();
        }
        this.show = true;
    }
    
    public void hiddenForm() {
    	this.show = false;
    }

    
    public List<Employee> getEmployees() {
    	employee = new Employee();
        if (employees == null) {
            employees = employeeService.getAll();
        }
        return employees;
    }
    
    public void save() {
    	employeeService.insert(employee);
        employees = null;
        employee = new Employee();
        this.show=false;
    }
    
    
    public void update() {
    	employeeService.update(employee);
        employees = null;
        employee = new Employee();
        this.show=false;
    }

    public void delete(Integer code) {
    	employeeService.delete(code);
        employees = null;
    }
    
    

	public boolean isShow() {
		return this.show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}
	
    public void validateName(FacesContext context, UIComponent component, Object value) {
    	
        if (value == null || value.toString().trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is required", null));
        }
        
        String name = value.toString();
        
        Pattern pattern = Pattern.compile("^[A-Za-z\\s]+$"); 
        
        if(!pattern.matcher(name).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name just contains characters and space", null));
        }
    }
    
	public void validateDob(FacesContext context, UIComponent component, Object value) {
		
        if (value == null || value.toString().trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date of Birth is required",null));
        }
        
        LocalDate dob = (LocalDate) value;
        LocalDate today = LocalDate.now();
        if (dob.isAfter(today)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date of Birth cannot be in the future", null));
        }
	}

	public boolean isShowTable() {
		return showTable;
	}

	public void setShowTable(boolean showTable) {
		this.showTable = showTable;
	}
}
