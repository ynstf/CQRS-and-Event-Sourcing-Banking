package ma.atif.cqrs.commands.aggregates;


import lombok.extern.slf4j.Slf4j;
import ma.atif.cqrs.commands.commands.AddAccountCommand;
import ma.atif.cqrs.commands.enums.AccountStatus;
import ma.atif.cqrs.events.AccountCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    //private String currency;
    private AccountStatus status;

    public AccountAggregate() {
    }
    @CommandHandler
    public AccountAggregate(AddAccountCommand command)
    {
        log.info("####################### CreateAccountCommand Received");
        if (command.getInitialBalance()<=0) throw new IllegalArgumentException("Initial balance must be positive");
        AggregateLifecycle.apply(
                new AccountCreatedEvent(
                        command.getId(),
                        command.getInitialBalance(),
                        command.getCurrency(),
                        AccountStatus.CREATED
                )
        );

    }
    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        log.info("################### AccountCreatedEvent Occured");
        this.accountId=event.getAccountId();
        this.balance=event.getInitialBalance();
        this.status=event.getStatus();
    }
}
