<template>
    <div>
        <h1>User Management</h1>

        <!-- User List -->
        <div class="card mb-4">
            <div class="card-header">User List</div>
            <div class="card-body">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Nim</th>
                            <th>Prodi</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(user, i) in users" :key="user.id">
                            <td>{{ i + 1 }}</td>
                            <td>{{ user.name }}</td>
                            <td>{{ user.nim }}</td>
                            <td>{{ user.prodi }}</td>
                            <td>
                                <button class="btn btn-sm btn-primary" @click="editUser(user.id)">Edit</button>
                                <button class="btn btn-sm btn-danger" @click="deleteUser(user.id)">Delete</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- User Form (Create or Edit) -->
        <div class="card">
            <div class="card-header">{{ isEditing ? 'Edit User' : 'Create User' }}</div>
            <div class="card-body">
                <form @submit.prevent="isEditing ? updateUser() : createUser()">
                    <div class="mb-3">
                        <label for="name" class="form-label">Name</label>
                        <input type="text" class="form-control" v-model="formData.name" required>
                    </div>
                    <div class="mb-3">
                        <label for="age" class="form-label">NIM</label>
                        <input type="number" class="form-control" v-model="formData.nim" required>
                    </div>
                    <div class="mb-3">
                        <label for="prodi" class="form-label">Prodi</label>
                        <input type="text" class="form-control" v-model="formData.prodi" required>
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

const users = ref([]);
const formData = ref({
    name: '',
    nim: null,
    prodi: ''
});
const isEditing = ref(false);

const fetchUsers = async () => {
    try {
        const response = await axios.get('http://localhost:8080/api/users');
        users.value = response.data;
    } catch (error) {
        console.error('Error fetching users:', error);
    }
};

const createUser = async () => {
    try {
        await axios.post('http://localhost:8080/api/users', formData.value);
        formData.value = {
            name: '',
            prodi: '',
            nim: null
        };
        fetchUsers();
    } catch (error) {
        console.error('Error creating user:', error);
    }
};

const editUser = (id) => {
    const user = users.value.find((u) => u.id === id);
    if (user) {
        formData.value = { ...user };
        isEditing.value = true;
    }
};

const updateUser = async () => {
    try {
        await axios.put(`http://localhost:8080/api/users/${formData.value.id}`, formData.value);
        formData.value = { name: '', age: '' };
        isEditing.value = false;
        fetchUsers();
    } catch (error) {
        console.error('Error updating user:', error);
    }
};

const deleteUser = async (id) => {
    try {
        await axios.delete(`http://localhost:8080/api/users/${id}`);
        fetchUsers();
    } catch (error) {
        console.error('Error deleting user:', error);
    }
};

onMounted(() => {
    fetchUsers();
});
</script>
