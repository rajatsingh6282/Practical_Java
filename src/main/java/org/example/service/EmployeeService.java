package org.example.service;

import org.example.model.Employee;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class EmployeeService {
    private final List<Employee> employees = new ArrayList<>();

    public EmployeeService() {
        // Adding some initial data
        employees.add(new Employee(101, "Alice Johnson", "Software Engineer", 75000, "IT", "2022-06-15"));
        employees.add(new Employee(102, "Bob Smith", "HR Manager", 85000, "HR", "2021-03-22"));
        employees.add(new Employee(103, "Charlie Davis", "Project Manager", 95000, "Management", "2019-11-01"));
        employees.add(new Employee(104, "Diana Martinez", "QA Analyst", 60000, "Quality Assurance", "2023-01-10"));
        employees.add(new Employee(105, "Edward Brown", "Data Scientist", 120000, "Data Science", "2020-08-05"));
        employees.add(new Employee(106, "Rajat Singh", "Data Scientist", 120000, "Data Science", "2020-08-05"));
    }

    public ByteArrayInputStream generateCsvAndZipGroupedByDoj() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            // Group employees by their DOJ
            Map<String, List<Employee>> employeesByDoj = employees.stream()
                    .collect(Collectors.groupingBy(Employee::getDateOfJoining));

            for (Map.Entry<String, List<Employee>> entry : employeesByDoj.entrySet()) {
                String doj = entry.getKey();
                List<Employee> employeesForDoj = entry.getValue();

                String fileName = "employees_" + doj + ".csv";
                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOutputStream.putNextEntry(zipEntry);

                String csvContent = createCsvContent(employeesForDoj);
                zipOutputStream.write(csvContent.getBytes());
                zipOutputStream.closeEntry();
            }
        }

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private String createCsvContent(List<Employee> employees) {
        StringBuilder builder = new StringBuilder();
        builder.append("EmpId,EmpName,Designation,Salary,Department,DateOfJoining\n");

        for (Employee employee : employees) {
            builder.append(employee.getEmpId()).append(",")
                    .append(employee.getEmpName()).append(",")
                    .append(employee.getDesignation()).append(",")
                    .append(employee.getSalary()).append(",")
                    .append(employee.getDepartment()).append(",")
                    .append(employee.getDateOfJoining()).append("\n");
        }

        return builder.toString();
    }
}
