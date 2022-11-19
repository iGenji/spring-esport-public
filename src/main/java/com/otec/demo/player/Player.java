package com.otec.demo.player;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Player {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "student_sequence",
            strategy = GenerationType.SEQUENCE)
    private long id;
    @NotBlank
    @Column(nullable = false,unique = true)
    private String username;
    @NotBlank
    @Column(nullable = false,unique = true)
    private String email;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
   @Column(nullable = false)
    private int score;
   @Column(nullable = false)
   @PositiveOrZero
    private int wins;

    public Player(String username, String email, Gender gender, int score, int wins) {
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.score = score;
        this.wins = wins;
    }
}
