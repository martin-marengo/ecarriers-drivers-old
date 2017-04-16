package com.ecarriers.drivers.data.db.operations;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ecarriers.drivers.data.preferences.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class OperationsDAO {

    private Context context;
    private static final String TIMESTAMP = "timestamp";
    private static final long INVALID_TIMESTAMP = -1;

    public OperationsDAO(Context context){
        this.context = context;
    }

    /**
     * MARK AS DRIVING
     * */

    public boolean enqueueMarkAsDrivingOp(@NonNull MarkAsDrivingOp op){
        boolean success = true;
        op.setSync(false);

        long timestamp = insertMarkAsDrivingOp(op);
        if(timestamp != INVALID_TIMESTAMP){

            MarkAsDrivingOp savedOperation = getMarkAsDrivingOp(timestamp);
            if(savedOperation != null){

                OperationsQueue queue;
                try{
                    // Get or initialize queue
                    String operationsJson = Preferences.getOperationsQueue(context);
                    if(!operationsJson.isEmpty()) {
                        queue = OperationsQueue.deserializeOperations(operationsJson);
                    }else{
                        queue = new OperationsQueue();
                        queue.initialize();
                    }

                    // Create new operation and enqueue it
                    OperationsQueue.Operation operation = queue.createOperation();
                    operation.setTimestamp(timestamp);
                    operation.setOperationType(MarkAsDrivingOp.OPERATION_TYPE);
                    queue.getOperations().add(operation);

                    // Save queue back to preferences
                    String newOperationsJson = OperationsQueue.serializeOperations(queue);
                    Preferences.setOperationsQueue(context, newOperationsJson);

                } catch(Exception e) {
                    e.printStackTrace();
                    success = false;
                }

            }else{
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private long insertMarkAsDrivingOp(@NonNull final MarkAsDrivingOp op){
        final long timestamp = generateTimestamp();

        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    op.setTimestamp(timestamp);
                    realm.copyToRealmOrUpdate(op);
                }
            });

            return timestamp;
        } catch (Exception e) {
            e.printStackTrace();
            return INVALID_TIMESTAMP;
        }
    }

    public MarkAsDrivingOp getMarkAsDrivingOp(long timestamp){
        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        MarkAsDrivingOp op = null;
        try {
            op = realm.where(MarkAsDrivingOp.class).equalTo(TIMESTAMP, timestamp).findFirst();
        } catch(Exception e){
            e.printStackTrace();
        }

        return op;
    }

    /**
     * MARK AS FINISHED
     * */

    public boolean enqueueMarkAsFinishedOp(@NonNull MarkAsFinishedOp op){
        boolean success = true;
        op.setSync(false);

        long timestamp = insertMarkAsFinishedOp(op);
        if(timestamp != INVALID_TIMESTAMP){

            MarkAsFinishedOp savedOperation = getMarkAsFinishedOp(timestamp);
            if(savedOperation != null){

                OperationsQueue queue;
                try{
                    // Get or initialize queue
                    String operationsJson = Preferences.getOperationsQueue(context);
                    if(!operationsJson.isEmpty()) {
                        queue = OperationsQueue.deserializeOperations(operationsJson);
                    }else{
                        queue = new OperationsQueue();
                        queue.initialize();
                    }

                    // Create new operation and enqueue it
                    OperationsQueue.Operation operation = queue.createOperation();
                    operation.setTimestamp(timestamp);
                    operation.setOperationType(MarkAsFinishedOp.OPERATION_TYPE);
                    queue.getOperations().add(operation);

                    // Save queue back to preferences
                    String newOperationsJson = OperationsQueue.serializeOperations(queue);
                    Preferences.setOperationsQueue(context, newOperationsJson);

                } catch(Exception e) {
                    e.printStackTrace();
                    success = false;
                }

            }else{
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private long insertMarkAsFinishedOp(@NonNull final MarkAsFinishedOp op){
        final long timestamp = generateTimestamp();

        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    op.setTimestamp(timestamp);
                    realm.copyToRealmOrUpdate(op);
                }
            });

            return timestamp;
        } catch (Exception e) {
            e.printStackTrace();
            return INVALID_TIMESTAMP;
        }
    }

    public MarkAsFinishedOp getMarkAsFinishedOp(long timestamp){
        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        MarkAsFinishedOp op = null;
        try {
            op = realm.where(MarkAsFinishedOp.class).equalTo(TIMESTAMP, timestamp).findFirst();
        } catch(Exception e){
            e.printStackTrace();
        }

        return op;
    }

    /**
     * MARK AS BEING SHIPPED
     * */

    public boolean enqueueMarkAsBeingShippedOp(@NonNull MarkAsBeingShippedOp op){
        boolean success = true;
        op.setSync(false);

        long timestamp = insertMarkAsBeingShippedOp(op);
        if(timestamp != INVALID_TIMESTAMP){

            MarkAsBeingShippedOp savedOperation = getMarkAsBeingShippedOp(timestamp);
            if(savedOperation != null){

                OperationsQueue queue;
                try{
                    // Get or initialize queue
                    String operationsJson = Preferences.getOperationsQueue(context);
                    if(!operationsJson.isEmpty()) {
                        queue = OperationsQueue.deserializeOperations(operationsJson);
                    }else{
                        queue = new OperationsQueue();
                        queue.initialize();
                    }

                    // Create new operation and enqueue it
                    OperationsQueue.Operation operation = queue.createOperation();
                    operation.setTimestamp(timestamp);
                    operation.setOperationType(MarkAsBeingShippedOp.OPERATION_TYPE);
                    queue.getOperations().add(operation);

                    // Save queue back to preferences
                    String newOperationsJson = OperationsQueue.serializeOperations(queue);
                    Preferences.setOperationsQueue(context, newOperationsJson);

                } catch(Exception e) {
                    e.printStackTrace();
                    success = false;
                }

            }else{
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private long insertMarkAsBeingShippedOp(@NonNull final MarkAsBeingShippedOp op){
        final long timestamp = generateTimestamp();

        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    op.setTimestamp(timestamp);
                    realm.copyToRealmOrUpdate(op);
                }
            });

            return timestamp;
        } catch (Exception e) {
            e.printStackTrace();
            return INVALID_TIMESTAMP;
        }
    }

    public MarkAsBeingShippedOp getMarkAsBeingShippedOp(long timestamp){
        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        MarkAsBeingShippedOp op = null;
        try {
            op = realm.where(MarkAsBeingShippedOp.class).equalTo(TIMESTAMP, timestamp).findFirst();
        } catch(Exception e){
            e.printStackTrace();
        }

        return op;
    }

    /**
     * MARK AS DELIVERED
     * */

    public boolean enqueueMarkAsDeliveredOp(@NonNull MarkAsDeliveredOp op){
        boolean success = true;
        op.setSync(false);

        long timestamp = insertMarkAsDeliveredOp(op);
        if(timestamp != INVALID_TIMESTAMP){

            MarkAsDeliveredOp savedOperation = getMarkAsDeliveredOp(timestamp);
            if(savedOperation != null){

                OperationsQueue queue;
                try{
                    // Get or initialize queue
                    String operationsJson = Preferences.getOperationsQueue(context);
                    if(!operationsJson.isEmpty()) {
                        queue = OperationsQueue.deserializeOperations(operationsJson);
                    }else{
                        queue = new OperationsQueue();
                        queue.initialize();
                    }

                    // Create new operation and enqueue it
                    OperationsQueue.Operation operation = queue.createOperation();
                    operation.setTimestamp(timestamp);
                    operation.setOperationType(MarkAsDeliveredOp.OPERATION_TYPE);
                    queue.getOperations().add(operation);

                    // Save queue back to preferences
                    String newOperationsJson = OperationsQueue.serializeOperations(queue);
                    Preferences.setOperationsQueue(context, newOperationsJson);

                } catch(Exception e) {
                    e.printStackTrace();
                    success = false;
                }

            }else{
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private long insertMarkAsDeliveredOp(@NonNull final MarkAsDeliveredOp op){
        final long timestamp = generateTimestamp();

        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    op.setTimestamp(timestamp);
                    realm.copyToRealmOrUpdate(op);
                }
            });

            return timestamp;
        } catch (Exception e) {
            e.printStackTrace();
            return INVALID_TIMESTAMP;
        }
    }

    public MarkAsDeliveredOp getMarkAsDeliveredOp(long timestamp){
        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        MarkAsDeliveredOp op = null;
        try {
            op = realm.where(MarkAsDeliveredOp.class).equalTo(TIMESTAMP, timestamp).findFirst();
        } catch(Exception e){
            e.printStackTrace();
        }

        return op;
    }

    /**
     * REPORT LOCATION
     * */

    public boolean enqueueReportLocationOp(@NonNull ReportLocationOp op){
        boolean success = true;
        op.setSync(false);

        long timestamp = insertReportLocationOp(op);
        if(timestamp != INVALID_TIMESTAMP){

            ReportLocationOp savedOperation = getReportLocationOp(timestamp);
            if(savedOperation != null){

                OperationsQueue queue;
                try{
                    // Get or initialize queue
                    String operationsJson = Preferences.getOperationsQueue(context);
                    if(!operationsJson.isEmpty()) {
                        queue = OperationsQueue.deserializeOperations(operationsJson);
                    }else{
                        queue = new OperationsQueue();
                        queue.initialize();
                    }

                    // Create new operation and enqueue it
                    OperationsQueue.Operation operation = queue.createOperation();
                    operation.setTimestamp(timestamp);
                    operation.setOperationType(ReportLocationOp.OPERATION_TYPE);
                    queue.getOperations().add(operation);

                    // Save queue back to preferences
                    String newOperationsJson = OperationsQueue.serializeOperations(queue);
                    Preferences.setOperationsQueue(context, newOperationsJson);

                } catch(Exception e) {
                    e.printStackTrace();
                    success = false;
                }

            }else{
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private long insertReportLocationOp(@NonNull final ReportLocationOp op){
        final long timestamp = generateTimestamp();

        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    op.setTimestamp(timestamp);
                    realm.copyToRealmOrUpdate(op);
                }
            });

            return timestamp;
        } catch (Exception e) {
            e.printStackTrace();
            return INVALID_TIMESTAMP;
        }
    }

    public ReportLocationOp getReportLocationOp(long timestamp){
        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        ReportLocationOp op = null;
        try {
            op = realm.where(ReportLocationOp.class).equalTo(TIMESTAMP, timestamp).findFirst();
        } catch(Exception e){
            e.printStackTrace();
        }

        return op;
    }

    /**
     * TIMESTAMP GENERATOR
     * */

    private long generateTimestamp(){
        return Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
    }
}
