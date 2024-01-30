<template>
  <div>
    <h1>MataKuliah Management</h1>

    <!-- MataKuliah List -->
    <div class="card mb-4">
      <div class="card-header">MataKuliah List</div>
      <div class="card-body">
        <table class="table table-hover">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Dosen</th>
              <th>Link</th>
              <th>Hari</th>
              <th>Jam Mulai</th>
              <th>Jam Selesai</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(matakuliah, i) in matakuliahs" :key="matakuliah.id">
              <td>{{ i + 1 }}</td>
              <td>{{ matakuliah.name }}</td>
              <td>{{ matakuliah.dosen }}</td>
              <td>{{ matakuliah.link }}</td>
              <td>{{ matakuliah.hari }}</td>
              <td>{{ matakuliah.jamMulai }}</td>
              <td>{{ matakuliah.jamSelesai }}</td>
              <td>
                <button class="btn btn-sm btn-primary" @click="editMataKuliah(matakuliah.id)">Edit</button>
                <button class="btn btn-sm btn-danger" @click="deleteMataKuliah(matakuliah.id)">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- MataKuliah Form (Create or Edit) -->
    <div class="card">
      <div class="card-header">{{ isEditing ? 'Edit MataKuliah' : 'Create MataKuliah' }}</div>
      <div class="card-body">
        <form @submit.prevent="isEditing ? updateMataKuliah() : createMataKuliah()">
          <div class="mb-3">
            <label for="name" class="form-label">Name</label>
            <input type="text" class="form-control" v-model="formData.name" required>
          </div>
          <div class="mb-3">
            <label for="dosen" class="form-label">Dosen</label>
            <input type="text" class="form-control" v-model="formData.dosen" required>
          </div>
          <div class="mb-3">
            <label for="link" class="form-label">Link</label>
            <input type="text" class="form-control" v-model="formData.link" required>
          </div>
          <div class="mb-3">
            <label for="hari" class="form-label">Hari</label>
            <input type="text" class="form-control" v-model="formData.hari" required>
          </div>

          <div class="mb-3">
            <label for="jamMulai" class="form-label">Jam Mulai</label>
            <input type="time" class="form-control" v-model="formData.jamMulai" required>
          </div>

          <div class="mb-3">
            <label for="jamSelesai" class="form-label">Jam Selesai</label>
            <input type="time" class="form-control" v-model="formData.jamSelesai" required>
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

const matakuliahs = ref([]);
const formData = ref({
  name: '',
  dosen: '',
  link: '',
  hari: '',
  jamMulai: '',
  jamSelesai: ''
});
const isEditing = ref(false);

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
    hour12: true // Use 12-hour format with AM/PM
  });
};



const prepareFormDataForSubmit = (data) => {
  // If your backend requires specific formatting for LocalDateTime, do it here
  // Example: Assuming backend needs a full date-time string
  const today = new Date().toISOString().substring(0, 10); // YYYY-MM-DD
  return {
    ...data,
    jamMulai: data.jamMulai ? `${today}T${data.jamMulai}:00` : null,
    jamSelesai: data.jamSelesai ? `${today}T${data.jamSelesai}:00` : null
  };
};

const fetchMataKuliahs = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/matakuliah');
    matakuliahs.value = response.data.map(mk => ({
      ...mk,
      jamMulai: formatTime(mk.jamMulai),
      jamSelesai: formatTime(mk.jamSelesai)
    }));
  } catch (error) {
    console.error('Error fetching matakuliahs:', error);
  }
};

const createMataKuliah = async () => {
  try {
    const submitData = prepareFormDataForSubmit(formData.value);
    await axios.post('http://localhost:8080/api/matakuliah', submitData);
    resetFormData();
    fetchMataKuliahs();
  } catch (error) {
    console.error('Error creating matakuliah:', error);
  }
};

const editMataKuliah = (id) => {
  const matakuliah = matakuliahs.value.find((mk) => mk.id === id);
  if (matakuliah) {
    formData.value = { ...matakuliah };
    isEditing.value = true;
  }
};

const updateMataKuliah = async () => {
  try {
    const submitData = prepareFormDataForSubmit(formData.value);
    await axios.put(`http://localhost:8080/api/matakuliah/${formData.value.id}`, submitData);
    resetFormData();
    isEditing.value = false;
    fetchMataKuliahs();
  } catch (error) {
    console.error('Error updating matakuliah:', error);
  }
};

const deleteMataKuliah = async (id) => {
  try {
    await axios.delete(`http://localhost:8080/api/matakuliah/${id}`);
    fetchMataKuliahs();
  } catch (error) {
    console.error('Error deleting matakuliah:', error);
  }
};

const resetFormData = () => {
  formData.value = {
    name: '',
    dosen: '',
    link: '',
    hari: '',
    jamMulai: '',
    jamSelesai: ''
  };
};

onMounted(() => {
  fetchMataKuliahs();
});
</script>
