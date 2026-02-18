import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Task } from './types/Task';
import TaskList from './components/TaskList';
import TaskForm from './components/TaskForm';
import './App.css';

const API_BASE_URL = 'http://localhost:8080/api/tasks';

const App: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [taskToEdit, setTaskToEdit] = useState<Task | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Fetch all tasks
  const fetchTasks = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await axios.get<Task[]>(API_BASE_URL);
      setTasks(response.data);
    } catch (err) {
      setError('Failed to fetch tasks');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Initial fetch
  useEffect(() => {
    fetchTasks();
  }, []);

  // Handle form submission
  const handleFormSubmit = async (taskData: Omit<Task, 'id' | 'createdAt' | 'updatedAt'>) => {
    try {
      if (taskToEdit) {
        // Update existing task
        const response = await axios.put<Task>(`${API_BASE_URL}/${taskToEdit.id}`, taskData);
        setTasks(tasks.map(task => task.id === taskToEdit.id ? response.data : task));
      } else {
        // Create new task
        const response = await axios.post<Task>(API_BASE_URL, taskData);
        setTasks([...tasks, response.data]);
      }
      setShowForm(false);
      setTaskToEdit(null);
    } catch (err) {
      setError('Failed to save task');
      console.error(err);
    }
  };

  // Handle edit
  const handleEdit = (task: Task) => {
    setTaskToEdit(task);
    setShowForm(true);
  };

  // Handle delete
  const handleDelete = async (id: number) => {
    if (!window.confirm('Are you sure you want to delete this task?')) return;

    try {
      await axios.delete(`${API_BASE_URL}/${id}`);
      setTasks(tasks.filter(task => task.id !== id));
    } catch (err) {
      setError('Failed to delete task');
      console.error(err);
    }
  };

  // Handle toggle complete
  const handleToggleComplete = async (task: Task) => {
    const updatedTask = { ...task, completed: !task.completed };
    try {
      const response = await axios.put<Task>(`${API_BASE_URL}/${task.id}`, updatedTask);
      setTasks(tasks.map(t => t.id === task.id ? response.data : t));
    } catch (err) {
      setError('Failed to update task');
      console.error(err);
    }
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>Task Management Application</h1>
        <p>Manage your tasks efficiently</p>
      </header>

      <main className="app-main">
        {error && <div className="error-message">{error}</div>}

        <div className="app-container">
          {showForm && (
            <div className="form-container">
              <TaskForm
                taskToEdit={taskToEdit}
                onSubmit={handleFormSubmit}
                onCancel={() => {
                  setShowForm(false);
                  setTaskToEdit(null);
                }}
              />
            </div>
          )}

          <div className="tasks-section">
            {!showForm && (
              <button
                onClick={() => setShowForm(true)}
                className="btn-create"
              >
                + Create New Task
              </button>
            )}

            {loading ? (
              <p className="loading">Loading tasks...</p>
            ) : (
              <TaskList
                tasks={tasks}
                onEdit={handleEdit}
                onDelete={handleDelete}
                onToggleComplete={handleToggleComplete}
              />
            )}
          </div>
        </div>
      </main>
    </div>
  );
};

export default App;
