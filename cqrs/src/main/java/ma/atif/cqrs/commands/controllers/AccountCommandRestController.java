package ma.atif.cqrs.commands.controllers;


import ma.atif.cqrs.commands.commands.AddAccountCommand;
import ma.atif.cqrs.commands.dto.AddAccountRequestDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/commands/accounts")
public class AccountCommandRestController {
    private CommandGateway commandGateway;
    public AccountCommandRestController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
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
}
