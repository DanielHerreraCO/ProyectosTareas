import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { DragDropContext, Droppable, Draggable } from "@hello-pangea/dnd";
import { getTasks, updateTask } from "../service/taskService";

export default function TaskList() {
  const [tasks, setTasks] = useState([]);
  const navigate = useNavigate();

  const loadTasks = async () => {
    try {
      const res = await getTasks(); // axios devuelve { data: ... }
      console.log("Respuesta completa:", res);

      // Si res.data ya es el array
      if (Array.isArray(res)) {
        setTasks(res);
      }
      // Si viene envuelto en un objeto, lo sacamos
      else if (Array.isArray(res.data.data)) {
        setTasks(res.data.data);
      } else {
        setTasks([]);
      }
    } catch (e) {
      console.error("Error cargando tareas:", e);
      setTasks([]);
    }
  };

  useEffect(() => {
    loadTasks();
  }, []);

  const nextStatus = {
    PENDING: "IN_PROGRESS",
    IN_PROGRESS: "COMPLETED",
    COMPLETED: "COMPLETED",
  };

  const moveToNextState = async (task) => {
    const updated = { ...task, status: nextStatus[task.status] };
    await updateTask(task.id, updated);
    loadTasks();
  };

  // Agrupar tareas por estado (usando objeto para mutabilidad)
  const [columns, setColumns] = useState({
    PENDING: [],
    IN_PROGRESS: [],
    COMPLETED: [],
  });

  // Sincronizar columnas cuando cambian las tareas
  useEffect(() => {
    const grouped = { PENDING: [], IN_PROGRESS: [], COMPLETED: [] };
    (tasks || []).forEach((task) => {
      if (grouped[task.status]) grouped[task.status].push(task);
    });
    setColumns(grouped);
  }, [tasks]);

  // Estilos simples para columnas tipo Kanban
  const columnStyle = {
    flex: 1,
    margin: "0 10px",
    background: "#f4f4f4",
    borderRadius: "8px",
    padding: "16px",
    minHeight: "200px",
    boxShadow: "0 2px 8px #0001",
    boxSizing: "border-box"
  };
  const boardStyle = {
    display: "flex",
    gap: "20px",
    marginTop: "24px",
    flexWrap: "wrap"
  };

  // Manejar el drag and drop
  const onDragEnd = async (result) => {
    const { source, destination } = result;
    if (!destination) return;
    if (
      source.droppableId === destination.droppableId &&
      source.index === destination.index
    )
      return;

    const sourceCol = source.droppableId;
    const destCol = destination.droppableId;

    // Copiamos columnas actuales
    const newColumns = { ...columns };
    const sourceTasks = Array.from(newColumns[sourceCol]);
    const destTasks = Array.from(newColumns[destCol]);

    // Sacamos la tarea de la columna de origen
    const [movedTask] = sourceTasks.splice(source.index, 1);

    // Si se mueve a otra columna, actualizamos el estado de la tarea
    if (sourceCol !== destCol) {
      movedTask.status = destCol;
      destTasks.splice(destination.index, 0, movedTask);

      // Actualizar backend
      try {
        await updateTask(movedTask.id, movedTask);
      } catch (err) {
        console.error("Error actualizando tarea:", err);
      }
    } else {
      // Solo reordenar dentro de la misma columna
      sourceTasks.splice(destination.index, 0, movedTask);
    }

    // Guardar nuevo orden en UI
    newColumns[sourceCol] = sourceTasks;
    newColumns[destCol] = destTasks;
    setColumns(newColumns);

    // Recargar desde backend para mantener sincronizado
    loadTasks();
  };

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h2 style={{ textAlign: "center", margin: 0 }}>Tablero de Tareas</h2>
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
      <DragDropContext onDragEnd={onDragEnd}>
        <div style={boardStyle} className="kanban-board">
          {["PENDING", "IN_PROGRESS", "COMPLETED"].map((status) => (
            <Droppable droppableId={status} key={status}>
              {(provided, snapshot) => (
                <div
                  ref={provided.innerRef}
                  {...provided.droppableProps}
                  style={{
                    ...columnStyle,
                    background: snapshot.isDraggingOver
                      ? "#e0e7ff"
                      : columnStyle.background,
                  }}
                >
                  <h3 style={{ textAlign: "center" }}>
                    {status === "PENDING" && "Pendientes"}
                    {status === "IN_PROGRESS" && "En Progreso"}
                    {status === "COMPLETED" && "Completadas"}
                  </h3>
                  <ul
                    style={{ listStyle: "none", padding: 0, minHeight: "40px" }}
                  >
                    {columns[status].length === 0 && (
                      <li style={{ textAlign: "center", color: "#aaa" }}>
                        Sin tareas
                      </li>
                    )}
                    {columns[status].map((task, idx) => (
                      <Draggable
                        draggableId={String(task.id)}
                        index={idx}
                        key={task.id}
                      >
                        {(provided, snapshot) => (
                          <li
                            ref={provided.innerRef}
                            {...provided.draggableProps}
                            {...provided.dragHandleProps}
                            style={{
                              marginBottom: "12px",
                              background: "#fff",
                              borderRadius: "6px",
                              padding: "10px",
                              boxShadow: "0 1px 4px #0001",
                              opacity: snapshot.isDragging ? 0.7 : 1,
                              ...provided.draggableProps.style,
                            }}
                          >
                            <strong>{task.title}</strong>
                            <div style={{ fontSize: "0.95em", color: "#555" }}>
                              {task.description}
                            </div>
                            {status !== "COMPLETED" && (
                              <button
                                onClick={() => moveToNextState(task)}
                                style={{ marginTop: "8px" }}
                              >
                                Mover a{" "}
                                {nextStatus[task.status].replace("_", " ")}
                              </button>
                            )}
                          </li>
                        )}
                      </Draggable>
                    ))}
                    {provided.placeholder}
                  </ul>
                </div>
              )}
            </Droppable>
          ))}
        </div>
      </DragDropContext>
    </div>
  );
}
