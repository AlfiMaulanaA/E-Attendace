package org.example.rest_api_maven.controller;

import org.example.rest_api_maven.model.MataKuliah;

import org.example.rest_api_maven.service.MataKuliahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/matakuliah")
public class MataKuliahController {

    private final MataKuliahService mataKuliahService;

    @Autowired
    public MataKuliahController (MataKuliahService mataKuliahService){
        this.mataKuliahService = mataKuliahService
        ;}

    @GetMapping
    public List<MataKuliah> getAllMataKuliah() {
        return mataKuliahService.getAllMataKuliah();
    }

    @GetMapping("/{id}")
    public MataKuliah getMataKuliahById(@PathVariable Long id) {
        return mataKuliahService.getMataKuliahById(id);
    }

    @PostMapping
    public MataKuliah createMataKuliah(@RequestBody MataKuliah mataKuliah) {
        return mataKuliahService.createMataKuliah(mataKuliah);
    }

    @PutMapping("/{id}")
    public MataKuliah updateMataKuliah(@PathVariable Long id, @RequestBody MataKuliah updatedMataKuliah) {
        return mataKuliahService.updateMataKuliah(id, updatedMataKuliah);
    }

    @DeleteMapping("/{id}")
    public void deleteMataKuliah(@PathVariable Long id) {
        mataKuliahService.deleteMataKuliah(id);
    }
}
