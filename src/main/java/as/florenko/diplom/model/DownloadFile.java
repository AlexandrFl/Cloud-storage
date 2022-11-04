package as.florenko.diplom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "files")
public class DownloadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "filename")
    private String filename;

    @Column(nullable = false, name = "size")
    private int size;

    @Column(name = "deleted")
    @JsonIgnore
    private int deleted;

    @JsonIgnore
    private String path;

}
