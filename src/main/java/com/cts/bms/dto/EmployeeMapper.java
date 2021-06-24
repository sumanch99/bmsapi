package com.cts.bms.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.cts.bms.model.Employee;

public class EmployeeMapper implements RowMapper<Employee> {

	@Override
	public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
		Employee emp = new Employee();
		emp.setEmpId(rs.getLong("emp_id"));
		emp.setAdhaarNo(rs.getLong("adhaar_no"));;
		emp.setPhoneNo(rs.getLong("phone_number"));
		return emp;
	}

}
