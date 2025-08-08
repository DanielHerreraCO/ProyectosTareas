// Mejor práctica: usar async/await y manejo de errores más claro
// Además, exportar la función para reutilizarla en componentes React

const username = 'admin';
const password = '1234';
const url = 'http://localhost:8080/api/users';

export async function fetchUsersBasicAuth() {
  const headers = new Headers();
  headers.set('Authorization', 'Basic ' + btoa(`${username}:${password}`));

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: headers
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
}

// Ejemplo de uso:
// fetchUsersBasicAuth().then(data => console.log(data)).catch(console.error);
