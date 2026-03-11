package edu.taskmanager.backend.chain;

import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagFilterTest {

    @Mock
    private TaskFilter nextFilter;

    private Tag requiredTag;
    private TagFilter tagFilter;

    @BeforeEach
    void setUp() {
        requiredTag = new Tag(1L, "urgent");
        tagFilter = new TagFilter(requiredTag);
    }


    @Test
    @DisplayName("filter(): задача с нужным тегом — возвращает true")
    void filter_taskHasRequiredTag_returnsTrue() {
        Task task = createTask(List.of(requiredTag));

        assertThat(tagFilter.filter(task)).isTrue();
    }

    @Test
    @DisplayName("filter(): задача без тегов — возвращает false")
    void filter_taskHasNoTags_returnsFalse() {
        Task task = createTask(new ArrayList<>());

        assertThat(tagFilter.filter(task)).isFalse();
    }

    @Test
    @DisplayName("filter(): список тегов задачи равен null — возвращает false")
    void filter_taskTagsNull_returnsFalse() {
        Task task = createTask(null);

        assertThat(tagFilter.filter(task)).isFalse();
    }

    @Test
    @DisplayName("filter(): задача содержит другие теги, но не нужный — возвращает false")
    void filter_taskHasOtherTagsOnly_returnsFalse() {
        Tag otherTag = new Tag(99L, "low-priority");
        Task task = createTask(List.of(otherTag));

        assertThat(tagFilter.filter(task)).isFalse();
    }

    @Test
    @DisplayName("filter(): задача содержит нужный тег среди нескольких — возвращает true")
    void filter_taskHasRequiredTagAmongMany_returnsTrue() {
        Tag other1 = new Tag(2L, "backend");
        Tag other2 = new Tag(3L, "bug");
        Task task = createTask(List.of(other1, requiredTag, other2));

        assertThat(tagFilter.filter(task)).isTrue();
    }

    @Test
    @DisplayName("filter(): тег совпадает, next возвращает true — итог true")
    void filter_tagMatches_nextReturnsTrue_returnsTrue() {
        tagFilter.setNext(nextFilter);
        Task task = createTask(List.of(requiredTag));
        when(nextFilter.filter(task)).thenReturn(true);

        assertThat(tagFilter.filter(task)).isTrue();
        verify(nextFilter, times(1)).filter(task);
    }

    @Test
    @DisplayName("filter(): тег совпадает, next возвращает false — итог false")
    void filter_tagMatches_nextReturnsFalse_returnsFalse() {
        tagFilter.setNext(nextFilter);
        Task task = createTask(List.of(requiredTag));
        when(nextFilter.filter(task)).thenReturn(false);

        assertThat(tagFilter.filter(task)).isFalse();
        verify(nextFilter, times(1)).filter(task);
    }

    @Test
    @DisplayName("filter(): тег НЕ совпадает — next не вызывается")
    void filter_tagDoesNotMatch_nextNeverCalled() {
        tagFilter.setNext(nextFilter);
        Task task = createTask(new ArrayList<>());

        assertThat(tagFilter.filter(task)).isFalse();
        verify(nextFilter, never()).filter(any());
    }

    @Test
    @DisplayName("filter(): теги null — next не вызывается")
    void filter_tagNull_nextNeverCalled() {
        tagFilter.setNext(nextFilter);
        Task task = createTask(null);

        assertThat(tagFilter.filter(task)).isFalse();
        verify(nextFilter, never()).filter(any());
    }


    @Test
    @DisplayName("filter(): тег с тем же id, но другим именем — считается совпавшим (equals по id)")
    void filter_sameIdDifferentName_treatedAsEqual() {
        Tag sameId = new Tag(1L, "другое имя");
        Task task = createTask(List.of(sameId));

        assertThat(tagFilter.filter(task)).isTrue();
    }

    @Test
    @DisplayName("filter(): тег с другим id, но тем же именем — не совпадает (equals по id)")
    void filter_sameNameDifferentId_treatedAsNotEqual() {
        Tag differentId = new Tag(42L, "urgent");
        Task task = createTask(List.of(differentId));

        assertThat(tagFilter.filter(task)).isFalse();
    }

    @Test
    @DisplayName("setNext(): установленный следующий фильтр вызывается при совпадении тега")
    void setNext_nextFilterIsCalledOnMatch() {
        tagFilter.setNext(nextFilter);
        Task task = createTask(List.of(requiredTag));
        when(nextFilter.filter(task)).thenReturn(true);

        tagFilter.filter(task);

        verify(nextFilter).filter(task);
    }

    @Test
    @DisplayName("setNext(): повторная установка next заменяет предыдущий фильтр")
    void setNext_replacingNext_newFilterIsUsed() {
        TaskFilter firstNext = mock(TaskFilter.class);
        TaskFilter secondNext = mock(TaskFilter.class);
        Task task = createTask(List.of(requiredTag));
        when(secondNext.filter(task)).thenReturn(true);

        tagFilter.setNext(firstNext);
        tagFilter.setNext(secondNext);
        tagFilter.filter(task);

        verify(firstNext, never()).filter(any());
        verify(secondNext, times(1)).filter(task);
    }

    @Test
    @DisplayName("setNext(null): next = null, цепочка завершается на текущем фильтре")
    void setNext_null_chainEndsAtCurrentFilter() {
        tagFilter.setNext(nextFilter);
        tagFilter.setNext(null);
        Task task = createTask(List.of(requiredTag));

        assertThat(tagFilter.filter(task)).isTrue();
        verify(nextFilter, never()).filter(any());
    }

    private Task createTask(List<Tag> tags) {
        Task task = new Task("Test task", LocalDateTime.now(), null, Priority.MEDIUM, TaskStatus.TODO);
        task.setTags(tags);
        return task;
    }
}