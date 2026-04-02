package edu.taskmanager.frontend.console;

import edu.taskmanager.backend.proxy.TaskServiceProxy;
import edu.taskmanager.backend.repository.*;
import edu.taskmanager.backend.service.*;
import edu.taskmanager.frontend.console.handlers.*;
import edu.taskmanager.frontend.console.util.AppContext;

/**

Конфигуратор приложения. Создаёт и связывает зависимости.

При переходе на БД достаточно заменить реализации репозиториев.
*/
public class ApplicationContext {
    // Репозитории (сейчас in-memory)
    private final TaskRepository taskRepository = new InMemoryTaskRepository();
    private final ProjectRepository projectRepository = new InMemoryProjectRepository();
    private final TagRepository tagRepository = new InMemoryTagRepository();
    private final UserRepository userRepository = new InMemoryUserRepository();

    // Сервисы (бизнес-логика)
    private final TaskService realTaskService = new TaskServiceImpl(taskRepository);
    private final TaskService taskService = new TaskServiceProxy(realTaskService, userRepository);
    private final NotificationService notificationService = new NotificationService();

    // Команды
    private final CreateCommand createCommand;
    private final ListCommand listCommand;
    private final GetCommand getCommand;
    private final UpdateCommand updateCommand;
    private final DeleteCommand deleteCommand;
    private final FilterCommand filterCommand;
    private final SortingCommand sortingCommand;
    private final LoginCommand loginCommand;
    private final LogoutCommand logoutCommand;
    private final HelpCommand helpCommand;
    private final ExitCommand exitCommand;

    public ApplicationContext() {
    // Инициализация команд с передачей зависимостей
    this.createCommand = new CreateCommand(taskService, notificationService, projectRepository, tagRepository, userRepository);
    this.listCommand = new ListCommand(taskService, projectRepository, tagRepository, userRepository);
    this.getCommand = new GetCommand(taskService, projectRepository, tagRepository, userRepository);
    this.updateCommand = new UpdateCommand(taskService, projectRepository, tagRepository, userRepository);
    this.deleteCommand = new DeleteCommand(taskService, projectRepository, tagRepository, userRepository);
    this.filterCommand = new FilterCommand(taskService, tagRepository, projectRepository, userRepository);
    this.sortingCommand = new SortingCommand(taskService);
    this.loginCommand = new LoginCommand(userRepository);
    this.logoutCommand = new LogoutCommand();
    this.helpCommand = new HelpCommand(); // реестр команд будет передан позже
    this.exitCommand = new ExitCommand(() -> {}, new DataSaver(taskRepository, projectRepository, userRepository, tagRepository), "saved/dev_tasks_.json");
    }

    // Геттеры для команд
    public CreateCommand getCreateCommand() { return createCommand; }
    public ListCommand getListCommand() { return listCommand; }
    public GetCommand getGetCommand() { return getCommand; }
    public UpdateCommand getUpdateCommand() { return updateCommand; }
    public DeleteCommand getDeleteCommand() { return deleteCommand; }
    public FilterCommand getFilterCommand() { return filterCommand; }
    public SortingCommand getSortingCommand() { return sortingCommand; }
    public LoginCommand getLoginCommand() { return loginCommand; }
    public LogoutCommand getLogoutCommand() { return logoutCommand; }
    public HelpCommand getHelpCommand() { return helpCommand; }
    public ExitCommand getExitCommand() { return exitCommand; }

    // Геттеры для репозиториев (если нужны)
    public TaskRepository getTaskRepository() { return taskRepository; }
    public UserRepository getUserRepository() { return userRepository; }
    public ProjectRepository getProjectRepository() { return projectRepository; }
    public TagRepository getTagRepository() { return tagRepository; }

    // Геттер для контекста текущего пользователя (можно хранить в отдельном месте)
    public AppContext getAppContext() { return AppContext.getInstance(); }
}
