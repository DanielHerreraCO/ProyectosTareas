if (sourceCol !== destCol) {
  const updatedTask = { ...movedTask, status: destCol };
  destTasks.splice(destination.index, 0, updatedTask);

  // Actualizar backend con el nuevo estado
  try {
    await updateTask(updatedTask.id, updatedTask);
  } catch (err) {
    console.error("Error actualizando tarea:", err);
  }
}