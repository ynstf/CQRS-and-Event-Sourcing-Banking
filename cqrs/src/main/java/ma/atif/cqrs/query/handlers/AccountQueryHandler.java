package ma.atif.cqrs.query.handlers;

import lombok.extern.slf4j.Slf4j;
import ma.atif.cqrs.query.dto.AccountStatementResponseDTO;
import ma.atif.cqrs.query.entities.Account;
import ma.atif.cqrs.query.entities.AccountOperation;
import ma.atif.cqrs.query.queries.GetAccountStatementQuery;
import ma.atif.cqrs.query.queries.GetAllAccounts;
import ma.atif.cqrs.query.repository.AccountRepository;
import ma.atif.cqrs.query.repository.OperationRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccountQueryHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    public AccountQueryHandler(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }
    @QueryHandler
    public List<Account> on(GetAllAccounts query){
        return accountRepository.findAll();
    }

    @QueryHandler
    public AccountStatementResponseDTO on(GetAccountStatementQuery query){
        Account account = accountRepository.findById(query.getAccountId()).get();
        List<AccountOperation> operations = operationRepository.findByAccountId(query.getAccountId());
        return new AccountStatementResponseDTO(account, operations);
    }
}
