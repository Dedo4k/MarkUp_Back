package vlad.lailo.markup.models.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ErrorDto {
    public int statusCode;
    public LocalDateTime dateTime;
    public String message;
    public String description;
}
