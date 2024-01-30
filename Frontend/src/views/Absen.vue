<template>
    <div>
        <h1>Absen Management</h1>

        <!-- Absen List -->
        <div class="card mb-4">
            <div class="card-header">Absen List</div>
            <div class="card-body">
                <button class="btn btn-success" @click="exportToExcel">Export to Excel</button>
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>NIM</th>
                            <th>Timestamp</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(absen, i) in absenList" :key="absen.id">
                            <td>{{ i + 1 }}</td>
                            <td>{{ absen.name }}</td>
                            <td>{{ absen.nim }}</td>
                            <td>{{ absen.timestamp }}</td>
                            <td>
                                <button class="btn btn-sm btn-primary" @click="editAbsen(absen.id)">Edit</button>
                                <button class="btn btn-sm btn-danger" @click="deleteAbsen(absen.id)">Delete</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Absen Form (Create or Edit) -->
        <div class="card">
            <div class="card-header">{{ isEditing ? 'Edit Absen' : 'Create Absen' }}</div>
            <div class="card-body">
                <form @submit.prevent="isEditing ? updateAbsen() : createAbsen()">
                    <div class="mb-3">
                        <label for="name" class="form-label">Name</label>
                        <input type="text" class="form-control" v-model="formData.name" required>
                    </div>
                    <div class="mb-3">
                        <label for="nim" class="form-label">NIM</label>
                        <input type="number" class="form-control" v-model="formData.nim" required>
                    </div>
                    <div class="mb-3">
                        <label for="timestamp" class="form-label">Timestamp</label>
                        <input type="datetime-local" class="form-control" v-model="formData.timestamp" required>
                    </div>
                    <button type="submit" class="btn btn-primary">{{ isEditing ? 'Update' : 'Create' }}</button>
                </form>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

import axios from 'axios';

const exportToExcel = () => {
    import('xlsx').then((XLSX) => {
        const wsData = absenList.value.map((absen) => ({
            Name: absen.name,
            NIM: absen.nim,
            Timestamp: absen.timestamp,
        }));

        const ws = XLSX.utils.json_to_sheet(wsData);
        const wb = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, 'Absen Data');
        const excelBuffer = XLSX.write(wb, { bookType: 'xlsx', type: 'array' });

        saveAsExcelFile(excelBuffer, 'absen_data.xlsx');
    });
};

const saveAsExcelFile = (buffer, fileName) => {
    const data = new Blob([buffer], { type: 'application/octet-stream' });
    const url = window.URL.createObjectURL(data);
    const a = document.createElement('a');
    a.style.display = 'none';
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
};

const absenList = ref([]);
const formData = ref({
    name: '',
    nim: null,
    timestamp: ''
});
const isEditing = ref(false);

const fetchAbsenList = async () => {
    try {
        const response = await axios.get('http://localhost:8080/api/absen');
        absenList.value = response.data;
    } catch (error) {
        console.error('Error fetching absen:', error);
    }
};

const createAbsen = async () => {
    try {
        await axios.post('http://localhost:8080/api/absen', formData.value);
        formData.value = {
            name: '',
            nim: null,
            timestamp: ''
        };
        fetchAbsenList();
    } catch (error) {
        console.error('Error creating absen:', error);
    }
};

const editAbsen = (id) => {
    const absen = absenList.value.find((a) => a.id === id);
    if (absen) {
        formData.value = { ...absen };
        isEditing.value = true;
    }
};

const updateAbsen = async () => {
    try {
        await axios.put(`http://localhost:8080/api/absen/${formData.value.id}`, formData.value);
        formData.value = { name: '', nim: null, timestamp: '' };
        isEditing.value = false;
        fetchAbsenList();
    } catch (error) {
        console.error('Error updating absen:', error);
    }
};

const deleteAbsen = async (id) => {
    try {
        await axios.delete(`http://localhost:8080/api/absen/${id}`);
        fetchAbsenList();
    } catch (error) {
        console.error('Error deleting absen:', error);
    }
};

onMounted(() => {
    fetchAbsenList();
});
</script>
