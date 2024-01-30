package org.example.rest_api_maven.dto;


public class AbsenDTO {
    private Long nim;
    private String matkul;

    public AbsenDTO(){}

    public AbsenDTO(Long nim, String matkul) {
        this.nim = nim;
        this.matkul = matkul;
    }

    public Long getNim() {
        return nim;
    }

    public void setNim(Long nim) {
        this.nim = nim;
    }

    public String getMatkul() {
        return matkul;
    }

    public void setMatkul(String matkul) {
        this.matkul = matkul;
    }
}

