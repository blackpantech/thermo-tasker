import "./App.css";

const tasks: Task[] = [
  {
    id: "3b1b8cb7-4b7e-4b7e-8b7e-4b7e8b7e4b7e",
    topic: "Complete Project Proposal",
    dueDate: new Date("2023-12-31T23:59:59"),
    description: "Write and submit the project proposal for the new client.",
  },
  {
    id: "4b7e8b7e-4b7e-8b7e-4b7e-8b7e4b7e8b7e",
    topic: "Review Code Changes",
    dueDate: new Date("2023-12-20T17:00:00"),
    description: "Review the latest code changes and provide feedback.",
  },
  {
    id: "8b7e4b7e-4b7e-8b7e-4b7e-8b7e4b7e8b7e",
    topic: "Team Meeting",
    dueDate: new Date("2023-12-15T15:00:00"),
    description: "Discuss the project status and next steps with the team.",
  },
];

interface Task {
  id: string;
  topic: string;
  dueDate: Date;
  description: string;
}

function App() {
  return (
    <>
      <div className="tasksContainer">
        <h1>Tasks List</h1>
        <TasksList tasks={tasks} />
        <ExitButton />
      </div>
    </>
  );
}

function TasksList({ tasks }: { tasks: Task[] }) {
  return (
    <ul className="task-list">
      {tasks.map((task) => (
        <TaskItem task={task} />
      ))}
    </ul>
  );
}

function TaskItem({ task }: { task: Task }) {
  return (
    <li className="task-item">
      <div className="task-header">
        <span className="task-topic">{task.topic}</span>
        <span className="task-date">{task.dueDate.toLocaleString()}</span>
      </div>
      <div className="task-description">{task.description}</div>
    </li>
  );
}

function ExitButton() {
  return (
    <div className="back-link">
      <a className="homeButton" href="/">
        Return to Home
      </a>
    </div>
  );
}

export default App;
