package com.zkxltech.domain;

import java.util.List;

/**
 * 作答记录
 * @author zkxl
 *
 */
public class Record {
	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 班级id
	 */
	private String classId;
	/**
	 * 科目
	 */
	private String subject;
	/**
	 * 课时表对应的主键
	 */
	private String classHourId;
	/**
	 * 试卷id
	 */
	private String testId;
	/**
	 * 试卷名称
	 */
	private String testName;
	/**
	 * 题目id
	 */
	private String questionId;
	/**
	 * 题目名称
	 */
	private String question;
	/**
	 * 题目类型
	 */
	private String questionType;
	/**
	 * 学生id
	 */
	private String studentId;
	/**
	 * 学生名称
	 */
	private String studentName;
	/**
	 * 正确答案
	 */
	private String trueAnswer;
	/**
	 * 分数
	 */
	private String score;
	/**
	 * 作答答案
	 */
	private String answer;
	/**
	 * 作答结果1正确，2错误
	 */
	private String result;
	/**
	 * 主观题是否已上传
	 * 0：未上传,1：
	 */
	private String isSubjectiveUpload;
	/**
	 * 客观题是否已上传
	 * 0：未上传,1：
	 */
	private String isObjectiveUpload;
	private String time ;
	
	private List<String> studentIds;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getClassHourId() {
		return classHourId;
	}
	public void setClassHourId(String classHourId) {
		this.classHourId = classHourId;
	}
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getTrueAnswer() {
		return trueAnswer;
	}
	public void setTrueAnswer(String trueAnswer) {
		this.trueAnswer = trueAnswer;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
    public String getTestName() {
        return testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    
    public List<String> getStudentIds() {
        return studentIds;
    }
    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }
    

	
	public String getIsSubjectiveUpload() {
		return isSubjectiveUpload;
	}
	public void setIsSubjectiveUpload(String isSubjectiveUpload) {
		this.isSubjectiveUpload = isSubjectiveUpload;
	}
	public String getIsObjectiveUpload() {
		return isObjectiveUpload;
	}
	public void setIsObjectiveUpload(String isObjectiveUpload) {
		this.isObjectiveUpload = isObjectiveUpload;
	}
	@Override
    public String toString() {
        return "Record [id=" + id + ", classId=" + classId + ", subject=" + subject + ", classHourId=" + classHourId
                + ", testId=" + testId + ", testName=" + testName + ", questionId=" + questionId + ", question="
                + question + ", questionType=" + questionType + ", studentId=" + studentId + ", studentName="
                + studentName + ", trueAnswer=" + trueAnswer + ", score=" + score + ", answer=" + answer + ", result="
                + result + ", time=" + time + "]";
    }
}
