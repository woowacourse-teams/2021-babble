package gg.babble.babble.restdocs;


import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class ApiDocumentTest {

    protected static RequestSpecification specification;

    @LocalServerPort
    protected int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private static void initiateSpecification(final RestDocumentationContextProvider restDocumentation) {
        specification = new RequestSpecBuilder()
            .addFilter(documentationConfiguration(restDocumentation).operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint()))
            .build();
    }

    protected static RequestSpecification given() {
        return RestAssured.given(specification)
            .contentType(ContentType.JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    @BeforeEach
    protected void setUp(final RestDocumentationContextProvider restDocumentation) throws Exception {
        RestAssured.port = port;
        initiateSpecification(restDocumentation);
    }

    @AfterEach
    void tearDown() {
        truncateAllTables();
    }

    private void truncateAllTables() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                jdbcTemplate.execute("set FOREIGN_KEY_CHECKS = 0;");
                JdbcTestUtils.deleteFromTables(jdbcTemplate, getAllTables().toArray(new String[0]));
                jdbcTemplate.execute("set FOREIGN_KEY_CHECKS = 1;");
            }
        });
    }

    private List<String> getAllTables() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            List<String> tables = new ArrayList<>();
            try (ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"})) {
                while (resultSet.next()) {
                    tables.add(resultSet.getString("TABLE_NAME"));
                }
            }
            tables.remove("flyway_schema_history");
            return tables;
        } catch (SQLException exception) {
            throw new IllegalStateException(exception);
        }
    }

    protected void localhost_관리자가_추가_됨() {
        jdbcTemplate.execute("insert into administrator (ip, name) values ('127.0.0.1', 'localhost');");
    }

    protected void localhost_관리자가_제거_됨() {
        jdbcTemplate.execute("delete from administrator where ip = '127.0.0.1';");
    }
}

