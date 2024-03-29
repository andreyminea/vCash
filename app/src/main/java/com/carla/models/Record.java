package com.carla.models;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class Record
{
    private SimpleUser sender;
    private int operationTypeInt;
    private OPERATION_TYPE opType;
    private SimpleUser receiver;
    private float amount;
    private Timestamp recordDate;
    final static private Map<Integer, OPERATION_TYPE> intTranslationMap = new HashMap<Integer, OPERATION_TYPE>()
    {{
        put(0, OPERATION_TYPE.TOP_UP);
        put(1, OPERATION_TYPE.WITHDRAW);
        put(2, OPERATION_TYPE.SEND);
        put(3, OPERATION_TYPE.RECEIVE);
    }};
    final static private Map<OPERATION_TYPE, Integer> enumTranslationMap = new HashMap<OPERATION_TYPE, Integer>()
    {{
        put(OPERATION_TYPE.TOP_UP, 0);
        put(OPERATION_TYPE.WITHDRAW, 1);
        put(OPERATION_TYPE.SEND, 2);
        put(OPERATION_TYPE.RECEIVE, 3);
    }};

    public SimpleUser getSender() {
        return sender;
    }

    public void setSender(SimpleUser sender) {
        this.sender = sender;
    }

    public SimpleUser getReceiver() {
        return receiver;
    }

    public void setReceiver(SimpleUser receiver) {
        this.receiver = receiver;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getOperationTypeInt() {
        return operationTypeInt;
    }

    public void setOperationTypeInt(int operationTypeInt) {
        this.operationTypeInt = operationTypeInt;
        this.opType = intTranslationMap.get(operationTypeInt);
    }

    public OPERATION_TYPE getTypeOfOperation()
    {
        return opType;
    }

    public void setOperationType(OPERATION_TYPE type)
    {
        opType = type;
        operationTypeInt = enumTranslationMap.get(type);
    }

    public Timestamp getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Timestamp recordDate) {
        this.recordDate = recordDate;
    }


    public enum OPERATION_TYPE
    {
        TOP_UP,
        WITHDRAW,
        SEND,
        RECEIVE
    }
}
