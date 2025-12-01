package ma.atif.cqrs.query.entities;

import jakarta.persistence.*;
import ma.atif.cqrs.commands.enums.OperationType;

import java.time.Instant;
import java.util.Date;

@Entity
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant date;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    @ManyToOne
    private Account account;
}
