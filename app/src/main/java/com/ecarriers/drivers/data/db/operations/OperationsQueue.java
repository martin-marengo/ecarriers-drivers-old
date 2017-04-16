package com.ecarriers.drivers.data.db.operations;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class OperationsQueue {

    void initialize(){
        this.operations = new ArrayList<>();
    }

    Operation createOperation(){
        return new Operation();
    }

    @SerializedName("pending_ops")
    private ArrayList<Operation> operations;

    public class Operation {

        @SerializedName("timestamp")
        private long timestamp;

        @SerializedName("operation_type")
        private int operationType;

        public long getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getOperationType() {
            return operationType;
        }
        public void setOperationType(int operationType) {
            this.operationType = operationType;
        }
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public static String serializeOperations(OperationsQueue queue){
        String operationsJson = "";
        if(queue != null){
            Gson gson = new Gson();
            operationsJson = gson.toJson(queue, OperationsQueue.class);
        }
        return operationsJson;
    }

    public static OperationsQueue deserializeOperations(String operationsJson){
        OperationsQueue queue = null;
        if(!operationsJson.isEmpty()){
            Gson gson = new Gson();
            queue = gson.fromJson(operationsJson, OperationsQueue.class);
            orderOperations(queue);
        }
        return queue;
    }

    public static void orderOperations(OperationsQueue queue){
        if(queue != null && queue.getOperations() != null && !queue.getOperations().isEmpty()) {
            Collections.sort(queue.getOperations(), new Comparator<Operation>() {
                @Override
                public int compare(Operation o1, Operation o2) {
                    Long l1 = o1.getTimestamp();
                    Long l2 = o2.getTimestamp();
                    return l1.compareTo(l2);
                }
            });
        }
    }

    public Operation getNextOperation(){
        Operation netxOp = null;
        if(this.getOperations() != null && !this.getOperations().isEmpty()) {
            netxOp = this.getOperations().get(0);
        }
        return netxOp;
    }

    public void removeOperation(long timestamp){
        if(this.getOperations() != null && !this.getOperations().isEmpty()) {
            try {
                Iterator<Operation> iterator = this.getOperations().iterator();
                while(iterator.hasNext()){
                    Operation op = iterator.next();
                    if(op.getTimestamp() == timestamp){
                        iterator.remove();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
