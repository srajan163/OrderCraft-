package com.sboot.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "rating_questions")
public class RatingQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_question_seq")
    @SequenceGenerator(
        name = "rating_question_seq",
        sequenceName = "RATING_QUESTION_SEQ",
        allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private String questionText;

    @Column(nullable = false)
    private Integer weight; // relative importance (e.g. 1–5)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
}
