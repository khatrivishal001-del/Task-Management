import React from 'react';
import { Task } from '../types/Task';

interface TaskListProps {
  tasks: Task[];
  onEdit: (task: Task) => void;
  onDelete: (id: number) => void;
  onToggleComplete: (task: Task) => void;
}

const TaskList: React.FC<TaskListProps> = ({ tasks, onEdit, onDelete, onToggleComplete }) => {
  return (
    <div className="task-list">
      {tasks.length === 0 ? (
        <p className="no-tasks">No tasks yet. Create one to get started!</p>
      ) : (
        tasks.map((task) => (
          <div key={task.id} className={`task-item ${task.completed ? 'completed' : ''}`}>
            <div className="task-content">
              <input
                type="checkbox"
                checked={task.completed}
                onChange={() => onToggleComplete(task)}
                className="task-checkbox"
              />
              <div className="task-details">
                <h3 className="task-title">{task.title}</h3>
                <p className="task-description">{task.description}</p>
              </div>
            </div>
            <div className="task-actions">
              <button onClick={() => onEdit(task)} className="btn-edit">
                Edit
              </button>
              <button onClick={() => onDelete(task.id!)} className="btn-delete">
                Delete
              </button>
            </div>
          </div>
        ))
      )}
    </div>
  );
};

export default TaskList;
