package com.dkoryukova.telegram.brainringbot.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;

@Data
@XmlRootElement(name = "search")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuestionPack {
    @XmlElement(name = "question")
    private LinkedList<Question> questions;

    public LinkedList<Question> getQuestions() {
        return questions;
    }
}
