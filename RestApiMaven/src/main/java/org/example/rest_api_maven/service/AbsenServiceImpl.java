package org.example.rest_api_maven.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.rest_api_maven.model.Absen;
import org.example.rest_api_maven.model.MataKuliah;
import org.example.rest_api_maven.repository.MataKuliahRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AbsenServiceImpl implements AbsenService {

    @PersistenceContext
    private EntityManager entityManager;
    private MataKuliahRepository mataKuliahRepository;

    @Override
    public List<Absen> getAllAbsen() {
        return entityManager.createQuery("SELECT a FROM Absen a", Absen.class).getResultList();
    }

    @Override
    public Absen getAbsenById(Long id) {
        return entityManager.find(Absen.class, id);
    }

    @Override
    @Transactional
    public Absen createAbsen(Absen absen) {
        Long mataKuliahId;
        try {
            mataKuliahId = Long.parseLong(absen.getMatkul());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid MataKuliah ID format", e);
        }

        Optional<MataKuliah> mataKuliahOpt = mataKuliahRepository.findById(mataKuliahId);
        if (mataKuliahOpt.isPresent()) {
            MataKuliah mataKuliah = mataKuliahOpt.get();
            LocalDateTime jamMulai = mataKuliah.getJamMulai();
            LocalDateTime jamSelesai = mataKuliah.getJamSelesai();
            LocalDateTime absenTime = convertToLocalDateTimeViaInstant(absen.getTimestamp());

            if (absenTime.isBefore(jamMulai)) {
                absen.setStatus("Not Yet Start Class");
            } else if (absenTime.isAfter(jamSelesai)) {
                absen.setStatus("Late");
            } else {
                absen.setStatus("Success");
            }
        } else {
            throw new RuntimeException("MataKuliah not found with ID: " + mataKuliahId);
        }

        entityManager.persist(absen);
        return absen;
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }


    @Override
    @Transactional
    public Absen updateAbsen(Long id, Absen updatedAbsen) {
        Absen existingAbsen = entityManager.find(Absen.class, id);
        if (existingAbsen != null) {
            existingAbsen.setName(updatedAbsen.getName());
            existingAbsen.setNim(updatedAbsen.getNim());
            existingAbsen.setMatkul(updatedAbsen.getMatkul());
            existingAbsen.setStatus(updatedAbsen.getStatus());
            existingAbsen.setTimestamp(updatedAbsen.getTimestamp());
            return existingAbsen;
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteAbsen(Long id) {
        Absen absen = entityManager.find(Absen.class, id);
        if (absen != null) {
            entityManager.remove(absen);
        }
    }
}
