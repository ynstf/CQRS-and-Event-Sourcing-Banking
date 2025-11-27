package ma.atif.cqrs.commands.dto;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
@Data @Getter
public class AddAccountRequestDTO {
    private double initialBalance;
    private String currency;
}
