package devracom.Mnemosyne.models.dto;

import devracom.Mnemosyne.models.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilesResponse {
    private List<FileInfo> files;
    private Long size;
}
