package com.papail;

import com.papail.models.Operation;

import java.math.BigDecimal;
import java.util.List;

public interface IOperationRepository {
    BigDecimal getBalance(String accountId);
    List<Operation> find(String accountId);
    void add(String accountId, Operation operation);
    boolean doesAccountExist(String accountId);
}
