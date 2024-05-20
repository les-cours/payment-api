package com.arini.paiment.service;

import com.arini.paiment.model.AppResponse;
import com.arini.paiment.model.Student;
import com.arini.paiment.repository.AppRepository;
import com.arini.paiment.repository.ChargeRepository;

import java.util.Optional;
import java.util.UUID;

public class ChargeService {

    private final ChargeRepository chargeRepository;
    private final AppRepository appRepository;

    public ChargeService(ChargeRepository chargeRepository,AppRepository appRepository){
        this.chargeRepository = chargeRepository;
        this.appRepository = appRepository;
    }





    //we must add transaction token to verify if student has paid (using api of chargily or something like that)
    public AppResponse chargeAccount(UUID studentID, float sold) {

        Optional<Student> studentOptional = appRepository.findStudentByID(studentID);

        if (!studentOptional.isPresent()){
            return new AppResponse(false,"student doesnt exist.");
        }

        var student = studentOptional.get();

        student.setAmount(student.getAmount() + sold);

       var err =  appRepository.updateStudent(student);

        if (err != null){
            return new AppResponse(false,"couldn't update the mount now."+err.Message);
        }

        return new AppResponse(true,"account charged successfully.");
    }
}
