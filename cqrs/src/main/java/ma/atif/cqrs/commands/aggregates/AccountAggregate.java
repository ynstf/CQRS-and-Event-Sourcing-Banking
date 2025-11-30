package ma.atif.cqrs.commands.aggregates;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.atif.cqrs.commands.commands.AddAccountCommand;
import ma.atif.cqrs.commands.commands.CreditAccountCommand;
import ma.atif.cqrs.commands.commands.DebitAccountCommand;
import ma.atif.cqrs.commands.commands.UpdateAccountStatusCommand;
import ma.atif.cqrs.commands.enums.AccountStatus;
import ma.atif.cqrs.events.*;
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


    public AccountAggregate() {}


    // Create & activated
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
        AggregateLifecycle.apply(new AccountActivatedEvent(
                command.getId(),
                AccountStatus.ACTIVATED
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
    @EventSourcingHandler
    public void on(AccountActivatedEvent event){
        log.info("AccountCreatedEvent occured");
        this.accountId =event.getAccountId();
        this.status = event.getStatus();
    }

    // credit amount
    @CommandHandler
    public void handleCommand(CreditAccountCommand command){
        log.info("CreditAccountCommand Command Received");
        log.info(String.valueOf(status));
        log.info(String.valueOf(status.equals(AccountStatus.ACTIVATED)));


        if (!status.equals(AccountStatus.ACTIVATED)) throw  new RuntimeException("This account can not be credited because of the account is not activated. The current status is "+status);
        if (command.getAmount()<0) throw  new IllegalArgumentException("Amount negative exception");

        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));

    }
    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        log.info("AccountCreatedEvent occured");
        this.accountId =event.getAccountId();
        this.currentBalance = this.currentBalance + event.getAmount();
    }

    // debit amount
    @CommandHandler
    public void handleCommand(DebitAccountCommand command){
        log.info("DebitAccountCommand Command Received");
        log.info(String.valueOf(status));
        log.info(String.valueOf(status.equals(AccountStatus.ACTIVATED)));


        if (!status.equals(AccountStatus.ACTIVATED)) throw  new RuntimeException("This account can not be credited because of the account is not activated. The current status is "+status);
        if (command.getAmount()<0) throw  new IllegalArgumentException("Amount negative exception");
        if (command.getAmount()>currentBalance) throw  new RuntimeException("Balance not sufficient");

        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));

    }
    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        log.info("AccountCreatedEvent occured");
        this.accountId =event.getAccountId();
        this.currentBalance = this.currentBalance - event.getAmount();
        log.info(String.valueOf(this.currentBalance));
    }



    // change status
    @CommandHandler
    public void handleCommand(UpdateAccountStatusCommand command){
        log.info("UpdateAccountStatusCommand Command Received");


        if (status == command.getStatus()) throw  new RuntimeException("This account"+command.getId()+" is already the "+status+ " state");


        AggregateLifecycle.apply(new AccountStatusUpdatedEvent(
                command.getId(),
                command.getStatus()
        ));

    }
    @EventSourcingHandler
    public void on(AccountStatusUpdatedEvent event){
        log.info("AccountCreatedEvent occured");
        this.accountId =event.getAccountId();
        this.status = event.getStatus();
    }


}
