package ma.atif.cqrs.commands.dto;

public record DebitAccountDTO(String accountId, double amount, String Currency) {
}
