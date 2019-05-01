package trainingportal.mapper;

import org.springframework.jdbc.core.RowMapper;
import trainingportal.model.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskMapper implements RowMapper<Task> {
    public static final String SELECT_SQL
            = "SELECT taskId, homeworkId, taskDescription FROM MATERIAL";

    public static final String EDIT_SQL
            = "UPDATE MATERIAL SET  taskDescription = ?, homeworkId = ?";


    @Override
    public Task mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Long taskId = resultSet.getLong("taskId");

        Long homeworkId = resultSet.getLong("homeworkId");

        String taskDescription = resultSet.getString("taskDescription");


        return new Task(taskId, taskDescription, homeworkId);
    }
}