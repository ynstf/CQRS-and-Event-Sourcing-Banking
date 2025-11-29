package ma.atif.cqrs.commands.controllers;


import ma.atif.cqrs.commands.commands.AddAccountCommand;
import ma.atif.cqrs.commands.dto.AddAccountRequestDTO;
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

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception exception){
        return exception.getMessage();
    }
    @GetMapping("/events/{accountId}")
    public Stream eventStore(@PathVariable String accountId){
        return eventStore.readEvents(accountId).asStream();
    }
}
