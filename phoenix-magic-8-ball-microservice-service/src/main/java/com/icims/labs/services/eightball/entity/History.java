package com.icims.labs.services.eightball.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "magic_eight_ball_history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "question")
    private String question;

    @Column(name = "frequency")
    private int frequency;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

}
