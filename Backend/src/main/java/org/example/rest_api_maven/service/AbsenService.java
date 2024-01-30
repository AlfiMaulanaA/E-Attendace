package org.example.rest_api_maven.service;

import org.example.rest_api_maven.model.Absen;

import java.util.List;

public interface AbsenService {

    List<Absen> getAllAbsen();

    Absen getAbsenById(Long id);

    Absen createAbsen(Absen absen);

    Absen updateAbsen(Long id, Absen updatedAbsen);

    void deleteAbsen(Long id);
}
