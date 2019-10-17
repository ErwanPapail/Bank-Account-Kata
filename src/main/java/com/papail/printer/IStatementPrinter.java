package com.papail.printer;

import com.papail.models.Operation;

import java.util.List;

public interface IStatementPrinter {
    void printStatement(List<Operation> operations);
}
