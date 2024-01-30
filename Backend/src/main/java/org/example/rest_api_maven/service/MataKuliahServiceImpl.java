package org.example.rest_api_maven.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.rest_api_maven.model.MataKuliah;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MataKuliahServiceImpl implements MataKuliahService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MataKuliah> getAllMataKuliah() {
        return entityManager.createQuery("SELECT m FROM MataKuliah m", MataKuliah.class).getResultList();
    }

    @Override
    public MataKuliah getMataKuliahById(Long id) {
        return entityManager.find(MataKuliah.class, id);
    }

    @Override
    @Transactional
    public MataKuliah createMataKuliah(MataKuliah mataKuliah) {
        entityManager.persist(mataKuliah);
        return mataKuliah;
    }

    @Override
    @Transactional
    public MataKuliah updateMataKuliah(Long id, MataKuliah updatedMataKuliah) {
        MataKuliah existingMataKuliah = entityManager.find(MataKuliah.class, id);
        if (existingMataKuliah != null) {
            existingMataKuliah.setName(updatedMataKuliah.getName());
            existingMataKuliah.setLink(updatedMataKuliah.getLink());
            existingMataKuliah.setDosen(updatedMataKuliah.getDosen());
            existingMataKuliah.setHari(updatedMataKuliah.getHari());
            existingMataKuliah.setJamMulai(updatedMataKuliah.getJamMulai());
            existingMataKuliah.setJamSelesai(updatedMataKuliah.getJamSelesai());
            return existingMataKuliah;
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteMataKuliah(Long id) {
        MataKuliah mataKuliah = entityManager.find(MataKuliah.class, id);
        if (mataKuliah != null) {
            entityManager.remove(mataKuliah);
        }
    }
}
