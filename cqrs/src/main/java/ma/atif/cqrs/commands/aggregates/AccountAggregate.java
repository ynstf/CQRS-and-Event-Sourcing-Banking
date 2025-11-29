package ma.atif.cqrs.commands.aggregates;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.atif.cqrs.commands.commands.AddAccountCommand;
import ma.atif.cqrs.commands.enums.AccountStatus;
import ma.atif.cqrs.events.AccountCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;



@Aggregate
@Slf4j
@Getter
@Setter
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId ;
    private double currentBalance;
    private String currency;
    private AccountStatus status;


    public AccountAggregate() {
        log.info("Account Aggregate Created");
    }

    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        log.info("CreateAccount Command Received");
        if (command.getInitialBalance()<0) throw  new IllegalArgumentException("Balance negative exception");
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                command.getCurrency(),
                AccountStatus.CREATED
        ));

    }
    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        log.info("AccountCreatedEvent occured");
        this.accountId =event.getAccountId();
        this.currentBalance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = event.getStatus();
    }
}
