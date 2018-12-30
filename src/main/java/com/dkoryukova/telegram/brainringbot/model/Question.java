package com.dkoryukova.telegram.brainringbot.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Question {

    @XmlElement(name = "QuestionId")
    private Long id;

    @XmlElement(name = "Question")
    private String text;

    @XmlElement(name = "Answer")
    private String answer;

    @XmlElement(name = "Comments")
    private String comment;

    private boolean attempted = false;

}

