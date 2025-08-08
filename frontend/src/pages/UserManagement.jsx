import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

export default function UserManagement() {
  const [users, setUsers] = useState([]);
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [rol, setRol] = useState("user");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const loadUsers = async () => {
    try {
      const res = await axiosInstance.get("/api/users");
      setUsers(Array.isArray(res.data) ? res.data : res.data.data || []);
    } catch (e) {
      setUsers([]);
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    // Validación de contraseña segura
    const passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]).{8,}$/;
    if (!passwordRegex.test(password)) {
      setError("La contraseña debe tener al menos 8 caracteres, una mayúscula, un dígito y un caracter especial.");
      return;
    }
    try {
      await axiosInstance.post("/api/users", { username, email, password, rol });
      setSuccess("Usuario creado correctamente");
      setUsername(""); setEmail(""); setPassword(""); setRol("user");
      loadUsers();
    } catch (err) {
      console.error("Error al crear usuario:", err?.response?.data || err);
      setError("Error al crear usuario");
    }
  };

  return (
    <div style={{ minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center", background: "#f3f4f6" }}>
      <div className="user-card" style={{ maxWidth: 700, width: "100%", padding: 24, background: "#fff", borderRadius: 8, boxShadow: "0 2px 8px #0001" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h2 style={{ textAlign: "center", margin: 0 }}>Gestión de Usuarios</h2>
        <button
          onClick={() => {
            localStorage.removeItem("user");
            navigate("/login");
          }}
          style={{ padding: "8px 16px", borderRadius: 4, background: "#ef4444", color: "#fff", border: "none", marginLeft: 16 }}
        >
          Cerrar sesión
        </button>
      </div>
      <form onSubmit={handleCreate} style={{ marginBottom: 24 }}>
        <div style={{ display: "flex", gap: 8, marginBottom: 8 }}>
          <input type="text" placeholder="Usuario" value={username} onChange={e => setUsername(e.target.value)} required style={{ flex: 1 }} />
          <input type="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} required style={{ flex: 1 }} />
          <input type="password" placeholder="Contraseña" value={password} onChange={e => setPassword(e.target.value)} required style={{ flex: 1 }} />
          <select value={rol} onChange={e => setRol(e.target.value)} style={{ flex: 1 }}>
            <option value="user">user</option>
            <option value="admin">admin</option>
          </select>
          <button type="submit">Crear</button>
        </div>
        {error && <div style={{ color: "red" }}>{error}</div>}
        {success && <div style={{ color: "green" }}>{success}</div>}
      </form>
      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr style={{ background: "#f4f4f4" }}>
            <th>ID</th>
            <th>Usuario</th>
            <th>Email</th>
            <th>Rol</th>
          </tr>
        </thead>
        <tbody>
          {users.map(u => (
            <tr key={u.id}>
              <td>{u.id}</td>
              <td>{u.username}</td>
              <td>{u.email}</td>
              <td>{u.rol}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
  );
}
