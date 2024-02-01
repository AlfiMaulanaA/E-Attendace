<template>
    <div>
        <h1>MataKuliah</h1>
        <div v-if="successAlert" class="alert alert-success" role="alert">
            {{ successAlert }}
        </div>

        <!-- MataKuliah List -->
        <div class="card mb-4">
            <div class="card-header">MataKuliah List</div>
            <div class="card-body">
                <div class="container-widget">

                    <div class="widget" v-for="matakuliah in matakuliahs" :key="matakuliah.id">
                        <div @click="openAbsenModal(matakuliah)" data-bs-toggle="modal" data-bs-target="#absenModal">
                            <div class="widget-header">
                                {{ matakuliah.name }}
                            </div>
                            <div class="widget-after-header">{{ matakuliah.dosen }}</div>
                            <div class="widget-body">{{ matakuliah.hari }}</div>
                            <div class="widget-body">{{ matakuliah.link }}</div>
                            <div class="widget-footer">
                                {{ formatTime(matakuliah.jamMulai) }} - {{ formatTime(matakuliah.jamSelesai) }}
                            </div>
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
                        <div v-if="successAlert" class="alert alert-success" role="alert">
                            {{ successAlert }}
                        </div>

                        <form @submit.prevent="submitAbsenForm">
                            <div class="mb-3">
                                <label for="matkul" class="form-label">Mata Kuliah</label>
                                <input type="text" class="form-control" v-model="absenForm.matkul" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="nim" class="form-label">NIM</label>
                                <input type="number" class="form-control" v-model="absenForm.nim" required>
                                <!-- Display Nim validation error message -->
                                <div v-if="!isNimValid" class="text-danger mt-2">
                                    NIM not found in the database.
                                </div>
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


const formatTime = (dateTime) => {
    if (!dateTime) return '';

    const time = new Date(dateTime);
    if (isNaN(time.getTime())) {
        return '';
    }

    // Format the time in HH:MM AM/PM format
    return time.toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
    });
};

const isNimValid = ref(true);

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
    absenForm.value.nim = null;
    isNimValid.value = true;
};

const successAlert = ref('');

const submitAbsenForm = async () => {
    try {
        const absenData = {
            nim: absenForm.value.nim,
            matkul: absenForm.value.matkul,
        };

        const response = await axios.post('http://localhost:8080/api/absen', absenData);
        successAlert.value = `Success to Absen. NIM: ${response.data.nim}, Name: ${response.data.name}, Time: ${new Date(response.data.timestamp).toLocaleString()}`;

        const matakuliah = matakuliahs.value.find(mk => mk.name === absenForm.value.matkul);
        if (matakuliah && matakuliah.link) {
            window.location.href = matakuliah.link; // Redirect to the matakuliah's link
        } else {
            console.error('Matakuliah link not found');
        }        

        fetchMataKuliahs();
        setTimeout(() => {
            successAlert.value = '';
        }, 3000);

    } catch (error) {
        if (error.response && error.response.status === 404) {
            isNimValid.value = false;
        } else {
            console.error('Error submitting absen:', error);
        }
    }
};



onMounted(() => {
    fetchMataKuliahs();
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
</style>