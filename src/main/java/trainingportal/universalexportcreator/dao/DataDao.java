package trainingportal.universalexportcreator.dao;

import java.util.List;

public interface DataDao {

    List<List> findFieldsFromTable(List<String> fields, String tableName, String fileName, String labelName);

}