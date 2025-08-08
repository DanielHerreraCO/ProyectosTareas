import axiosInstance from '../api/axiosInstance';

const API_URL = '/api/users';

function getAuthHeader() {
  const token = localStorage.getItem('token');
  return token ? { Authorization: `Bearer ${token}` } : {};
}

export const getUsers = async () => {
  try {
    const res = await axiosInstance.get(API_URL, { headers: getAuthHeader() });
    return res.data;
  } catch (error) {
    console.error('Error al obtener usuarios:', error);
    throw error;
  }
};

export const createUser = async (user) => {
  try {
    const res = await axiosInstance.post(API_URL, user, { headers: getAuthHeader() });
    return res.data;
  } catch (error) {
    console.error('Error al crear usuario:', error);
    throw error;
  }
};

export const updateUser = async (id, user) => {
  try {
    const res = await axiosInstance.put(`${API_URL}/${id}`, user, { headers: getAuthHeader() });
    return res.data;
  } catch (error) {
    console.error('Error al actualizar usuario:', error);
    throw error;
  }
};

export const deleteUser = async (id) => {
  try {
    const res = await axiosInstance.delete(`${API_URL}/${id}`, { headers: getAuthHeader() });
    return res.data;
  } catch (error) {
    console.error('Error al eliminar usuario:', error);
    throw error;
  }
};
