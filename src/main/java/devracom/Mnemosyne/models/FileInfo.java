package devracom.Mnemosyne.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    private String name;
    private String ext;
    private String url;
    private Long size;
    private Boolean isFile;
}