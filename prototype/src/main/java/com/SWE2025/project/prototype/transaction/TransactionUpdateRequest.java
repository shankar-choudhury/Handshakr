package com.SWE2025.project.prototype.transaction;

import java.util.List;

public record TransactionUpdateRequest(String transactionID, TransactionStatus transactionStatus) { }
