package com.arini.paiment.controller;


import com.arini.paiment.model.AppResponse;
import com.arini.paiment.service.PayService;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pay")
public class PayController {

    private final PayService payService;
    private final JdbcTemplate jdbcTemplate;

    public PayController(JdbcTemplate jdbcTemplate, PayService payService) {
        this.jdbcTemplate = jdbcTemplate;
        this.payService = payService;
    }

    @GetMapping("/test-connection")
    public String testConnection() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return "Connection successful!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Connection failed: " + e.getMessage();
        }
    }

    @GetMapping("/{classRoomID}/student/{studentID}/month/{month}")
    public ResponseEntity<AppResponse> payClassRoom(@PathVariable UUID classRoomID, @PathVariable UUID studentID, @PathVariable String month) {
        System.out.println("id = " + classRoomID);

               return ResponseEntity.ok(payService.payClassRoom(classRoomID,studentID,month));

    }


//    @PostMapping("/pay")
//    public ResponseEntity<AppResponse> payClassRoom(@RequestBody PayRequest payRequest) {
//        UUID classRoomID = payRequest.getClassRoomID();
//        UUID studentID = payRequest.getStudentID();
//        String month = payRequest.getMonth();
//
//        System.out.println("Classroom ID = " + classRoomID);
//        System.out.println("Student ID = " + studentID);
//        System.out.println("Month = " + month);
//
//        return ResponseEntity.ok(payService.payClassRoom(classRoomID, studentID, month));
//    }
}
