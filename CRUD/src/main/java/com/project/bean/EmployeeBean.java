package com.project.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
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
public class EmployeeBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean show = false;
    private boolean showTable = false;
    @Inject
    private EmployeeService employeeService;
    @Inject
    private Conversation conversation;
    private List<Employee> employees;
    private Employee employee;

    @PostConstruct
    public void init() {
        employee = new Employee();
        System.out.println("Employee bean initiated");
    }
    @PreDestroy
    public void destroy() {
        System.out.println("Employee bean is destroyed");
    }

    public void beginConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
            showTable = true;
            employees = null; // Refresh employee list
        }
        System.out.println("Conversation started");
    }

    public void destroyConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
        showTable = false;
        show = false;
        employee = new Employee();
        employees = null;
        System.out.println("Conversation ended");
    }

    public void showForm(Employee emp) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().visitTree(VisitContext.createVisitContext(context), new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent component) {
                if (component instanceof EditableValueHolder) {
                    ((EditableValueHolder) component).resetValue();
                }
                return VisitResult.ACCEPT;
            }
        });

        employee = (emp != null) ? new Employee(emp.getCode(), emp.getName(), emp.getAge(), emp.getDob()) : new Employee();
        show = true;
        showTable = false; // Hide table when showing form
    }

    public void hiddenForm() {
        show = false;
        employee = new Employee();
        if (!conversation.isTransient()) {
            showTable = true; // Show table after canceling, if conversation is active
        }
    }

    public List<Employee> getEmployees() {
        if (employees == null && showTable) {
            employees = employeeService.getAll();
        }
        return employees;
    }

    public void save() {
        employeeService.insert(employee);
        employees = null; // Refresh list
        show = false;
        employee = new Employee();
        if (!conversation.isTransient()) {
            showTable = true; // Show table after saving, if conversation is active
        }
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Employee saved successfully", null));
    }

    public void update() {
        employeeService.update(employee);
        employees = null; // Refresh list
        show = false;
        employee = new Employee();
        if (!conversation.isTransient()) {
            showTable = true; // Show table after updating, if conversation is active
        }
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Employee updated successfully", null));
    }

    public void delete(Integer code) {
        employeeService.delete(code);
        employees = null; // Refresh list
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Employee deleted successfully", null));
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isShowTable() {
        return showTable;
    }

    public void setShowTable(boolean showTable) {
        this.showTable = showTable;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void validateName(FacesContext context, UIComponent component, Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is required", null));
        }
        String name = value.toString();
        Pattern pattern = Pattern.compile("^[A-Za-z\\s]+$");
        if (!pattern.matcher(name).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name must contain only letters and spaces", null));
        }
    }

    public void validateDob(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date of Birth is required", null));
        }
        if (!(value instanceof LocalDate)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Date of Birth format", null));
        }
        LocalDate dob = (LocalDate) value;
        LocalDate today = LocalDate.now();
        if (dob.isAfter(today)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date of Birth cannot be in the future", null));
        }
    }
}