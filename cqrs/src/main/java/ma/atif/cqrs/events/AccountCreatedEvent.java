package ma.atif.cqrs.events;


import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.atif.cqrs.commands.enums.AccountStatus;
import java.math.BigDecimal;

@Getter @AllArgsConstructor
public class AccountCreatedEvent {
    private String accountId;
    private double initialBalance;
    private final String currency;
    private final AccountStatus status;


}
