package com.arini.paiment.controller;


import com.arini.paiment.model.AppResponse;
import com.arini.paiment.service.ChargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pay")
public class ChargeController {

    private final ChargeService chargeService;;

    public ChargeController(ChargeService chargeService) {
        this.chargeService = chargeService;
    }


    @GetMapping("/{classRoomID}/student/{studentID}/month/{month}")
    public ResponseEntity<AppResponse> payClassRoom(@PathVariable UUID studentID, @PathVariable float amount) {

               return ResponseEntity.ok(chargeService.chargeAccount(studentID,amount));

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
