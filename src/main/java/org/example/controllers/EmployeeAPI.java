package org.example.controllers;

import org.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/employees")
public class EmployeeAPI {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportEmployeesAsZip() throws IOException {
        ByteArrayInputStream zipStream = employeeService.generateCsvAndZipGroupedByDoj();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=employees_by_doj.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(zipStream));
    }
}
