package trainingportal.reports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportsServiceImpl implements ReportsService {

    @Autowired
    private ReportsServiceExcel reportsServiceExcel;

    @Override
    public List<List> getFieldsFromTable(List<String> fields, String tableName, String fileName, String labelName) {
        return reportsServiceExcel.findFieldsFromTable(fields, tableName, fileName, labelName);
    }

    @Override
    public List<List> getFieldsFromTableWithCondition(List<String> fields, String tableName, String fileName, String labelName, String whereCondition) {
        return reportsServiceExcel.findFieldsFromTableWithCondition(fields, tableName, fileName, labelName, whereCondition);
    }

    @Override
    public List<List> getMultiFieldsFromTables(List<String> fields, String sql, String fileName, String labelName) {
        return reportsServiceExcel.findMultiFieldsFromTables(fields, sql, fileName, labelName);
    }
}