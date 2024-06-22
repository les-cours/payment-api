package com.arini.paiment;

import com.arini.paiment.api.*;
import com.arini.paiment.model.Err;
import com.arini.paiment.service.ChargeService;
import com.arini.paiment.service.PayService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
public class PaymentServiceGRPC extends PaymentServiceGrpc.PaymentServiceImplBase{


	private final ChargeService chargeService;
	private final PayService payService;
	private final LearningClient learningClientService;

    public PaymentServiceGRPC(ChargeService chargeService, PayService payService,LearningClient learningClientService) {
        this.chargeService = chargeService;
		this.payService = payService;
		this.learningClientService = learningClientService;
    }


	@Override
	public void chargeAccount(ChargeAccountRequest request, StreamObserver<AppResponse> responseObserver) {

		UUID studentID = UUID.fromString(request.getStudentID());
		var res = chargeService.chargeAccountByPaymentCode(studentID,request.getCode());
		if (!res.isSuccess()) {
			System.err.println(res.getMessage());
			ErrInternal(responseObserver, res.getMessage());
		}


		var response  = AppResponse.newBuilder().setMessage(res.getMessage()).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void generatePaymentCode(GeneratePaymentCodeRequest request, StreamObserver<AppResponse> responseObserver) {

		var res =  chargeService.generatePaymentCode(request.getAmount());

		if (!res.isSuccess()) {
			System.err.println(res.getMessage());
			ErrInternal(responseObserver, res.getMessage());
		}


		var response = AppResponse.newBuilder().setMessage(res.getData().toString()).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void payClassRoom(PayClassRoomRequest request, StreamObserver<AppResponse> responseObserver) {


		var res = payService.payClassRoom(UUID.fromString(request.getClassRoomID()),UUID.fromString(request.getStudentID()),request.getMonth());


		if (!res.isSuccess()) {
			System.err.println(res.getMessage());
			ErrInternal(responseObserver, res.getMessage());
		}

		try {
			Boolean success = learningClientService.addStudentToChatRoom(request.getClassRoomID(), request.getStudentID());
			if (!success) {
				System.err.println("can't add student to chat room");
				ErrInternal(responseObserver, "can't add student to chat room");
			}

		}catch (Exception e){
			System.err.println("=================here===================== " +e.getMessage());
		}




		var response = AppResponse.newBuilder().setMessage(res.getMessage()).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void getAmount(GetAmountRequest request, StreamObserver<GetAmountResponse> responseObserver) {



		var res = payService.getAmount(UUID.fromString(request.getStudentID()));


		if (!res.isSuccess()) {
			System.err.println(res.getMessage());
			var status = Status.INTERNAL.withDescription(res.getMessage());
			responseObserver.onError(status.asException());

		}


		var response = GetAmountResponse.newBuilder().setAmount((Float) res.getData()).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}


	@Override
	public void getAmountByCode(GetAmountRequest request, StreamObserver<GetAmountResponse> responseObserver) {

		var res = payService.getAmountByCode(request.getStudentID());
		if (!res.isSuccess()) {
			System.err.println(res.getMessage());
			var status = Status.INTERNAL.withDescription(res.getMessage());
			responseObserver.onError(status.asException());

		}

		System.err.println(res.getData());

		var response = GetAmountResponse.newBuilder().setAmount((Float) res.getData()).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}


	private static void ErrInternal(StreamObserver<AppResponse> responseObserver,String message) {

		var status = Status.INTERNAL.withDescription(message);
		responseObserver.onError(status.asException());
	}
}