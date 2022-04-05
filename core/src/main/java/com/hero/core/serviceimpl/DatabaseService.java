package com.hero.core.serviceimpl;

import com.day.commons.datasource.poolservice.DataSourcePool;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@Component(immediate = true, service = DatabaseService.class)
public class DatabaseService {
    private static final String NAME_OF_DATASOURCE = "communities";

    @Reference
    DataSourcePool dataSourcePool;

    @Reference(target = "(&(objectclass=javax.sql.DataSource)(datasource.name=communities))")
    private DataSource dataSource;

    public ArrayList<String> getNames() {
        ArrayList<String> resultList = new ArrayList<>();
//        DataSource dataSource = null;
        Connection connection = null;

        try {
//            dataSource = (DataSource) dataSourcePool.getDataSource(NAME_OF_DATASOURCE);
            connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM communities.transport");
            while (resultSet.next()) {
                resultList.add(resultSet.getNString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
