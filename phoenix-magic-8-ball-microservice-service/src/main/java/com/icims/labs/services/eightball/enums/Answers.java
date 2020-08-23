package com.icims.labs.services.eightball.enums;


public enum Answers {

    CERTAIN_ANSWER(1,"certain_answer"),
    DECIDED_ANSWER(2,"decided_answer"),
    NO_DOUBT_ANSWER(3,"no_doubt_answer"),
    DEFINITE_ANSWER(4, "definite_answer"),
    RELY_ANSWER(5, "rely_answer"),
    SEE_YES_ANSWER(6,"see_yes_answer"),
    LIKELY_ANSWER(7,"likely_answer"),
    OUTLOOK_GOOD_ANSWER(8,"outlook_good_answer"),
    YES_ANSWER(9, "yes_answer"),
    SIGNS_YES_ANSWER(10, "signs_yes_answer"),
    HAZY_ANSWER(11, "hazy_answer"),
    LATER_ANSWER(12, "later_answer"),
    NOT_TELL_ANSWER(13, "not_tell_answer"),
    NOT_PREDICT_ANSWER(14, "not_predict_answer"),
    CONCENTRATE_ANSWER(15, "concentrate_answer"),
    COUNT_ANSWER(16, "count_answer"),
    REPLY_NO_ANSWER(17, "reply_no_answer"),
    SOURCES_NO_ANSWER(18, "sources_no_answer"),
    OUTLOOK_BAD_ANSWER(19, "outlook_bad_answer"),
    DOUBTFUL_ANSWER(20,"doubtful_answer"),
    TRY_LATER_ANSWER(21,"try_later");
	
    private int answerId;
    private String answerKey;

    public int getAnswerId() {
        return answerId;
    }

    public String getAnswerKey() {
        return answerKey;
    }
    
    public static String getAnswerByValue(final Integer value)
    {
    	String responseAnswer=null;
    	String response="try_later";
    	if(value != null)
    	{
    		switch(value)
    		{
    		case 1:
    			responseAnswer = response;
    			break;
    		default :
    			responseAnswer = response;
    		}
    	}
    	else
    	{
    		responseAnswer = "try_later";
    	}
    	return responseAnswer;
    }
    Answers(int answerId, String answerKey){
     this.answerId = answerId;
     this.answerKey = answerKey;
    }

}
