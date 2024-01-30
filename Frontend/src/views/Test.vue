<template>
    <div>
        <h1>MataKuliah</h1>

        <!-- MataKuliah List -->
        <div class="card mb-4">
            <div class="card-header">MataKuliah List</div>
            <div class="card-body">
                <div class="container-widget">
                    <div class="widget" v-for="(matakuliah, i) in matakuliahs" :key="matakuliah.id">
                        <div class="widget-header" @click="openAbsenModal(matakuliah.id)" style="cursor: pointer">
                            {{ matakuliah.name }}
                        </div>
                        <div class="widget-after-header">{{ matakuliah.dosen }}</div>
                        <div class="widget-body">{{ matakuliah.hari }}</div>
                        <div class="widget-footer">{{ matakuliah.jamMulai }} - {{ matakuliah.jamSelesai }}</div>
                    </div>
                </div>
                <div class="mt-4">Klik Mata Kuliah untuk absen</div>
                <pre class="text-danger">Notes: Absen tidak bisa dilakukan diluar kelas!</pre>
            </div>
        </div>

        <!-- Absen Modal -->
        <div class="modal fade" id="absenModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Absen</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" @click="closeAbsenModal"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="submitAbsen">
                            <div class="mb-3">
                                <label for="nim" class="form-label">NIM</label>
                                <input type="number" class="form-control" v-model="nimInput" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<!-- Rest of the code remains the same -->


<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const matakuliahs = ref([]);
const nimInput = ref('');
const selectedMatakuliahId = ref(null);

const fetchMataKuliahs = async () => {
    try {
        const response = await axios.get('http://localhost:8080/api/matakuliah');
        matakuliahs.value = response.data;
    } catch (error) {
        console.error('Error fetching matakuliahs:', error);
    }
};

const openAbsenModal = (matakuliahId) => {
    selectedMatakuliahId.value = matakuliahId;
    $('#absenModal').modal('show'); // Show the modal
};

const closeAbsenModal = () => {
    selectedMatakuliahId.value = null;
    nimInput.value = '';
    $('#absenModal').modal('hide'); // Hide the modal
};

const submitAbsen = async () => {
    try {
        const nim = parseInt(nimInput.value);
        if (!isNaN(nim)) {
            // Check if the Nim exists in the User table
            const response = await axios.get(`http://localhost:8080/api/users/${nim}`);
            if (response.data) {
                // If Nim exists, save the Absen entry
                const matakuliahId = selectedMatakuliahId.value;
                const timestamp = new Date().toISOString();
                await axios.post('http://localhost:8080/api/absen', {
                    name: response.data.name,
                    matkul: matakuliahId,
                    nim,
                    timestamp,
                });
                closeAbsenModal(); // Close the modal
                // You can also update the Absen list if needed
            } else {
                alert('NIM not found in the User table. Please enter a valid NIM.');
            }
        } else {
            alert('Please enter a valid NIM.');
        }
    } catch (error) {
        console.error('Error submitting absen:', error);
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
