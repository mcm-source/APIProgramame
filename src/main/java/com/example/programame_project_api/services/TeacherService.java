package com.example.programame_project_api.services;

import com.example.programame_project_api.entities.*;
import com.example.programame_project_api.repositories.SponsorRepository;
import com.example.programame_project_api.repositories.TeacherRepository;
import com.example.programame_project_api.repositories.TeamRepository;
import com.example.programame_project_api.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private SponsorRepository sponsorRepository;


    public ResponseEntity listDataFromTeacher(String token) {

        try {

            Teacher teacher = teacherRepository.findByEmail(extractEmailFromToken(token));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(prepareDataForResponse(teacher.getListTeams()));


        } catch (Exception e) {
            return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }


    }


    public ResponseEntity listDataForOVerallTable() {

        try {

            List<Sponsor> listSponsor = sponsorRepository.findAll();
            List<ContainerOverallTable> containerOverallTable = new ArrayList<>();

            for (Sponsor sponsor : listSponsor) {

                if (sponsor.getSimpleDonation() != null) {
                    containerOverallTable.add(doContainerDataForSimpleDonation(sponsor));
                } else if (sponsor.getComplexDonation() != null) {
                    containerOverallTable.add(doContainerDataForComplexDonation(sponsor));
                }
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(containerOverallTable);

        } catch (Exception e) {
            return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }


    }


    private ContainerOverallTable doContainerDataForSimpleDonation(Sponsor sponsor) {

        SimpleDonation simpleDonation = sponsor.getSimpleDonation();
        simpleDonation.setSponsor(null);
        double totalAcount = simpleDonation.calculateAmount();

        return new ContainerOverallTable(
                sponsor.getTeam().getName(),
                sponsor.getTeam().getSchoolName(),
                sponsor.getName(),
                simpleDonation,
                totalAcount);


    }

    private ContainerOverallTable doContainerDataForComplexDonation(Sponsor sponsor) {


        ComplexDonation complexDonation = sponsor.getComplexDonation();
        complexDonation.setSponsor(null);
        double totalAcount = complexDonation.calculateAmount();

        return new ContainerOverallTable(
                sponsor.getTeam().getName(),
                sponsor.getTeam().getSchoolName(),
                sponsor.getName(),
                complexDonation,
                totalAcount);


    }


    private List prepareDataForResponse(List<Team> listData) {

        double subtotalAcount = 0;

        listData.forEach(team -> {
            team.setTeacher(null);
            team.getListSponsors().forEach(sponsor -> {
                sponsor.setTeam(null);
                if (sponsor.getSimpleDonation() != null) {
                    SimpleDonation simpleDonation = sponsor.getSimpleDonation();
                    simpleDonation.setTotalAcount(simpleDonation.calculateAmount());
                    team.setTotalAcount(team.getTotalAcount() + simpleDonation.getTotalAcount());
                    sponsor.getSimpleDonation().setSponsor(null);
                }
                if (sponsor.getComplexDonation() != null) {
                    ComplexDonation complexDonation = sponsor.getComplexDonation();
                    complexDonation.setTotalAcount(complexDonation.calculateAmount());
                    team.setTotalAcount(team.getTotalAcount() + complexDonation.getTotalAcount());
                    sponsor.getComplexDonation().setSponsor(null);
                }
            });

        });

        return listData;

    }


    private ResponseEntity createResponseEntity(HttpStatus status, String bodyMessage) {

        return ResponseEntity
                .status(status)
                .body(bodyMessage);


    }

    private String extractEmailFromToken(String token) {

        System.out.println(token);
        String dato = token;
        dato = dato.replace("Bearer ", "");
        System.out.println(dato);
        return jwtUtil.extractUsername(dato);


    }
}
