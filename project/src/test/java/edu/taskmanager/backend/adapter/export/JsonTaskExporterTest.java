package edu.taskmanager.backend.adapter.export;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import edu.taskmanager.frontend.console.util.AppData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class JsonTaskExporterTest {

    @TempDir
    Path tempDir;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // export()
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("export(): возвращаемый JSON содержит корневой ключ 'tasks'")
    void export_returnedJsonHasTasksKey() throws IOException {
        JsonTaskExporter exporter = exporter("tasks.json");
        Task task = createTask(1L, "Test", Priority.HIGH);

        String json = exporter.export(List.of(task));

        JsonNode root = objectMapper.readTree(json);
        assertThat(root.has("tasks")).isTrue();
        assertThat(root.get("tasks").isArray()).isTrue();
    }

    @Test
    @DisplayName("export(): количество задач в JSON совпадает с переданным списком")
    void export_jsonContainsCorrectNumberOfTasks() throws IOException {
        JsonTaskExporter exporter = exporter("tasks.json");
        List<Task> tasks = List.of(
                createTask(1L, "First", Priority.LOW),
                createTask(2L, "Second", Priority.MEDIUM),
                createTask(3L, "Third", Priority.CRITICAL)
        );

        String json = exporter.export(tasks);

        JsonNode tasksArray = objectMapper.readTree(json).get("tasks");
        assertThat(tasksArray).hasSize(3);
    }

    @Test
    @DisplayName("export(): title задачи корректно сериализуется")
    void export_taskTitleSerializedCorrectly() throws IOException {
        JsonTaskExporter exporter = exporter("tasks.json");
        Task task = createTask(1L, "My Task Title", Priority.MEDIUM);

        String json = exporter.export(List.of(task));

        JsonNode first = objectMapper.readTree(json).get("tasks").get(0);
        assertThat(first.get("title").asText()).isEqualTo("My Task Title");
    }

    @Test
    @DisplayName("export(): приоритет задачи корректно сериализуется")
    void export_taskPrioritySerializedCorrectly() throws IOException {
        JsonTaskExporter exporter = exporter("tasks.json");
        Task task = createTask(1L, "Task", Priority.CRITICAL);

        String json = exporter.export(List.of(task));

        JsonNode first = objectMapper.readTree(json).get("tasks").get(0);
        assertThat(first.get("priority").asText()).isEqualTo("CRITICAL");
    }

    @Test
    @DisplayName("export(): создаёт файл по указанному пути")
    void export_createsFileAtOutputPath() {
        File outputFile = tempDir.resolve("output/tasks.json").toFile();
        JsonTaskExporter exporter = new JsonTaskExporter(outputFile.getAbsolutePath());

        exporter.export(List.of(createTask(1L, "Task", Priority.LOW)));

        assertThat(outputFile).exists().isFile();
    }

    @Test
    @DisplayName("export(): создаёт родительские директории, если их нет")
    void export_createsParentDirectoriesIfMissing() {
        File outputFile = tempDir.resolve("nested/dir/tasks.json").toFile();
        JsonTaskExporter exporter = new JsonTaskExporter(outputFile.getAbsolutePath());

        exporter.export(List.of(createTask(1L, "Task", Priority.LOW)));

        assertThat(outputFile.getParentFile()).isDirectory();
        assertThat(outputFile).exists();
    }

    @Test
    @DisplayName("export(): файл содержит тот же JSON, что и возвращаемая строка")
    void export_fileContentMatchesReturnedJson() throws IOException {
        File outputFile = tempDir.resolve("tasks.json").toFile();
        JsonTaskExporter exporter = new JsonTaskExporter(outputFile.getAbsolutePath());
        Task task = createTask(1L, "Task", Priority.HIGH);

        String returnedJson = exporter.export(List.of(task));

        JsonNode fromReturn = objectMapper.readTree(returnedJson);
        JsonNode fromFile = objectMapper.readTree(outputFile);
        assertThat(fromReturn).isEqualTo(fromFile);
    }

    @Test
    @DisplayName("export(): пустой список — JSON содержит пустой массив tasks")
    void export_emptyList_jsonHasEmptyTasksArray() throws IOException {
        JsonTaskExporter exporter = exporter("tasks.json");

        String json = exporter.export(new ArrayList<>());

        JsonNode tasksArray = objectMapper.readTree(json).get("tasks");
        assertThat(tasksArray.isArray()).isTrue();
        assertThat(tasksArray).isEmpty();
    }

    @Test
    @DisplayName("export(): dueDate сериализуется в строковом формате ISO (не timestamp)")
    void export_dueDateSerializedAsIsoString() throws IOException {
        JsonTaskExporter exporter = exporter("tasks.json");
        Task task = createTask(1L, "Task", Priority.LOW);
        task.setDueDate(LocalDateTime.of(2025, 6, 15, 10, 30));

        String json = exporter.export(List.of(task));

        JsonNode first = objectMapper.readTree(json).get("tasks").get(0);
        String dueDate = first.get("dueDate").asText();
        // должна быть строка, а не число
        assertThat(dueDate).contains("2025").contains("06").contains("15");
        assertThatCode(() -> objectMapper.readTree(json)).doesNotThrowAnyException();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // exportAppData()
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("exportAppData(): создаёт файл по указанному пути")
    void exportAppData_createsFileAtOutputPath() {
        File outputFile = tempDir.resolve("appdata.json").toFile();
        JsonTaskExporter exporter = new JsonTaskExporter(outputFile.getAbsolutePath());

        exporter.exportAppData(buildAppData());

        assertThat(outputFile).exists().isFile();
    }

    @Test
    @DisplayName("exportAppData(): файл содержит валидный JSON")
    void exportAppData_fileContainsValidJson() {
        File outputFile = tempDir.resolve("appdata.json").toFile();
        JsonTaskExporter exporter = new JsonTaskExporter(outputFile.getAbsolutePath());

        exporter.exportAppData(buildAppData());

        assertThatCode(() -> objectMapper.readTree(outputFile)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("exportAppData(): JSON содержит список tasks из AppData")
    void exportAppData_jsonContainsTasksFromAppData() throws IOException {
        File outputFile = tempDir.resolve("appdata.json").toFile();
        JsonTaskExporter exporter = new JsonTaskExporter(outputFile.getAbsolutePath());
        AppData data = buildAppData();

        exporter.exportAppData(data);

        JsonNode root = objectMapper.readTree(outputFile);
        assertThat(root.has("tasks")).isTrue();
        assertThat(root.get("tasks").isArray()).isTrue();
        assertThat(root.get("tasks")).hasSize(2);
    }

    @Test
    @DisplayName("exportAppData(): JSON содержит список tags из AppData")
    void exportAppData_jsonContainsTagsFromAppData() throws IOException {
        File outputFile = tempDir.resolve("appdata.json").toFile();
        JsonTaskExporter exporter = new JsonTaskExporter(outputFile.getAbsolutePath());
        AppData data = buildAppData();

        exporter.exportAppData(data);

        JsonNode root = objectMapper.readTree(outputFile);
        assertThat(root.has("tags")).isTrue();
        assertThat(root.get("tags")).hasSize(1);
        assertThat(root.get("tags").get(0).get("name").asText()).isEqualTo("feature");
    }

    @Test
    @DisplayName("exportAppData(): создаёт родительские директории, если их нет")
    void exportAppData_createsParentDirectoriesIfMissing() {
        File outputFile = tempDir.resolve("deep/nested/appdata.json").toFile();
        JsonTaskExporter exporter = new JsonTaskExporter(outputFile.getAbsolutePath());

        exporter.exportAppData(buildAppData());

        assertThat(outputFile.getParentFile()).isDirectory();
        assertThat(outputFile).exists();
    }

    @Test
    @DisplayName("exportAppData(): AppData без tasks — поле tasks отсутствует в JSON (NON_NULL)")
    void exportAppData_nullTasks_fieldAbsentInJson() throws IOException {
        File outputFile = tempDir.resolve("appdata.json").toFile();
        JsonTaskExporter exporter = new JsonTaskExporter(outputFile.getAbsolutePath());
        AppData data = new AppData(); // все поля null

        exporter.exportAppData(data);

        JsonNode root = objectMapper.readTree(outputFile);
        assertThat(root.has("tasks")).isFalse();
    }


    @Test
    @DisplayName("export(): getParentFile() == null — mkdirs не вызывается, файл создаётся")
    void export_pathWithNoParentDir_fileCreated() throws IOException {
        File realFile = tempDir.resolve("flat_export.json").toFile();
        // Анонимный подкласс: resolveFile() возвращает реальный File в tempDir,
        // но переопределённый так, что getParentFile() == null — покрывает ветку if(... != null) = false
        JsonTaskExporter exporter = new JsonTaskExporter(realFile.getAbsolutePath()) {
            @Override
            protected File resolveFile(String path) {
                return new File(path) {
                    @Override
                    public File getParentFile() {
                        return null; // ← ветка false
                    }
                };
            }
        };

        String json = exporter.export(List.of(createTask(1L, "Task", Priority.LOW)));

        assertThat(realFile).exists().isFile();
        assertThat(objectMapper.readTree(json).has("tasks")).isTrue();
    }

    @Test
    @DisplayName("export(): невалидный путь (директория вместо файла) — бросает RuntimeException")
    void export_invalidPath_throwsRuntimeException() {
        // Передаём путь к существующей директории — запись в неё как в файл вызовет IOException
        String dirPath = tempDir.toAbsolutePath().toString();
        JsonTaskExporter exporter = new JsonTaskExporter(dirPath);

        assertThatThrownBy(() -> exporter.export(List.of(createTask(1L, "Task", Priority.LOW))))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ошибка при экспорте задач в JSON");
    }

    @Test
    @DisplayName("exportAppData(): getParentFile() == null — mkdirs не вызывается, файл создаётся")
    void exportAppData_pathWithNoParentDir_fileCreated() {
        File realFile = tempDir.resolve("flat_appdata.json").toFile();
        // Анонимный подкласс: resolveFile() возвращает реальный File в tempDir,
        // но getParentFile() возвращает null — покрывает ветку if(... != null) = false
        JsonTaskExporter exporter = new JsonTaskExporter(realFile.getAbsolutePath()) {
            @Override
            protected File resolveFile(String path) {
                return new File(path) {
                    @Override
                    public File getParentFile() {
                        return null; // ← ветка false
                    }
                };
            }
        };

        exporter.exportAppData(buildAppData());

        assertThat(realFile).exists().isFile();
    }

    @Test
    @DisplayName("exportAppData(): невалидный путь (директория вместо файла) — бросает RuntimeException")
    void exportAppData_invalidPath_throwsRuntimeException() {
        String dirPath = tempDir.toAbsolutePath().toString();
        JsonTaskExporter exporter = new JsonTaskExporter(dirPath);

        assertThatThrownBy(() -> exporter.exportAppData(buildAppData()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ошибка при сохранении данных");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Вспомогательные методы
    // ──────────────────────────────────────────────────────────────────────────

    private JsonTaskExporter exporter(String fileName) {
        return new JsonTaskExporter(tempDir.resolve(fileName).toAbsolutePath().toString());
    }

    private Task createTask(Long id, String title, Priority priority) {
        Task task = new Task(title, LocalDateTime.now(), null, priority, TaskStatus.TODO);
        task.setId(id);
        return task;
    }

    private AppData buildAppData() {
        Tag tag = new Tag(1L, "feature");

        Task t1 = createTask(1L, "First task", Priority.HIGH);
        Task t2 = createTask(2L, "Second task", Priority.LOW);

        User user = new User("alice", "", User.Role.USER);
        user.setId(1L);

        AppData data = new AppData();
        data.setTasks(List.of(t1, t2));
        data.setTags(List.of(tag));
        data.setUsers(List.of(user));
        return data;
    }
}


