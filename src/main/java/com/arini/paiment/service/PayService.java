package com.arini.paiment.service;


import com.arini.paiment.model.ClassRoom;
import com.arini.paiment.model.PayResponse;
import com.arini.paiment.model.Student;
import com.arini.paiment.repository.PayRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PayService {

    private final PayRepository payRepository;

    public PayService(PayRepository payRepository) {
        this.payRepository = payRepository;
    }


    @Transactional
    public PayResponse payClassRoom(UUID classroom, UUID studentID ) {

        Student student;
        ClassRoom classRoom;
        var classRoomOption = getClassRoomById(classroom);
        if (classRoomOption.isPresent()){
             classRoom = classRoomOption.get();
        }
        else {
            return new PayResponse(false,"classroom not found");
        }
//        check if already purchased


       var  studentOption = getStudentByID(studentID);
        if (studentOption.isPresent()){
             student = studentOption.get();
        }
        else {
            return new PayResponse(false,"student not found");
        }

        var studentAmount = student.getAmount();
        var classRoomPrice = classRoom.getPrice();

        if (studentAmount < classRoomPrice) {
            return new PayResponse(false,"amount not enough");
        }

        studentAmount -= classRoomPrice;
        student.setAmount(studentAmount);

        var err = payRepository.UpdateStudent(student);
        if (err != null) {
            return new PayResponse(false,"update student failed : " + err.Message);
        }

        //add subscription.

        return new PayResponse(true,"new amount :" + student.getAmount());
    }

    public PayResponse chargeAccount(UUID studentID, float amount){
        return new PayResponse(true,"charge account successful");
    }



    private Optional<ClassRoom> getClassRoomById(UUID classRoomId) {
        return payRepository.findClassRoomByID(classRoomId);
    }

    private Optional<Student> getStudentByID(UUID studentID) {
        return payRepository.findStudentByID(studentID);
    }


//    private short searchForDiscount(String discountCode, ClassRoom classroom) {
//        payRepository.SearchForDiscount(discountCode,classroom);
//        return 99;
//    }
}

