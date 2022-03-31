package com.binchoi.springboot.domain.race;

import com.binchoi.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class Race extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String raceName;

    @Column(columnDefinition = "TEXT")
    private String wager;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column
    private Long fstUserId;

    @Column(columnDefinition = "TEXT")
    private String fstUserHabit;

    @Column
    private Long sndUserId;

    @Column(columnDefinition = "TEXT")
    private String sndUserHabit;

    @Builder
    public Race(String raceName, String wager, LocalDate startDate, LocalDate endDate, Long fstUserId,
                String fstUserHabit, Long sndUserId, String sndUserHabit) {
        this.raceName = raceName;
        this.wager = wager;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fstUserId = fstUserId;
        this.fstUserHabit = fstUserHabit;
        this.sndUserId = sndUserId; //not provided in user scenario -> set as null
        this.sndUserHabit = sndUserHabit; //not provided in user scenario -> set as null
    }

    public void update(LocalDate endDate, Long sndUserId, String sndUserHabit) {
        this.endDate = endDate;
        this.sndUserId = sndUserId;
        this.sndUserHabit = sndUserHabit;
    }

}
