package org.example.rest_api_maven.controller;

import org.example.rest_api_maven.exception.UserNotFoundException;
import org.example.rest_api_maven.model.Absen;
import org.example.rest_api_maven.model.User;
import org.example.rest_api_maven.repository.AbsenRepository;
import org.example.rest_api_maven.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/absen")
public class AbsenController {

    private final AbsenRepository absenRepository;
    private final UserRepository userRepository;

    @Autowired
    public AbsenController(AbsenRepository absenRepository, UserRepository userRepository) {
        this.absenRepository = absenRepository;
        this.userRepository = userRepository;
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

    @PostMapping
    public Absen createAbsen(@RequestBody Absen absen) {
        // Find the user by NIM
        User user = userRepository.findByNim(absen.getNim());
        if (user != null) {
            absen.setName(user.getName());
            absen.setTimestamp(new Date());
            return absenRepository.save(absen);
        } else {
            // Handle case when NIM is not found
            throw new UserNotFoundException("User not found with NIM: " + absen.getNim());
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