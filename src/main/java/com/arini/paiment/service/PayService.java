package com.arini.paiment.service;


import com.arini.paiment.model.AppResponse;
import com.arini.paiment.model.ClassRoom;
import com.arini.paiment.model.Err;
import com.arini.paiment.model.Student;
import com.arini.paiment.repository.AppRepository;
import com.arini.paiment.repository.PayRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PayService {

    private final PayRepository payRepository;
    private final AppRepository appRepository;

    public PayService(PayRepository payRepository,AppRepository appRepository) {
        this.payRepository = payRepository;
        this.appRepository = appRepository;
    }


    @Transactional
    public AppResponse payClassRoom(UUID classroomID, UUID studentID, int month) {

        Student student;
        ClassRoom classRoom;
        var classRoomOption = getClassRoomById(classroomID);
        if (classRoomOption.isPresent()){
             classRoom = classRoomOption.get();
        }
        else {
            return new AppResponse(false,"classroom not found");
        }


       var  studentOption = getStudentByID(studentID);
        if (studentOption.isPresent()){
             student = studentOption.get();
        }
        else {
            return new AppResponse(false,"student not found");
        }

        //        check if already purchased
        var checkPurchasedErr =  payRepository.CheckPurchased(studentID,classroomID,month);

        if (checkPurchasedErr != null){
            return new AppResponse(false,checkPurchasedErr.Message);
        }


        var studentAmount = student.getAmount();
        var classRoomPrice = classRoom.getPrice();

        if (studentAmount < classRoomPrice) {
            return new AppResponse(false,"amount not enough");
        }

        studentAmount -= classRoomPrice;
        student.setAmount(studentAmount);

        var err = appRepository.updateStudent(student);
        if (err != null) {
            return new AppResponse(false,"update student failed : " + err.Message);
        }

        //add subscription.
        var subscriptionErr = subscription(studentID,classroomID,month);

        if (subscriptionErr != null) {
            return new AppResponse(false,subscriptionErr.Message);
        }


        return new AppResponse(true,"new amount :" + student.getAmount());
    }


    private Err subscription(UUID studentID, UUID classroomID, int month) {

        //        check if already purchased
        Err err ;
         err =  payRepository.CheckPurchased(studentID,classroomID,month);
        if (err == null) {
            err = createSubscription( studentID,  classroomID,  month);
            return err;
        }

        err =  payRepository.CreateSubscription(studentID, classroomID, month);

        return err;
    }

    private Err createSubscription(UUID studentID, UUID classroomID, int month) {
      return  payRepository.CreateSubscription(studentID, classroomID,month);

    }

    private Optional<ClassRoom> getClassRoomById(UUID classRoomId) {
        return appRepository.findClassRoomByID(classRoomId);
    }

    private Optional<Student> getStudentByID(UUID studentID) {
        return appRepository.findStudentByID(studentID);
    }


    public AppResponse getAmount(UUID studentID) {
        Optional<Student> studentOptional = appRepository.findStudentByID(studentID);

        if (!studentOptional.isPresent()){
            return new AppResponse(false,"student doesnt exist.");
        }
        return new AppResponse(true,"",studentOptional.get().getAmount());
    }



//    private short searchForDiscount(String discountCode, ClassRoom classroom) {
//        payRepository.SearchForDiscount(discountCode,classroom);
//        return 99;
//    }
}

