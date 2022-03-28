package hu.nextent.peas.jpa.view;

import hu.nextent.peas.jpa.constans.HibernateConstant;
import hu.nextent.peas.jpa.entity.*;
import hu.nextent.peas.jpa.entity.base.JpaEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Immutable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Table(name = "view_todo")
@Immutable
@NoArgsConstructor(force = true)
@Value
@ToString
@EqualsAndHashCode
public class ViewToDo implements JpaEntity<String>, Serializable {

    private static final long serialVersionUID = -717590555504393413L;

    @Id
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(updatable = false, nullable = false)
    private Long todoId;

    @Column(name = "language", nullable = true, length = 255)
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(name = "toDoType", nullable = false, updatable = true, length = 100)
    private ToDoTypeEnum toDoType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = true, length = 100)
    private ToDoStatusEnum status = ToDoStatusEnum.OPEN;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    @CreatedDate
    private OffsetDateTime createdDate;

    @Column(name = "modified_at", columnDefinition = "TIMESTAMP")
    @LastModifiedDate
    private OffsetDateTime modifiedDate;

    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

    // Nyelvesített üzenet kód
    @NotNull
    @Column(name = "messageCode", nullable = false, insertable = true, updatable = true, length = 100)
    private String messageCode;

    // Határidő
    @NotNull
    @Column(name = "deadline", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime deadline;

    // Todo elvégzése
    @Nullable
    @Column(name = "done", nullable = true, updatable = true, columnDefinition = "TIMESTAMP")
    private OffsetDateTime done;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "referenceType", nullable = false, updatable = true, length = 100)
    private ReferenceTypeEnum referenceType;

    // Todo számára
    // Redundás értéke, a gyors keresés miatt lett betével
    // Ez mind megtalálható vagy a task-ban vagy az értékelésben
    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "to_user_id",
            foreignKey =
            @ForeignKey(name = "fk_todo_to_user_id"),
            nullable = false, insertable = true, updatable = false)
    @ToString.Exclude
    private User toUser;

    @Nullable
    @ManyToOne
    @JoinColumn(
            name = "task_id",
            foreignKey = @ForeignKey(name = "fk_todo_task_id"),
            nullable = true,
            insertable = true,
            updatable = false
    )
    private Task task;

    @Nullable
    @ManyToOne
    @JoinColumn(
            name = "evaluation_id",
            foreignKey = @ForeignKey(name = "fk_todo_evaluation_id"),
            nullable = true,
            insertable = true,
            updatable = false
    )
    private Evaluation evaluation;

    @Nullable
    @ManyToOne
    @JoinColumn(
            name = "rating_id",
            foreignKey = @ForeignKey(name = "fk_todo_rating_id"),
            nullable = true,
            insertable = true,
            updatable = false
    )
    private Rating rating;

    @Nullable
    @ManyToOne
    @JoinColumn(
            name = "leadervirtue_id",
            foreignKey = @ForeignKey(name = "fk_todo_leadervirtue_id"),
            nullable = true,
            insertable = true,
            updatable = false
    )
    private LeaderVirtue leaderVirtue;

    @Nullable
    @ManyToOne
    @JoinColumn(
            name = "period_id",
            foreignKey = @ForeignKey(name = "fk_todo_period_id"),
            nullable = true,
            insertable = true,
            updatable = false
    )
    private Period period;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "company_id",
            foreignKey = @ForeignKey(name = "fk_todo_company_id"),
            nullable = false,
            insertable = true,
            updatable = false
    )
    private Company company;

    // Label Base
    @Column(name = "statusName", nullable = true)
    private String statusName;
}
