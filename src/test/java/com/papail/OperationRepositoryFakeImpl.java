package com.papail;

import com.papail.models.Operation;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

class OperationRepositoryFakeImpl implements IOperationRepository {
    private final LinkedList<Operation> operations = new LinkedList<>();

    @Override
    public BigDecimal getBalance(final String id) {
        try{
            return operations.getLast().getBalance();
        }
        catch (final Exception e) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public List<Operation> find(final String id) {
        return operations;
    }

    @Override
    public void add(final String accountId, final Operation operation) {
        if (operation.getOperationType().equals(Operation.OperationType.DEPOSIT)) {
            operations.add(Operation.createDeposit(operation.getDate(), operation.getAmount(), operation.getBalance()));
        }
        if (operation.getOperationType().equals(Operation.OperationType.WITHDRAW)){
            operations.add(Operation.createWithdraw(operation.getDate(), operation.getAmount(), operation.getBalance()));
        }
    }

    @Override
    public boolean doesAccountExist(final String accountId) {
        return true;
    }
}
