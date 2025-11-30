package ma.atif.cqrs.commands.dto;

import ma.atif.cqrs.commands.enums.AccountStatus;

public record UpdateAccountStatusDTO(
        String accountId,
        AccountStatus status
        ) {
}
