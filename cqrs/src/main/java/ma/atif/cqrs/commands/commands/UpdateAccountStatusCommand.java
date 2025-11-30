package ma.atif.cqrs.commands.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.atif.cqrs.commands.enums.AccountStatus;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@AllArgsConstructor
public class UpdateAccountStatusCommand {
    @TargetAggregateIdentifier
    private String id;
    private AccountStatus status;
}

