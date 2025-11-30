package ma.atif.cqrs.commands.dto;

import java.io.StringReader;

public record CreditAccountDTO(String accountId, double amount, String Currency) {
}
