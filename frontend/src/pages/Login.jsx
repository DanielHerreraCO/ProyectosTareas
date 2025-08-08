import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const res = await axiosInstance.post("/api/auth/login", { email, password });
      // Suponiendo que el backend responde con el usuario y su rol
      const user = res.data;
      console.log(user);
      if (user && user.rol === "user") {
        localStorage.setItem("user", JSON.stringify(user));
        navigate("/task-list");
      } else if (user && user.rol === "admin") {
        localStorage.setItem("user", JSON.stringify(user));
        navigate("/user-management");
      } else {
        setError("No tienes permisos de usuario o el rol no es válido.");
      }
    } catch (err) {
      setError("Credenciales incorrectas o error de red.");
    }
  };

  return (
    <div style={{ minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center", background: "#f3f4f6", padding: 16 }}>
      <style>{`
        @media (max-width: 500px) {
          .login-card {
            max-width: 100% !important;
            padding: 12px !important;
            border-radius: 0 !important;
            box-shadow: none !important;
          }
        }
      `}</style>
      <div className="login-card" style={{ maxWidth: 400, width: "100%", padding: 24, background: "#fff", borderRadius: 8, boxShadow: "0 2px 8px #0001" }}>
        <h2 style={{ textAlign: "center" }}>Iniciar Sesión</h2>
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: 16 }}>
            <label>Email:</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              style={{ width: "100%", padding: 8, borderRadius: 4, border: "1px solid #ccc" }}
            />
          </div>
          <div style={{ marginBottom: 16 }}>
            <label>Contraseña:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              style={{ width: "100%", padding: 8, borderRadius: 4, border: "1px solid #ccc" }}
            />
          </div>
          {error && <div style={{ color: "red", marginBottom: 12 }}>{error}</div>}
          <button type="submit" style={{ width: "100%", padding: 10, borderRadius: 4, background: "#4f46e5", color: "#fff", border: "none" }}>
            Ingresar
          </button>
        </form>
      </div>
    </div>
  );
};

export default Login;
