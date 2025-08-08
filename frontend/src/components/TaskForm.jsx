import { useState } from 'react';
import { createTask } from '../service/taskService';

export default function TaskForm({ onTaskCreated }) {
  const [title, setTitle] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newTask = {
      title,
      status: 'PENDING'
    };
    await createTask(newTask);
    onTaskCreated();
    setTitle('');
  };

  return (
    <form onSubmit={handleSubmit} style={{ display: "flex", gap: 8, marginBottom: 16, flexWrap: "wrap" }}>
      <input
        type="text"
        placeholder="Nueva tarea"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        required
        style={{ flex: 1, minWidth: 0 }}
      />
      <button type="submit" style={{ minWidth: 100 }}>Agregar</button>
    </form>
  );
}
