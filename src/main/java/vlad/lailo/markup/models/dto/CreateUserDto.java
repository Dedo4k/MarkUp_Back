package vlad.lailo.markup.models.dto;

import java.util.List;

public class CreateUserDto {
    public String username;
    public String password;
    public List<String> roles;
    public List<String> datasets;
}
