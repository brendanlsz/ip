package duke.task;

import java.util.ArrayList;

import duke.exception.DukeException;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Contains the ArrayList tasks and methods associated
 * with this ArrayList.
 */
public class TaskManager {
    // Constants
    private static final int MAX_TASKS = 100;
    private static final String DELIMITER = " | ";
    private static final String SYMBOL_TODO = "T";
    private static final String SYMBOL_DEADLINE = "D";
    private static final String SYMBOL_EVENT = "E";
    private static final String SYMBOL_DONE = "1";
    private static final String SYMBOL_NOT_DONE = "0";

    // Task list
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Gets the number os tasks in the ArrayList tasks.
     *
     * @return The number of current tasks.
     */
    public int getTasksCount() {
        return tasks.size();
    }

    /**
     * Adds a task to the ArrayList tasks.
     *
     * @param task The task object to be added.
     * @return The task object that has been added.
     */
    public Task addTask(Task task) {
        tasks.add(task);
        return task;
    }

    /**
     * Deletes a task from the ArrayList tasks.
     *
     * @param id The ID of the task to be removed.
     * @return The task object that has been removed.
     */
    public Task deleteTask(int id) {
        Task task = tasks.get(id);
        tasks.remove(id);
        return task;
    }

    /**
     * Marks a task as done.
     *
     * @param id The ID of the task to be marked as done.
     * @return The task object that has been marked as done.
     */
    public Task markAsDone(int id) {
        Task task = tasks.get(id);
        task.markAsDone();
        return task;
    }

    /**
     * Preloads tasks from the file.
     *
     * @param reader Reads from the file used to save tasks.
     * @throws IOException If an I/O exception occurs.
     */
    // @@author brendanlsz-reused
    // Reused from https://www.techiedelight.com/how-to-read-a-file-using-bufferedreader-in-java/
    // with modifications
    public void preloadTasks(BufferedReader reader) throws IOException {
        int preloadTaskCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] description = line.trim().split("\\s*[|]\\s*");
            Task task;
            switch (description[0]) {
            case SYMBOL_TODO:
                task = new Todo(description[2]);
                break;
            case SYMBOL_DEADLINE:
                task = new Deadline(description[2], description[3]);
                break;
            case SYMBOL_EVENT:
                task = new Event(description[2], description[3]);
                break;
            default:
                return;
            }
            this.addTask(task);
            if (description[1].strip().equals(SYMBOL_DONE)) {
                task.markAsDone();
            }
            preloadTaskCount++;
        }
        System.out.println("Successfully preloaded " + preloadTaskCount + " tasks");
    }

    /**
     * Converts all the current tasks into a String.
     *
     * @return The String representing all the current tasks.
     * @throws DukeException If unable to save one or more tasks.
     */
    public String currentTasks() throws DukeException {
        StringBuilder lines = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            String taskType, isDone, description, time;
            boolean hasTime = false;
            Task task = tasks.get(i);
            isDone = task.getIsDone() ? SYMBOL_DONE : SYMBOL_NOT_DONE;
            description = task.getDescription();
            if (task instanceof Todo) {
                taskType = SYMBOL_TODO;
                time = "";
            } else if (task instanceof Deadline) {
                hasTime = true;
                taskType = SYMBOL_DEADLINE;
                time = ((Deadline) task).getBy();
            } else if (task instanceof Event) {
                hasTime = true;
                taskType = SYMBOL_EVENT;
                time = ((Event) task).getAt();
            } else {
                throw new DukeException("Error saving task no.: " + i);
            }
            lines.append(taskType + DELIMITER + isDone + DELIMITER + description +
                    (hasTime ? DELIMITER + time : "") + System.lineSeparator());
        }
        return lines.toString();
    }

    /**
     * Prints out all the tasks in the ArrayList tasks.
     */
    public void listTasks() {
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println(" " + i + "." +
                    tasks.get(i - 1).toString());
        }
    }
}
