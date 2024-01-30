package org.example.rest_api_maven.model;

import jakarta.persistence.*;

import java.util.Date;


@Entity
public class Absen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String matkul;
    private Long nim;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Absen() {
    }

    public Absen(Long id, String name, String matkul, Long nim, Date timestamp) {
        this.id = id;
        this.name = name;
        this.matkul = matkul;
        this.nim = nim;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatkul() {
        return matkul;
    }

    public void setMatkul(String matkul) {
        this.matkul = matkul;
    }

    public Long getNim() {
        return nim;
    }

    public void setNim(Long nim) {
        this.nim = nim;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

