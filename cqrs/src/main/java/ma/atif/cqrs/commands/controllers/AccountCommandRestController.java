package ma.atif.cqrs.commands.controllers;


import ma.atif.cqrs.commands.commands.AddAccountCommand;
import ma.atif.cqrs.commands.commands.CreditAccountCommand;
import ma.atif.cqrs.commands.commands.DebitAccountCommand;
import ma.atif.cqrs.commands.dto.AddAccountRequestDTO;
import ma.atif.cqrs.commands.dto.CreditAccountDTO;
import ma.atif.cqrs.commands.dto.DebitAccountDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commands/accounts")
public class AccountCommandRestController {
    private CommandGateway commandGateway;
    private EventStore eventStore;


    public AccountCommandRestController(CommandGateway commandGateway,
                                        EventStore eventStore) {
        this.commandGateway = commandGateway;
        this.eventStore = eventStore;   // ✔ injected correctly
    }

    @PostMapping(path = "/create")
    public CompletableFuture<String> AddNewAccount(@RequestBody AddAccountRequestDTO request){
        CompletableFuture<String> response = commandGateway.send(new AddAccountCommand(
                UUID.randomUUID().toString(),
                request.getInitialBalance(),
                request.getCurrency()
        ));
        return response;
    }

    @GetMapping("/events/{accountId}")
    public Stream eventStore(@PathVariable String accountId){
        return eventStore.readEvents(accountId).asStream();
    }

    @PostMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountDTO request){
        CompletableFuture<String> result = this.commandGateway.send(new CreditAccountCommand(
                request.accountId(),
                request.amount(),
                request.Currency()
        ));
        return result;
    }

    @PostMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountDTO request){
        CompletableFuture<String> result = this.commandGateway.send(new DebitAccountCommand(
                request.accountId(),
                request.amount(),
                request.Currency()
        ));
        return result;
    }


    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception exception){
        return exception.getMessage();
    }
}
