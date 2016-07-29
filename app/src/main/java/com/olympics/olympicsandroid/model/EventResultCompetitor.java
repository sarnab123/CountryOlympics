package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
@Root(name = "competitor",strict = false)
public class EventResultCompetitor
{
    @Attribute(required = false)
    private String id;

    @Attribute(required = false)
    private String last_name;

    @Attribute(required = false)
    private String first_name;

    @Attribute(required = false)
    private String type;

    @Attribute(required = false)
    private String result_type;

    @Attribute(required = false)
    private String result;

    @Attribute(required = false)
    private String rank;

    @Attribute(required = false)
    private String description;

    @Attribute(required = false)
    private String organization;

    @Attribute(required = false)
    private String gender;

    @Attribute(required = false)
    private String print_name;

    public String getGender() {
        return gender;
    }

    public String getDescription() {
        return description;
    }

    public String getOrganization() {
        return organization;
    }

    @ElementList(name = "extended-results",required = false)
    List<ExtendedResult> extendedResults;

    @ElementList(required = false)
    List<ScoreModel> scoring;

    @Attribute(required = false)
    private String outcome;


    public String getOutcome() {
        return outcome;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    public List<ExtendedResult> getExtendedResults() {
        return extendedResults;
    }

    public List<ScoreModel> getScoring() {
        return scoring;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getRank() {
        return rank;
    }

    public String getResult_type() {
        return result_type;
    }

    public String getType() {
        return type;
    }

    public void setExtendedResults(List<ExtendedResult> extendedResults) {
        this.extendedResults = extendedResults;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setResult_type(String result_type) {
        this.result_type = result_type;
    }

    public void setScoring(List<ScoreModel> scoring) {
        this.scoring = scoring;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrint_name() {
        return print_name;
    }

    public void setPrint_name(String print_name) {
        this.print_name = print_name;
    }
}
