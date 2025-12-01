package ma.atif.cqrs.query.dto;


import ma.atif.cqrs.query.entities.Account;
import ma.atif.cqrs.query.entities.AccountOperation;

import java.util.List;

public record AccountStatementResponseDTO(
        Account account, List<AccountOperation> operations
) {
}
