package com.arini.paiment;


import com.arini.paiment.api.Learning;
import com.arini.paiment.api.LearningServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class LearningClient{
    private final LearningServiceGrpc.LearningServiceBlockingStub blockingStub;
    public LearningClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("learning-service", 9003)
                .usePlaintext()
                .build();
        blockingStub = LearningServiceGrpc.newBlockingStub(channel);
    }

    public Boolean addStudentToChatRoom(String roomID,String userID) {
        Learning.IDRequest request =  Learning.IDRequest.newBuilder().setId(roomID).setUserID(userID).build();
        Learning.OperationStatus response = blockingStub.addStudentToChatRoom(request);
        return response.getSuccess();
    }
}
