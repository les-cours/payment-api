package com.arini.paiment.service;


import com.arini.paiment.model.ClassRoom;
import com.arini.paiment.model.Err;
import com.arini.paiment.model.PayResponse;
import com.arini.paiment.model.Student;
import com.arini.paiment.repository.PayRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PayService {

    private final PayRepository payRepository;

    public PayService(PayRepository payRepository) {
        this.payRepository = payRepository;
    }


    @Transactional
    public PayResponse payClassRoom(UUID classroomID, UUID studentID, String month) {

        Student student;
        ClassRoom classRoom;
        var classRoomOption = getClassRoomById(classroomID);
        if (classRoomOption.isPresent()){
             classRoom = classRoomOption.get();
        }
        else {
            return new PayResponse(false,"classroom not found");
        }


       var  studentOption = getStudentByID(studentID);
        if (studentOption.isPresent()){
             student = studentOption.get();
        }
        else {
            return new PayResponse(false,"student not found");
        }

        //        check if already purchased
        var checkPurchasedErr =  payRepository.CheckPurchased(studentID,classroomID,month);

        if (checkPurchasedErr != null){
            return new PayResponse(false,checkPurchasedErr.Message);
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
        var subscriptionErr = subscription(studentID,classroomID,month);

        if (subscriptionErr != null) {
            return new PayResponse(false,subscriptionErr.Message);
        }


        return new PayResponse(true,"new amount :" + student.getAmount());
    }

    private Err subscription(UUID studentID, UUID classroomID, String month) {

        //        check if already purchased
        Err err ;
         err =  payRepository.CheckPurchased(studentID,classroomID,month);
        if (err == null) {
            err = createSubscription( studentID,  classroomID,  month);
            return err;
        }

        err =  payRepository.payMonth(studentID, classroomID, month);

        return err;
    }

    private Err createSubscription(UUID studentID, UUID classroomID, String month) {
      return  payRepository.CreateSubscription(studentID, classroomID,month);

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

