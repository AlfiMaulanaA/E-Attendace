package org.example.rest_api_maven.service;

import org.example.rest_api_maven.model.MataKuliah;

import java.util.List;

public interface MataKuliahService{
    List<MataKuliah> getAllMataKuliah();
    MataKuliah getMataKuliahById(Long id);

    MataKuliah createMataKuliah(MataKuliah mataKuliah);

    MataKuliah updateMataKuliah(Long id, MataKuliah updatedMataKuliah);

    void deleteMataKuliah(Long id);
}
