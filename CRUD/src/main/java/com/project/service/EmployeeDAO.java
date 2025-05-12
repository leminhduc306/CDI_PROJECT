package com.project.service;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import com.project.model.Employee;

import java.io.Serializable;
import java.sql.*;
import java.time.*;

@Named
@ConversationScoped
public class EmployeeDAO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection connection;

    @PostConstruct
    private void getConnection() {
        System.out.println("Set up DB");
        try {
            String db_url = "jdbc:postgresql://localhost:5432/Employee",
                    db_userName = "postgres",
                    db_password = "Taskhub1";
            connection = DriverManager.getConnection(db_url, db_userName, db_password);
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    @PreDestroy
    public void cleanUp() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("End DB");
    }

    
	public List<Employee> getAll(){
        System.out.println("get Employee");
        List<Employee> list = new ArrayList<>();
        
        
		String sql = "SELECT * FROM \"Mt_employee\" Order By employee_code";
		try(
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(sql);)
		{
			while(rs.next()) {
				int code = rs.getInt("employee_code");
                String name = rs.getString("employee_name");
                int age = rs.getInt("employee_age");
                LocalDate dob = rs.getDate("date_of_birth").toLocalDate();

                Employee emp = new Employee(code, name, age, dob);
                list.add(emp);
			}	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
		public Employee getById(int code) {
		    String sql = "SELECT * FROM \"Mt_employee\" WHERE employee_code = ?";
		    Employee emp = null;
	
		    try (
		         PreparedStatement stmt = connection.prepareStatement(sql)) {
		        stmt.setInt(1, code);
		        ResultSet rs = stmt.executeQuery();
	
		        if (rs.next()) {
		            String name = rs.getString("employee_name");
		            int age = rs.getInt("employee_age");
		            LocalDate dob = rs.getDate("date_of_birth").toLocalDate();
		            emp = new Employee(code, name, age, dob);
		        }
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		    }
	
		    return emp;
		}
	
		public void insert(Employee e) {
		    String sql = "INSERT INTO \"Mt_employee\" (employee_name, employee_age, date_of_birth) VALUES (?, ?, ?)";

		    try (
		         PreparedStatement stmt = connection.prepareStatement(sql)) {

		        int age = Period.between(e.getDob(), LocalDate.now()).getYears();

		        stmt.setString(1, e.getName());
		        stmt.setInt(2, age); 
		        stmt.setDate(3, java.sql.Date.valueOf(e.getDob()));

		        stmt.executeUpdate();
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		    }
		}
		
	   
		public void update(Employee e) {
		    String sql = "UPDATE \"Mt_employee\" SET employee_name=?, employee_age=?, date_of_birth=? WHERE employee_code=?";

		    try (
		         PreparedStatement stmt = connection.prepareStatement(sql)) {

		        int age = Period.between(e.getDob(), LocalDate.now()).getYears();

		        stmt.setString(1, e.getName());
		        stmt.setInt(2, age);
		        stmt.setDate(3, java.sql.Date.valueOf(e.getDob()));
		        stmt.setInt(4, e.getCode());
		        stmt.executeUpdate();

		    } catch (SQLException ex) {
		        ex.printStackTrace();
		    }
		}
	   
	    public void delete(int code) {
	        String sql = "DELETE FROM \"Mt_employee\" WHERE employee_code=?";

	        try (
	             PreparedStatement stmt = connection.prepareStatement(sql)) {
	            stmt.setInt(1, code);
	            stmt.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
}