package org.example.rest_api_maven.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.rest_api_maven.model.Absen;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbsenServiceImpl implements AbsenService {

    @PersistenceContext
    private EntityManager entityManager;

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
        entityManager.persist(absen);
        return absen;
    }

    @Override
    @Transactional
    public Absen updateAbsen(Long id, Absen updatedAbsen) {
        Absen existingAbsen = entityManager.find(Absen.class, id);
        if (existingAbsen != null) {
            existingAbsen.setName(updatedAbsen.getName());
            existingAbsen.setNim(updatedAbsen.getNim());
            existingAbsen.setMatkul(updatedAbsen.getMatkul());
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
