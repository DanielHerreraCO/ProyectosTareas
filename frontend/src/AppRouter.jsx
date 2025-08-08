
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import TaskList from "./components/TaskList";
import UserManagement from "./pages/UserManagement";
import RequireAuth from "./components/RequireAuth";

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route
          path="/task-list"
          element={
            <RequireAuth>
              <TaskList />
            </RequireAuth>
          }
        />
        <Route
          path="/user-management"
          element={
            <RequireAuth>
              <UserManagement />
            </RequireAuth>
          }
        />
        <Route path="*" element={<Login />} />
      </Routes>
    </BrowserRouter>
  );
}
