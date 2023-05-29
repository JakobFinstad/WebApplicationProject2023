package no.ntnu.idata2306.group6.entity.dto;


public class TestimonialDTO {
    private String statement;
    private int testimonialId;
    private String userImageSrc;
    private String userName;

    public String getUserName() {
        return this.userName;
    }

    public TestimonialDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getStatement() {
        return statement;
    }

    public TestimonialDTO setStatement(String statement) {
        this.statement = statement;
        return this;
    }

    public int getTestimonialId() {
        return testimonialId;
    }

    public TestimonialDTO setTestimonialId(int testimonialId) {
        this.testimonialId = testimonialId;
        return this;
    }

    public String getUserImageSrc() {
        return userImageSrc;
    }

    public TestimonialDTO setUserImageSrc(String userImageSrc) {
        this.userImageSrc = userImageSrc;
        return this;
    }
}
