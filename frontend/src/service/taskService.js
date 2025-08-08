import axiosInstance from '../api/axiosInstance';

const API_URL = '/task'; // Usar proxy o baseURL en axiosInstance

// Helper para obtener el token JWT
function getAuthHeader() {
  const token = localStorage.getItem('token');
  return token ? { Authorization: `Bearer ${token}` } : {};
}

export const getTasks = async () => {
  try {
    const res = await axiosInstance.get(API_URL, { headers: getAuthHeader() });
    return res.data;
  } catch (error) {
    console.error('Error al obtener tareas:', error);
    throw error;
  }
};

export const createTask = async (task) => {
  try {
    const res = await axiosInstance.post(API_URL, task, { headers: getAuthHeader() });
    return res.data;
  } catch (error) {
    console.error('Error al crear tarea:', error);
    throw error;
  }
};

export const updateTask = async (id, task) => {
  try {
    return axios.put(`/tasks/${id}`, task, {
    headers: { "Content-Type": "application/json" }
  });
  } catch (error) {
    console.error('Error al actualizar tarea:', error);
    throw error;
  }
};

export const deleteTask = async (id) => {
  try {
    const res = await axiosInstance.delete(`${API_URL}/${id}`, { headers: getAuthHeader() });
    return res.data;
  } catch (error) {
    console.error('Error al eliminar tarea:', error);
    throw error;
  }
};
