package ma.atif.cqrs.query.controllers;

import ma.atif.cqrs.query.dto.AccountStatementResponseDTO;
import ma.atif.cqrs.query.entities.Account;
import ma.atif.cqrs.query.entities.AccountOperation;
import ma.atif.cqrs.query.queries.GetAccountStatementQuery;
import ma.atif.cqrs.query.queries.GetAllAccounts;
import ma.atif.cqrs.query.queries.WatchEventQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/accounts")
@CrossOrigin("*")
public class AccountQueryController {
    private QueryGateway queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/all")
    public CompletableFuture<List<Account>> getAllAccounts(){
        CompletableFuture<List<Account>> result = queryGateway.query(
                new GetAllAccounts(),
                ResponseTypes.multipleInstancesOf(Account.class)
                );
        return result;
    }

    @GetMapping("/statement/{accountId}")
    public CompletableFuture<AccountStatementResponseDTO> getAccountStatement(@PathVariable String accountId){
        CompletableFuture<AccountStatementResponseDTO> result = queryGateway.query(
                new GetAccountStatementQuery(accountId),
                ResponseTypes.instanceOf(AccountStatementResponseDTO.class)
        );
        return result;
    }

    @GetMapping(value = "/watch/{accountId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountOperation> watch(@PathVariable String accountId){
        SubscriptionQueryResult<AccountOperation, AccountOperation> result = queryGateway.subscriptionQuery(
                new WatchEventQuery(accountId),
                ResponseTypes.instanceOf(AccountOperation.class),
                ResponseTypes.instanceOf(AccountOperation.class)
        );
        return result.initialResult().concatWith(result.updates());
    }
}
