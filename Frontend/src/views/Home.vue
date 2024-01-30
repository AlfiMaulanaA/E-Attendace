<template>
    <div>
        <h1>MataKuliah</h1>

        <!-- MataKuliah List -->
        <div class="card mb-4">
            <div class="card-header">MataKuliah List</div>
            <div class="card-body">
                <div class="container-widget">
                    <div class="widget" v-for="matakuliah in matakuliahs" :key="matakuliah.id">
                        <div class="widget-header" @click="openAbsenModal(matakuliah)" data-bs-toggle="modal"
                            data-bs-target="#absenModal">
                            {{ matakuliah.name }}
                        </div>
                        <div class="widget-after-header">{{ matakuliah.dosen }}</div>
                        <div class="widget-body">{{ matakuliah.hari }}</div>
                        <div class="widget-footer">
                            {{ matakuliah.jamMulai }} - {{ matakuliah.jamSelesai }}
                        </div>
                    </div>
                </div>
                <div class="mt-4">Klik Mata Kuliah untuk absen</div>
                <pre class="text-danger">
          Notes: Absen tidak bisa dilakukan di luar kelas!
        </pre>
            </div>
        </div>

        <!-- Bootstrap Modal -->
        <div class="modal fade" id="absenModal" tabindex="-1" aria-labelledby="absenModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="absenModalLabel">Absen Form</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="submitAbsenForm">
                            <div class="mb-3">
                                <label for="nim" class="form-label">NIM</label>
                                <input type="number" class="form-control" v-model="absenForm.nim" required>
                                <!-- Display Nim validation error message -->
                                <div v-if="!isNimValid" class="text-danger mt-2">
                                    NIM not found in the database.
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="name" class="form-label">Name</label>
                                <input type="text" class="form-control" v-model="absenForm.name" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="matkul" class="form-label">Mata Kuliah</label>
                                <input type="text" class="form-control" v-model="absenForm.matkul" readonly>
                            </div>
                            <button type="submit" class="btn btn-primary">Absen</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const matakuliahs = ref([]);
const absenForm = ref({
    nim: null,
    name: '',
    matkul: '',
});

const users = ref([]);
const isNimValid = ref(true); // Track Nim validation status

const fetchUsers = async () => {
    try {
        const response = await axios.get('http://localhost:8080/api/users');
        users.value = response.data;
    } catch (error) {
        console.error('Error fetching users:', error);
    }
};

const fetchMataKuliahs = async () => {
    try {
        const response = await axios.get('http://localhost:8080/api/matakuliah');
        matakuliahs.value = response.data;
    } catch (error) {
        console.error('Error fetching matakuliahs:', error);
    }
};

const openAbsenModal = (matakuliah) => {
    absenForm.value.matkul = matakuliah.name;
};

const submitAbsenForm = async () => {
    // Reset Nim validation status
    isNimValid.value = true;

    // Validate NIM against User table and submit Absen if valid
    try {
        const response = await axios.get(`http://localhost:8080/api/users/${absenForm.value.nim}`);
        const user = response.data;
        if (user) {
            // Submit Absen with user data and matkul
            const absenData = {
                name: user.name,
                nim: user.nim,
                matkul: absenForm.value.matkul,
                timestamp: new Date().toISOString(),
            };
            // Send the absenData to the backend API to save the Absen record
            await axios.post('http://localhost:8080/api/absen', absenData);
            // Close the modal and reset the form
            $('#absenModal').modal('hide'); // Close Bootstrap modal
            absenForm.value.nim = null; // Reset NIM
            // Optionally, you can update the list of Absen to reflect the new entry
        } else {
            // Handle case when NIM is not found
            isNimValid.value = false; // Set Nim validation status to false
        }
    } catch (error) {
        console.error('Error submitting absen:', error);
    }
};

onMounted(() => {
    fetchMataKuliahs();
    fetchUsers();
});
</script>


<style scoped>
.container-widget {
    gap: 10px;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
}

.widget {
    border-radius: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    padding: 10px;
    font-weight: 600;
    min-width: 180px;
}

/* Add your custom styles here */
</style>
