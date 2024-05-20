package com.arini.paiment.controller;


import com.arini.paiment.model.ClassRoom;
import com.arini.paiment.model.PayResponse;
import com.arini.paiment.service.PayService;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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

    @GetMapping("/{classRoomID}/student/{studentID}")
    public ResponseEntity<PayResponse> getClassroomById(@PathVariable UUID classRoomID, @PathVariable UUID studentID,String month) {
        System.out.println("id = " + classRoomID);

               return ResponseEntity.ok(payService.payClassRoom(classRoomID,studentID,month));

    }

}
