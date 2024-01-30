package org.example.rest_api_maven.controller;

import org.example.rest_api_maven.dto.AbsenDTO;
import org.example.rest_api_maven.exception.MataKuliahNotFoundException;
import org.example.rest_api_maven.exception.UserNotFoundException;
import org.example.rest_api_maven.model.Absen;
import org.example.rest_api_maven.model.MataKuliah;
import org.example.rest_api_maven.model.User;
import org.example.rest_api_maven.repository.AbsenRepository;
import org.example.rest_api_maven.repository.MataKuliahRepository;
import org.example.rest_api_maven.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/absen")
public class AbsenController {

    private final AbsenRepository absenRepository;
    private final UserRepository userRepository;
    private final MataKuliahRepository mataKuliahRepository;

    @Autowired
    public AbsenController(AbsenRepository absenRepository, UserRepository userRepository, MataKuliahRepository mataKuliahRepository) {
        this.absenRepository = absenRepository;
        this.userRepository = userRepository;
        this.mataKuliahRepository = mataKuliahRepository;
    }

    @GetMapping
    public List<Absen> getAllAbsen() {
        return absenRepository.findAll();
    }

    @GetMapping("/{id}")
    public Absen getAbsenById(@PathVariable Long id) {
        return absenRepository.findById(id)
                .orElseThrow(() -> new AbsenNotFoundException("Absen not found with ID: " + id));
    }


//@PostMapping
//public ResponseEntity<Absen> createAbsen(@RequestBody AbsenDTO absenDTO) {
//    // Find the user by NIM
//    User user = userRepository.findByNim(absenDTO.getNim());
//    if (user != null) {
//        Absen absen = new Absen();
//        absen.setName(user.getName());
//        absen.setNim(user.getNim());
//        absen.setMatkul(absenDTO.getMatkul());
//        absen.setTimestamp(new Date());
//        return new ResponseEntity<>(absenRepository.save(absen), HttpStatus.CREATED);
//    } else {
//        // Handle case when NIM is not found
//        throw new UserNotFoundException("User not found with NIM: " + absenDTO.getNim());
//    }
//}
@PostMapping
public ResponseEntity<Absen> createAbsen(@RequestBody AbsenDTO absenDTO) {
    User user = userRepository.findByNim(absenDTO.getNim());
    if (user != null) {
        MataKuliah mataKuliah = mataKuliahRepository.findByName(absenDTO.getMatkul()).orElse(null);
        if (mataKuliah != null) {
            Absen absen = new Absen();
            absen.setName(user.getName());
            absen.setNim(user.getNim());
            absen.setMatkul(absenDTO.getMatkul());
            Date now = new Date();
            absen.setTimestamp(now);
            absen.setStatus(calculateStatus(now, mataKuliah));
            return new ResponseEntity<>(absenRepository.save(absen), HttpStatus.CREATED);
        } else {
            throw new MataKuliahNotFoundException("Mata Kuliah not found: " + absenDTO.getMatkul());
        }
    } else {
        throw new UserNotFoundException("User not found with NIM: " + absenDTO.getNim());
    }
}

    private String calculateStatus(Date timestamp, MataKuliah mataKuliah) {
        if (timestamp.before(Date.from(mataKuliah.getJamMulai().atZone(ZoneId.systemDefault()).toInstant()))) {
            return "Not Yet Start Class";
        } else if (timestamp.after(Date.from(mataKuliah.getJamSelesai().atZone(ZoneId.systemDefault()).toInstant()))) {
            return "Late";
        } else {
            return "Success";
        }
    }


    @PutMapping("/{id}")
    public Absen updateAbsen(@PathVariable Long id, @RequestBody Absen updatedAbsen) {
        return absenRepository.findById(id)
                .map(existingAbsen -> {
                    // Update the fields of existingAbsen with fields from updatedAbsen
                    existingAbsen.setNim(updatedAbsen.getNim());
                    existingAbsen.setMatkul(updatedAbsen.getMatkul());
                    // You can update other fields as needed
                    return absenRepository.save(existingAbsen);
                })
                .orElseThrow(() -> new AbsenNotFoundException("Absen not found with ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteAbsen(@PathVariable Long id) {
        Absen absen = absenRepository.findById(id)
                .orElseThrow(() -> new AbsenNotFoundException("Absen not found with ID: " + id));
        absenRepository.delete(absen);
    }
}