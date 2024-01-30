package org.example.rest_api_maven.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class MataKuliah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String link;
    private String dosen;
    private String hari;
    private LocalDateTime jamMulai;
    private LocalDateTime jamSelesai;

    // Default Constructor
    public MataKuliah() {
    }

    // Constructor with all parameters
    public MataKuliah(Long id, String name, String link, String dosen, String hari, LocalDateTime jamMulai, LocalDateTime jamSelesai) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.dosen = dosen;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getDosen() {
        return dosen;
    }

    public String getHari() {
        return hari;
    }

    public LocalDateTime getJamMulai() {
        return jamMulai;
    }

    public LocalDateTime getJamSelesai() {
        return jamSelesai;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDosen(String dosen) {
        this.dosen = dosen;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public void setJamMulai(LocalDateTime jamMulai) {
        this.jamMulai = jamMulai;
    }

    public void setJamSelesai(LocalDateTime jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

}