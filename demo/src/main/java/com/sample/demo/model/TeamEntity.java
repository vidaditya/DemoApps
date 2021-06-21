package com.sample.demo.model;

import java.util.ArrayList;
import java.util.List;

public class TeamEntity {

    // private String id;

    private Team team;

    private List<Developer> developers = new ArrayList<>();

    public TeamEntity(Team team, List<Developer> developers) {
        this.team = team;
        // this.id = team.getId();
        this.developers = developers;
    }

/*
 * public String getId() { return id; } public void setId(String id) { this.id = id; }
 */

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }
}
