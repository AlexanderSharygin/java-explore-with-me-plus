package ru.practicum.ewm.stats.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Table(name = "hits")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "app", nullable = false)
    String app;

    @Column(name = "ip", nullable = false, length = 15)
    String ip;

    @Column(name = "uri", nullable = false)
    String uri;

    @Column(name = "timestamp", nullable = false, columnDefinition = "TIMESTAMP")
    LocalDateTime timestamp;
}