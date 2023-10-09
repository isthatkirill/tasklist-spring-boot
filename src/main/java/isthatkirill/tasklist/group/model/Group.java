package isthatkirill.tasklist.group.model;

import isthatkirill.tasklist.task.model.Task;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Getter
@Setter
@Entity
@Builder
@Table(name = "groups")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title", nullable = false, length = 128)
    String title;

    @Column(name = "desciption", nullable = false, length = 512)
    String description;

    @Column(name = "progress", nullable = false, length = 32)
    String progress;

    @OneToMany
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    List<Task> tasks;

}
