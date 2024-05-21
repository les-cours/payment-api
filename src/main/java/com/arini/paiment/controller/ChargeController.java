package com.arini.paiment.controller;


import com.arini.paiment.model.AppResponse;
import com.arini.paiment.model.ChargeAccountRequest;
import com.arini.paiment.model.GeneratePaymentCodeRequest;
import com.arini.paiment.service.ChargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class ChargeController {

    private final ChargeService chargeService;;

    public ChargeController(ChargeService chargeService) {
        this.chargeService = chargeService;
    }


    @PostMapping("/charge-account")
    public ResponseEntity<AppResponse> chargeAccount(@RequestBody ChargeAccountRequest req ) {

               return ResponseEntity.ok(chargeService.chargeAccountByPaymentCode(req.getStudentID(),req.getCode()));

    }

    @PostMapping("/generate-payment-code")
    public ResponseEntity<AppResponse> generatePaymentCode(@RequestBody GeneratePaymentCodeRequest req) {

        return ResponseEntity.ok(chargeService.generatePaymentCode(req.getAmount()));

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
