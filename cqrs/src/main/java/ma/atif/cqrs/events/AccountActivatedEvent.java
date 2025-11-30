package ma.atif.cqrs.events;


import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.atif.cqrs.commands.enums.AccountStatus;

@Getter @AllArgsConstructor
public class AccountActivatedEvent {
    private String accountId;
    private AccountStatus status;


}
