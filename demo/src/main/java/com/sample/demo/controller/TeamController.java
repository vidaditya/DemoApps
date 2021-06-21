package com.sample.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.demo.exception.NotificationRequestFailureException;
import com.sample.demo.exception.TeamNotFoundException;
import com.sample.demo.model.Developer;
import com.sample.demo.model.NotificationRequest;
import com.sample.demo.model.Team;
import com.sample.demo.model.TeamEntity;
import com.sample.demo.repo.DeveloperRepository;
import com.sample.demo.repo.TeamRepository;

@RestController
public class TeamController {

    private final TeamRepository teamRepository;

    private final DeveloperRepository developerRepository;

    String notificationURL = "https://run.mocky.io/v3/";

    public TeamController(TeamRepository teamRepository, DeveloperRepository developerRepository) {
        this.teamRepository = teamRepository;
        this.developerRepository = developerRepository;
    }

    @PostMapping("rest/v1/create-team")
    public void createTeam(@RequestBody TeamEntity entity) {
        Team team = entity.getTeam();
        String teamId = generateUUID();
        team.setId(teamId);
        // entity.setId(teamId);
        List<Developer> developers = entity.getDevelopers();
        for (Developer developer : developers) {
            String developerId = generateUUID();
            developer.setId(developerId);
            developer.setTeamId(teamId);
            developerRepository.save(developer);
        }
        teamRepository.save(team);
    }

    @GetMapping("rest/v1/get-teams")
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @PostMapping("rest/v1/receive-alert/{id}")
    public void receiveAlert(@PathVariable Iterable<String> id) throws ClientProtocolException, IOException {
        Optional<Team> optionalTeam = teamRepository.findById(id.toString());
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            List<Developer> developers = developerRepository.findAllById(id);
            TeamEntity entity = new TeamEntity(team, developers);
            int statusCode = sendNotification(entity);
            if (statusCode != 200) {
                throw new NotificationRequestFailureException(id.toString());
            }
        } else {
            throw new TeamNotFoundException(id.toString());
        }

    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        return uuidAsString;
    }

    private int sendNotification(TeamEntity entity) throws ClientProtocolException, IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = notificationURL + entity.getTeam().getId();
        HttpPost postRequest = new HttpPost(url);
        String requestBody = getRequestBody(entity);
        StringEntity input = new StringEntity(requestBody);
        postRequest.setEntity(input);
        HttpResponse response = httpClient.execute(postRequest);
        return response.getStatusLine().getStatusCode();
    }

    private String getRequestBody(TeamEntity entity) throws JsonProcessingException {
        Developer developer = getRandomDeveloper(entity.getDevelopers());
        NotificationRequest request = new NotificationRequest(developer.getPhoneNumber(), "Sending mail to " + developer.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(request);
    }

    private Developer getRandomDeveloper(List<Developer> developers) {
        int numberOfDevelopers = developers.size();
        Random rand = new Random();
        int index = rand.nextInt(numberOfDevelopers);
        return developers.get(index);
    }

}
